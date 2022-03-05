package xyz.jocn.chat.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

	@Pointcut("execution(* xyz.jocn.chat..*Service.*(..))")
	void servicePointCut() {
	}

	@Pointcut("@annotation(xyz.jocn.chat.common.aop.ExecutionTime)")
	void executionTimePointCut() {
	}

	@Pointcut("@annotation(xyz.jocn.chat.common.aop.ParameterLog)")
	void parameterLogPointCut() {
	}
}
