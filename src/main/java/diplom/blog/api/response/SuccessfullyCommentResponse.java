package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessfullyCommentResponse {

    private Long id;

}
