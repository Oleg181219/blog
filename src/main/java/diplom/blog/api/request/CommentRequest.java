package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRequest {

    @JsonProperty("parent_id")
    private Long parentId;

    @JsonProperty("post_id")
    private Long postId;

    private String text;


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
