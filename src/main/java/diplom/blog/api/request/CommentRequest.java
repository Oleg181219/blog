package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {

    @JsonProperty("parent_id")
    private Long parentId;

    @JsonProperty("post_id")
    private Long postId;

    private String text;

}
