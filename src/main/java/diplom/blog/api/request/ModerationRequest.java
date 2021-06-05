package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import diplom.blog.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ModerationRequest {

    @JsonProperty("post_id")
    private Long id;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private String decision;

}
