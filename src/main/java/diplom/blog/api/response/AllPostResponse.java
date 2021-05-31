package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import diplom.blog.model.DtoModel.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllPostResponse implements Response{

    private int count;

    private List<PostDTO> posts;


}
