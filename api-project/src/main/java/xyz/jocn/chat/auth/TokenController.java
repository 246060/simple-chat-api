package xyz.jocn.chat.auth;

import static org.springframework.http.MediaType.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import javax.validation.Valid;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.auth.dto.TokenCreateRequestDto;
import xyz.jocn.chat.auth.dto.TokenRefreshRequestDto;
import xyz.jocn.chat.auth.dto.TokenResponseDto;
import xyz.jocn.chat.common.dto.ApiResponseDto;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/token")
@RestController
public class TokenController {

	private final String JSON = MediaType.APPLICATION_JSON_VALUE;
	private final TokenService tokenService;

	@PostMapping(consumes = JSON, produces = JSON)
	public ResponseEntity<ApiResponseDto> generate(
		@Valid @RequestBody TokenCreateRequestDto tokenCreateRequestDto
	) {
		TokenResponseDto dto = tokenService.generateToken(tokenCreateRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noStore()).body(success(dto));
	}

	@PostMapping(value = "/refresh", consumes = JSON, produces = JSON)
	public ResponseEntity<ApiResponseDto> refresh(
		@Valid @RequestBody TokenRefreshRequestDto tokenRefreshRequestDto
	) {
		TokenResponseDto dto = tokenService.refresh(tokenRefreshRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noStore()).body(success(dto));
	}

}
