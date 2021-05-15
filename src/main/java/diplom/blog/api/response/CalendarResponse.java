package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarResponse {

    List<String> years;

    Map<String, Integer> posts;

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public Map<String, Integer> getPosts() {
        return posts;
    }

    public void setPosts(Map<String, Integer> posts) {
        this.posts = posts;
    }
}
