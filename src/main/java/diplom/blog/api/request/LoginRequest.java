package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import diplom.blog.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class LoginRequest {

    @JsonProperty("e_mail")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private String email;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = Config.STRING_AUTH_SHORT_PASSWORD)
    private String password;

}
