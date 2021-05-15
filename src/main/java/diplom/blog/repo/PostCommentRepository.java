package diplom.blog.repo;

import diplom.blog.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findAll();


    @Query("SELECT pc " +
            "FROM PostComment pc " +
            "WHERE pc.text = :text")
    PostComment findByText(@Param("text") String text);
}
