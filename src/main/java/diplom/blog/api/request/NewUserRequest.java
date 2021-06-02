package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NewUserRequest {


    @JsonProperty("e_mail")
    private final String email;

    private final String name;

    private final String password;

    private final String captcha;

    @JsonProperty("captcha_secret")
    private final String captchaSecret;

}
