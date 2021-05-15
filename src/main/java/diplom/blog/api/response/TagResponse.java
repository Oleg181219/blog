package diplom.blog.api.response;

import diplom.blog.model.DtoModel.TagDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TagResponse {

    private ArrayList<TagDTO> tags;

    public ArrayList<TagDTO> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagDTO> tags) {
        this.tags = tags;
    }
}
