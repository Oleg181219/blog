package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import diplom.blog.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class NewUserRequest {


    @JsonProperty("e_mail")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private final String email;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Pattern(regexp = "([А-Яа-яA-Za-z0-9-_]+)")
    private final String name;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 10, message = Config.STRING_AUTH_SHORT_PASSWORD)
    private final String password;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private final String captcha;

    @JsonProperty("captcha_secret")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private final String captchaSecret;

}
