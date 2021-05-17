package diplom.blog.repo;

import diplom.blog.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Long> {

    GlobalSettings getGlobalSettingsById(Long id);

    @Override
    List<GlobalSettings> findAll();
}
