package diplom.blog.repo;

import diplom.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findAll();

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.email = :email ")
    User findByEmail(@Param("email") String email);

    User findByCode(String code);

    User findById(int id);


    List<User> findAllUserByEmail(String email);
}
