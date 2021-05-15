package diplom.blog.api.response;

import org.springframework.stereotype.Component;

@Component
public class StatisticResponse {

    private Long postsCount;

    private Long likesCount;

    private Long dislikesCount;

    private Long viewsCount;

    private Long firstPublication;


    public Long getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(Long postsCount) {
        this.postsCount = postsCount;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public Long getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(Long dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public Long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Long getFirstPublication() {
        return firstPublication;
    }

    public void setFirstPublication(Long firstPublication) {
        this.firstPublication = firstPublication;
    }
}
