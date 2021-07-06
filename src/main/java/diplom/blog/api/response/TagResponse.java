package diplom.blog.api.response;

import diplom.blog.model.dtoModel.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@AllArgsConstructor
@Data
@Component
public class TagResponse  implements Response{

    private ArrayList<TagDTO> tags;

}
