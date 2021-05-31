package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessfullyCommentResponse  implements Response{

    private Long id;

}
