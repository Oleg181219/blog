package diplom.blog.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
@Component
public class StatisticResponse {

    private Long postsCount;

    private Long likesCount;

    private Long dislikesCount;

    private Long viewsCount;

    private Long firstPublication;

}
