package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import diplom.blog.model.DtoModel.PostDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllPostResponse {

    private int count;

    private List<PostDTO> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }
}
