package diplom.blog.model.DtoModel;



public class CommentDTO {
    private long id;
    private long timestamp;
    private String text;
    private UserCommentDTO user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserCommentDTO getUser() {
        return user;
    }

    public void setUser(UserCommentDTO user) {
        this.user = user;
    }
}
