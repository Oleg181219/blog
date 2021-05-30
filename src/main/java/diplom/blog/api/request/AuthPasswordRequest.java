package diplom.blog.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthPasswordRequest {

    private String code;

    private String password;

    private String captcha;

    private String captchaSecret;

}
