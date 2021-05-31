package diplom.blog.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
@Component
public class StatisticResponse  implements Response{

    private int postsCount;

    private int likesCount;

    private int dislikesCount;

    private int viewsCount;

    private int firstPublication;

}
