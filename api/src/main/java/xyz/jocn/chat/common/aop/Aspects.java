package xyz.jocn.chat.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Aspects {

	@Component
	@Aspect
	public static class ParameterLogAspect {

		@Around("xyz.jocn.chat.common.aop.Pointcuts.parameterLogPointCut()")
		public Object execute(ProceedingJoinPoint pjp) throws Throwable {
			log.info("{} : {}", pjp.getSignature(), pjp.getArgs());
			return pjp.proceed();
		}
	}

	@Component
	@Aspect
	public static class ExecutionTimeAspect {

		@Around("xyz.jocn.chat.common.aop.Pointcuts.executionTimePointCut()")
		public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			Object proceed = pjp.proceed();
			stopWatch.stop();

			long totalMillis = stopWatch.getTotalTimeMillis();
			if (totalMillis >= 1000) {
				log.warn("Time Taken : {} ms, Target : {}", totalMillis, pjp.getSignature());
			} else {
				log.info("Time Taken : {} ms, Target : {}", totalMillis, pjp.getSignature());
			}

			return proceed;
		}
	}

}
