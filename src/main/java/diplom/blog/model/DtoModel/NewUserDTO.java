package diplom.blog.model.DtoModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewUserDTO {

    @JsonProperty(value = "e_mail")
    private final String email;

    private final String name;

    private final String password;

    private final String captcha;

    @JsonProperty(value = "captcha_secret")
    private final String captchaSecret;

    public NewUserDTO(String email, String name, String password, String captcha, String captchaSecret) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.captcha = captcha;
        this.captchaSecret = captchaSecret;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public String getCaptchaSecret() {
        return captchaSecret;
    }
}
