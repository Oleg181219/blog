package diplom.blog.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModerationRequest {

    private Long id;

    private String decision;

}
