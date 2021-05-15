package diplom.blog.repo;

import diplom.blog.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostVotesRepository extends JpaRepository<PostVotes,Long> {
    List<PostVotes> findAll();


    @Query("SELECT pv " +
            "FROM PostVotes pv " +
            "WHERE pv.post.id = :id")
    List<PostVotes> findAllVotes(@Param("id") Long id);


    @Query("SELECT pv " +
            "FROM PostVotes pv " +
            "WHERE pv.value = :value")
    List<PostVotes> findAllLikesAndDisLikes(@Param("value") Integer value);
}
