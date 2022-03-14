package xyz.jocn.chat.common.exception;

import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.chat_space.exception.RoomException;
import xyz.jocn.chat.common.dto.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RoomException.class)
	public ResponseEntity handleRoomException(RoomException ex, WebRequest request) {
		log.error(ex.getMessage());
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorResponse error = new ErrorResponse(status.value(), ex.getMessage());
		return this.handleExceptionInternal(ex, fail(error), headers, status, request);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		log.error(ex.getMessage());
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		ErrorResponse error = new ErrorResponse(status.value(), ex.getMessage());
		return this.handleExceptionInternal(ex, fail(error), headers, status, request);
	}

	@ExceptionHandler(ApiAccessDenyException.class)
	public ResponseEntity handleApiAccessDenyException(ApiAccessDenyException ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.FORBIDDEN;
		ErrorResponse error = new ErrorResponse(status.value(), ex.getMessage());
		return this.handleExceptionInternal(ex, fail(error), headers, status, request);
	}

	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity handleResourceAlreadyExistException(ResourceAlreadyExistException ex, WebRequest request) {
		log.error(ex.getMessage());
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorResponse error = new ErrorResponse(status.value(), ex.getDescription(), ex.getMessage());
		return this.handleExceptionInternal(ex, fail(error), headers, status, request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		log.error(ex.getMessage());
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorResponse error = new ErrorResponse(status.value(), ex.getDescription(), ex.getMessage());
		return this.handleExceptionInternal(ex, fail(error), headers, status, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleHighestException(Exception ex, WebRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%s : %s", ex.getClass().getSimpleName(), ex.getMessage()));
		sb.append(System.lineSeparator());

		for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
			sb.append("\t");
			sb.append(stackTraceElement);
			sb.append(System.lineSeparator());
		}
		log.error(sb.toString());

		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorResponse error = new ErrorResponse(status.value(), "unknown server error", ex.getMessage());
		return this.handleExceptionInternal(ex, fail(error), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
		HttpStatus status, WebRequest request) {
		return super.handleMissingPathVariable(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleMissingServletRequestParameter(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleServletRequestBindingException(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleConversionNotSupported(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
		HttpStatus status, WebRequest request) {
		return super.handleTypeMismatch(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleHttpMessageNotReadable(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleHttpMessageNotWritable(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final String desc = "argument invalid";
		ErrorResponse error = new ErrorResponse(status.value(), desc, ex.getBindingResult().getFieldErrors());
		return this.handleExceptionInternal(ex, fail(error), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleMissingServletRequestPart(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
		WebRequest request) {
		return super.handleBindException(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
		HttpStatus status, WebRequest request) {
		return super.handleNoHandlerFoundException(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
		HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
		return super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest);
	}
}
