package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationRequest {

    @JsonProperty("post_id")
    private Long id;

    private String decision;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
