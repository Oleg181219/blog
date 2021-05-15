package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostVotesRequest {
    @JsonProperty("post_id")
    private Long postId;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
