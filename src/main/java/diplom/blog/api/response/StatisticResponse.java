package diplom.blog.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
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
