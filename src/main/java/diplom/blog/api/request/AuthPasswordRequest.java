package diplom.blog.api.request;

import diplom.blog.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AuthPasswordRequest {

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private String code;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = Config.STRING_AUTH_SHORT_PASSWORD)
    private String password;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private String captcha;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private String captchaSecret;

}
