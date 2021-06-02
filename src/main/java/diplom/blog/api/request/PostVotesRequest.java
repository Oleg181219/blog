package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostVotesRequest {

    @JsonProperty("post_id")
    private Long postId;


}
