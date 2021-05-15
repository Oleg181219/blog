package diplom.blog.repo;

import diplom.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tag, Long> {
    @Query(
            "SELECT t " +
                    "FROM Tag t " +
                    "LEFT JOIN TagToPost tp ON tp.tagId = t.id " +
                    "LEFT JOIN Post p ON p.id = tp.postId " +
                    "WHERE tp.id > 0"
    )
    List<Tag> findAll();

    List<Tag> findByName(String name);
}
