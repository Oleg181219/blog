package diplom.blog.service;

import diplom.blog.api.request.SettingRequest;
import diplom.blog.api.response.SettingsResponse;
import diplom.blog.model.GlobalSettings;
import diplom.blog.repo.GlobalSettingsRepository;
import diplom.blog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Service
public class SettingsService {
    private final GlobalSettingsRepository settingsRepository;
    private final UserRepository userRepository;


    @Autowired
    public SettingsService(GlobalSettingsRepository settingsRepository
            , UserRepository userRepository) {
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
    }

    public SettingsResponse getGlobalSettings() {
        var settingsResponse = new SettingsResponse();

        List<GlobalSettings> gs = settingsRepository.findAll();

        for (GlobalSettings settings : gs) {

            switch (settings.getCode()) {
                case ("MULTIUSER_MODE"):
                    settingsResponse.setMultyuserMode(settings.getValue().equals("YES"));
                    break;
                case ("POST_PREMODERATION"):
                    settingsResponse.setPostPremoderation(settings.getValue().equals("YES"));
                    break;
                case ("STATISTICS_IS_PUBLIC"):
                    settingsResponse.setStatisticsIsPublic(settings.getValue().equals("YES"));
                    break;
                default:
                    break;
            }

        }

        return settingsResponse;
    }

    public SettingsResponse setGlobalSettings(SettingRequest settingRequest, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        if (userRepository.findByEmail(principal.getName()).getIsModerator() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        var newMultiUser = settingsRepository.getGlobalSettingsById(1L);
        newMultiUser.setValue(Boolean.TRUE.equals(settingRequest.getMultiuserMode()) ? "YES" : "NO");
        settingsRepository.save(newMultiUser);

        var newPostModer = settingsRepository.getGlobalSettingsById(2L);
        newPostModer.setValue(Boolean.TRUE.equals(settingRequest.getPostPremoderation()) ? "YES" : "NO");
        settingsRepository.save(newPostModer);

        var newStatisticsIsPublic = settingsRepository.getGlobalSettingsById(3L);
        newStatisticsIsPublic.setValue(Boolean.TRUE.equals(settingRequest.getStatisticsIsPublic()) ? "YES" : "NO");
        settingsRepository.save(newStatisticsIsPublic);

        return getGlobalSettings();
    }
}
