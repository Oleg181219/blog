package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import diplom.blog.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyProfileRequest {

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Pattern(regexp = "([А-Яа-яA-Za-z0-9-_]+)")
    private String name;

    @JsonProperty("e_mail")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private String email;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = Config.STRING_AUTH_SHORT_PASSWORD)
    private String password;

    private Integer removePhoto;

    private MultipartFile photo;

}
