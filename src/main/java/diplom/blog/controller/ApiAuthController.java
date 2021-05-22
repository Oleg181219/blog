package diplom.blog.controller;

import diplom.blog.api.request.AuthPasswordRequest;
import diplom.blog.api.request.AuthRestoreRequest;
import diplom.blog.api.request.LoginRequest;
import diplom.blog.api.response.AuthResponse;
import diplom.blog.api.response.LoginResponse;
import diplom.blog.api.response.ResultResponse;
import diplom.blog.model.DtoModel.CaptchaDTO;
import diplom.blog.model.DtoModel.NewUserDTO;
import diplom.blog.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@Api(value = "/api/auth", description = "Операции с профилем")
public class ApiAuthController {


    private final AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Авторизация
     */
    @PostMapping("/login")
    @ApiOperation(value = "Авторизация")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     * Logout
     */
    @GetMapping("/logout")
    @ApiOperation(value = "Logout")
    public ResponseEntity<ResultResponse> logout() {
        return authService.logout();
    }

    /**
     * Проверка авторизации
     */
    @GetMapping("/check")
    @ApiOperation(value = "Проверка авторизации")
    public ResponseEntity<LoginResponse> check(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(new LoginResponse());
        }
        return authService.check(principal);
    }

    /**
     * Генерация капчи
     */
    @GetMapping("/captcha")
    @ApiOperation(value = "Генерация капчи")
    public CaptchaDTO captcha() {
        return authService.captcha();
    }

    /**
     * Регистрация нового пользователя
     */
    @PostMapping("/register")
    @ApiOperation(value = "Регистрация нового пользователя")
    public AuthResponse register(@RequestBody @Valid NewUserDTO user) {
        return authService.register(user);
    }

    /**
     * Запрос ссылки на восстановление пароля
     */
    @PostMapping("/restore")
    @ApiOperation(value = "Запрос ссылки на восстановление пароля")
    public ResponseEntity<ResultResponse> restore(@RequestBody AuthRestoreRequest email) throws MessagingException {
        return authService.restorePassword(email.getEmail());
    }

    /**
     * Восстановление пароля
     */
    @PostMapping("/password")
    @ApiOperation(value = "Восстановление пароля")
    public ResponseEntity<?> newPassword(@RequestBody AuthPasswordRequest authPasswordRequest) {
        return authService.authPassword(authPasswordRequest);
    }
}
