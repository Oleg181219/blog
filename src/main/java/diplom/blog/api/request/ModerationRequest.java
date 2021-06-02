package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModerationRequest {

    @JsonProperty("post_id")
    private Long id;

    private String decision;

}
