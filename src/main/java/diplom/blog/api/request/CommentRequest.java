package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import diplom.blog.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {

    @JsonProperty("parent_id")
    private Long parentId;

    @JsonProperty("post_id")
    private Long postId;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private String text;

}
