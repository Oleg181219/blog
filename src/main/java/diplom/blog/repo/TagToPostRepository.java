package diplom.blog.repo;
import diplom.blog.model.TagToPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TagToPostRepository extends JpaRepository<TagToPost, Integer> {
    List<TagToPost> findAll();
}
