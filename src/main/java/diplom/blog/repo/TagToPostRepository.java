package diplom.blog.repo;
import diplom.blog.model.TagToPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagToPostRepository extends JpaRepository<TagToPost, Integer> {
    List<TagToPost> findAll();

}
