package diplom.blog.api.request;

import diplom.blog.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;


@Data
@AllArgsConstructor
public class NewPostRequest {

    private Long  timestamp;
    private int active;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 3, message = Config.STRING_AUTH_SHORT_PASSWORD)
    private String title;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 3, message = Config.STRING_AUTH_SHORT_PASSWORD)
    private String text;

    private Set<String> tags;


}
