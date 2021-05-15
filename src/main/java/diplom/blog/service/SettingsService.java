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


        if (gs.get(0).getValue().equals("YES")) {
            settingsResponse.setMultyuserMode(true);
        } else if (gs.get(0).getValue().equals("NO")) {
            settingsResponse.setMultyuserMode(false);
        }
        if (gs.get(1).getValue().equals("YES")) {
            settingsResponse.setPostPremoderation(true);
        } else if (gs.get(1).getValue().equals("NO")) {
            settingsResponse.setPostPremoderation(false);
        }
        if (gs.get(2).getValue().equals("YES")) {
            settingsResponse.setStatisticsIsPublic(true);
        } else if (gs.get(2).getValue().equals("NO")) {
            settingsResponse.setStatisticsIsPublic(false);
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
