package diplom.blog.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import diplom.blog.model.Enum.Role;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "is_moderator", nullable = false)
    private int isModerator;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "reg_time", nullable = false)
    private Date regTime;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "code")
    private String code;

    @Column(name = "photo")
    private String photo;

    public Role getRole(){
        return isModerator ==  1 ? Role.MODDERATOR : Role.USER;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();


    public Set<Post> getPosts() {
        return posts;
    }

    public boolean addPost(Post post) {
        post.setUser(this);
        return getPosts().add(post);
    }

    public void removePost(Post post) {
        getPosts().remove(post);
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostVotes> postVotes = new HashSet<>();


    public Set<PostVotes> getPostVotes() {
        return postVotes;
    }

    public boolean addPost(PostVotes postVote) {
        postVote.setUser(this);
        return getPostVotes().add(postVote);
    }

    public void removePostVote(PostVotes postVote) {
        getPostVotes().remove(postVote);
    }

    public void setPostVotes(Set<PostVotes> postVotes) {
        this.postVotes = postVotes;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostComment> postComments = new HashSet<>();


    public Set<PostComment> getPostComments() {
        return postComments;
    }

    public boolean addPostComment(PostComment postComment) {
        postComment.setUser(this);
        return getPostComments().add(postComment);
    }

    public void removePostComment(PostComment postComment) {
        getPostComments().remove(postComment);
    }

    public void setPostComments(Set<PostComment> postComments) {
        this.postComments = postComments;
    }

//


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(int isModerator) {
        this.isModerator = isModerator;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
