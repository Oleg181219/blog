package diplom.blog.repo;

import diplom.blog.model.Enum.ModerationStatus;
import diplom.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pvl ON (p.id = pc.post.id and pvl.value = 1) " +
            "WHERE (p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_TIME)" +
            "GROUP BY p.id ORDER BY p.time desc ")
    Page<Post> findPostsOrderByTimeDesc(Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pvl ON p.id = pc.post.id  " +
            "WHERE (p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_TIME)" +
            "GROUP BY p.id ORDER BY size(p.postComments) desc ")
    Page<Post> findPostsOrderByPostComments(Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pvl ON (p.id = pc.post.id and pvl.value = 1) " +
            "WHERE (p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_TIME)" +
            "GROUP BY p.id ORDER BY p.time  ")
    Page<Post> findPostsOrderByTimeIncrease(Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pvl ON (p.id = pc.post.id and pvl.value = 1) " +
            "WHERE (p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_TIME)" +
            "GROUP BY p.id ORDER BY size(p.postVotes) desc ")
    Page<Post> findPostsOrderByLikeCount(Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_TIME"
    )
    List<Post> getCountPosts();

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pvl ON (p.id = pc.post.id and pvl.value = 1) " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIME " +
            "AND (p.text LIKE  %:text% OR p.title LIKE %:text% )" +
            "GROUP BY p.id  ")
    Page<Post> findAllText(Pageable page, @Param("text") String text);

    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE (p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_TIME)" +
            "GROUP BY p.id")
    List<Post> findAllByCalendar();

    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND DATE_FORMAT(p.time, '%Y-%m-%d') = str(:date_requested)")
    Page<Post> findPostsByDate(Pageable page, @Param("date_requested") String dateRequested);

    @Query("select p from Post p " +
            "LEFT JOIN TagToPost tp ON p.id = tp.postId " +
            "LEFT join Tag t ON tp.tagId = t.id " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIME " +
            "AND t.name LIKE %:tag%")
    Page<Post> findPostByTag(Pageable page
            , @Param("tag") String tag);

    @Query("select p " +
            "from Post p " +
            "LEFT JOIN TagToPost tp ON tp.postId = p.id " +
            "LEFT JOIN Tag t ON t.id = tp.tagId " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "WHERE (p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_TIME) " +
            "AND p.id = :id")
    Post findById(long id);


    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pvl ON (p.id = pc.post.id and pvl.value = 1) " +
            "WHERE (p.moderationStatus = 'NEW')")
    List<Post> findAllByModerationStatus();

    @Query("SELECT p " +
            "FROM Post p " +
            "left join User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pv ON p.id = pv.post.id " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = :status " +
            "AND p.moderatorId = :id " +
            "GROUP BY p.id ORDER BY p.time desc ")
    Page<Post> findAllPostsByModerationStatusAndModeratorId(Pageable pageable, @Param("status") Enum<ModerationStatus> status, @Param("id") int id);

    @Query("SELECT p " +
            "FROM Post p " +
            "left join User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pv ON p.id = pv.post.id " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = :status " +
            "GROUP BY p.id ORDER BY p.time desc ")
    Page<Post> findAllPostsByModerationStatus(Pageable pageable, @Param("status") Enum<ModerationStatus> status);



    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id=p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pv ON p.id = pv.post.id " +
            "WHERE p.isActive = :isActive " +
            "AND p.user.id = :id "+
            "GROUP BY p.id ORDER BY p.time desc ")
    Page<Post> findAllMyPostInactive(Pageable pageable, @Param("isActive") Integer isActive, @Param("id") int id);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id=p.user.id " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "LEFT JOIN PostVotes pv ON p.id = pv.post.id " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = :status " +
            "AND p.user.id = :id "+
            "GROUP BY p.id ORDER BY p.time desc ")
    Page<Post> findAllMyPostIsActive(Pageable pageable, @Param("status") Enum<ModerationStatus> status, @Param("id") int id);

    Post findByTimeAndTitleAndModerationStatusAndText(Date data
            , String title
            , Enum<ModerationStatus> moderationStatus
            , String text);


    @Query("select p " +
            "from Post p " +
            "LEFT JOIN TagToPost tp ON tp.postId = p.id " +
            "LEFT JOIN Tag t ON t.id = tp.tagId " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "WHERE p.id = :id")
    Post findByIdAuth(@Param("id") Long id);


    Post findPostById(@Param("id") Long id);


    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.user.email = :email")
    List<Post> findAllPostByUserEmail(@Param("email") String email);


    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN PostVotes pv ON pv.post.id = p.id " +
            "WHERE p.user.email = :email")
    List<Post> findAlPostVotesByUserEmail(@Param("email") String email);




}

