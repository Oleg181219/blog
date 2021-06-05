package diplom.blog.controller;

import diplom.blog.api.request.AuthPasswordRequest;
import diplom.blog.api.request.AuthRestoreRequest;
import diplom.blog.api.request.LoginRequest;
import diplom.blog.api.request.NewUserRequest;
import diplom.blog.api.response.Response;
import diplom.blog.model.DtoModel.CaptchaDTO;
import diplom.blog.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult error) {
        return  authService.login(loginRequest, error);
    }

    /**
     * Logout
     */
    @GetMapping("/logout")
    @ApiOperation(value = "Logout")
    public ResponseEntity<?> logout() {
        return authService.logout();
    }

    /**
     * Проверка авторизации
     */
    @GetMapping("/check")
    @ApiOperation(value = "Проверка авторизации")
    public ResponseEntity<Response> check() {
        return authService.check();
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
    public ResponseEntity<Response> register(@Valid @RequestBody  NewUserRequest user,  BindingResult error) {
        return authService.register(user, error);
    }

    /**
     * Запрос ссылки на восстановление пароля
     */
    @PostMapping("/restore")
    @ApiOperation(value = "Запрос ссылки на восстановление пароля")
    public ResponseEntity<Response> restore(@Valid @RequestBody AuthRestoreRequest email, BindingResult error) throws MessagingException {
        return authService.restorePassword(email.getEmail(), error);
    }

    /**
     * Восстановление пароля
     */
    @PostMapping("/password")
    @ApiOperation(value = "Восстановление пароля")
    public ResponseEntity<Response> newPassword(@Valid @RequestBody AuthPasswordRequest authPasswordRequest, BindingResult error) {
        return authService.authPassword(authPasswordRequest, error);
    }
}
