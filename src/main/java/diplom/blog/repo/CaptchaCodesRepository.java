package diplom.blog.repo;

import diplom.blog.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
@Transactional
public interface CaptchaCodesRepository extends JpaRepository<CaptchaCode, Integer> {

    @Modifying
    void deleteAllByTimeBefore(Date date);

    CaptchaCode findBySecretCode(String secretCode);

}
