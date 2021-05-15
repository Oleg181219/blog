package diplom.blog.api.request;

import java.util.Set;


public class NewPostRequest {

    private Long  timestamp;
    private int active;
    private String title;
    private String text;
    private Set<String> tags;

    public NewPostRequest(Long timestamp, int active, String title, String text, Set<String> tags) {
        this.timestamp = timestamp;
        this.active = active;
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    public NewPostRequest() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
