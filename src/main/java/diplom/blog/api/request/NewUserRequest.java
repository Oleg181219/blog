package diplom.blog.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NewUserRequest {


    private final String email;

    private final String name;

    private final String password;

    private final String captcha;

    private final String captchaSecret;

}
