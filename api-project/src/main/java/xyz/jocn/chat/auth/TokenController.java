package xyz.jocn.chat.auth;

import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import javax.validation.Valid;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/token")
@RestController
public class TokenController {

	private final TokenService tokenService;

	@PostMapping
	public ResponseEntity generate(@RequestBody @Valid TokenCreateRequestDto tokenCreateRequestDto) {
		TokenResponseDto dto = tokenService.generateToken(tokenCreateRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noStore()).body(success(dto));
	}

	@PostMapping("/refresh")
	public ResponseEntity refresh(@RequestBody @Valid TokenRefreshRequestDto tokenRefreshRequestDto) {
		TokenResponseDto dto = tokenService.refresh(tokenRefreshRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noStore()).body(success(dto));
	}
}
