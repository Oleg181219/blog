package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import diplom.blog.model.DtoModel.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllPostResponse {

    private int count;

    private List<PostDTO> posts;


}
