package diplom.blog.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import diplom.blog.model.Enum.ModerationStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_active", nullable = false)
    private int isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", columnDefinition = "enum", nullable = false)
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private int moderatorId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //----------------------------------------------
// One post Many post_comments------------------------------------------------------
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostComment> postComments = new HashSet<>();

    public Set<PostComment> getPostComments() {
        return postComments;
    }

    public boolean addPostComment(PostComment postComment) {
        postComment.setPost(this);
        return getPostComments().add(postComment);
    }

    public void removePostComment(PostComment postComment) {
        getPostComments().remove(postComment);
    }

    public void setPostComments(Set<PostComment> postComments) {
        this.postComments = postComments;
    }

    //-----------------------------------------------------------------------------------
//    One post Many postVotes-------------------------------------------------------
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostVotes> postVotes = new HashSet<>();

    public Set<PostVotes> getPostVotes() {
        return postVotes;
    }

    public boolean addPostVote(PostVotes postVotes) {
        postVotes.setPost(this);
        return getPostVotes().add(postVotes);
    }

    public void removePostVote(PostVotes postVotes) {
        getPostVotes().remove(postVotes);
    }

    public void setPostVotes(Set<PostVotes> postVotes) {
        this.postVotes = postVotes;
    }

    //----------------------------------------------------------------------------------
    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "time", nullable = false)
    private Date time;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @ManyToMany
    @JoinTable(name = "tag2post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public Set<Tag> getTags() {
        return tags;
    }

    public Post() {
    }

    public Post(Long id,
                int isActive,
                ModerationStatus moderationStatus,
                int moderatorId,
                User user,
                Date time,
                String title,
                String text,
                int viewCount) {
        this.id = id;
        this.isActive = isActive;
        this.moderationStatus = moderationStatus;
        this.moderatorId = moderatorId;
        this.user = user;
        this.time = time;
        this.title = title;
        this.text = text;
        this.viewCount = viewCount;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public Enum getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
