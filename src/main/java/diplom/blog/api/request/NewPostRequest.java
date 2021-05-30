package diplom.blog.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;


@Data
@AllArgsConstructor
public class NewPostRequest {

    private Long  timestamp;
    private int active;
    private String title;
    private String text;
    private Set<String> tags;


}
