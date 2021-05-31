package diplom.blog.service;

import diplom.blog.api.request.SettingRequest;
import diplom.blog.api.response.Response;
import diplom.blog.api.response.SettingsResponse;
import diplom.blog.model.GlobalSettings;
import diplom.blog.repo.GlobalSettingsRepository;
import diplom.blog.repo.UserRepository;
import diplom.blog.util.AuthCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SettingsService {
    private final GlobalSettingsRepository settingsRepository;
    private final UserRepository userRepository;
    private final AuthCheck authCheck;


    @Autowired
    public SettingsService(GlobalSettingsRepository settingsRepository,
                           UserRepository userRepository,
                           AuthCheck authCheck) {
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
        this.authCheck = authCheck;
    }

    public ResponseEntity<Response> getGlobalSettings() {
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
        return ResponseEntity.ok(settingsResponse);
    }

    public ResponseEntity<Response> setGlobalSettings(SettingRequest settingRequest) {

       if( authCheck.securityCheck()) {
           if (userRepository.findByEmail(SecurityContextHolder
                   .getContext()
                   .getAuthentication()
                   .getName())
                   .getIsModerator() != 1) {
               throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
           }
           var newMultiUser = settingsRepository.getGlobalSettingsByCode("MULTIUSER_MODE");
           newMultiUser.setValue(Boolean.TRUE.equals(settingRequest.getMultiuserMode()) ? "YES" : "NO");
           settingsRepository.save(newMultiUser);

           var newPostModer = settingsRepository.getGlobalSettingsByCode("POST_PREMODERATION");
           newPostModer.setValue(Boolean.TRUE.equals(settingRequest.getPostPremoderation()) ? "YES" : "NO");
           settingsRepository.save(newPostModer);

           var newStatisticsIsPublic = settingsRepository.getGlobalSettingsByCode("STATISTICS_IS_PUBLIC");
           newStatisticsIsPublic.setValue(Boolean.TRUE.equals(settingRequest.getStatisticsIsPublic()) ? "YES" : "NO");
           settingsRepository.save(newStatisticsIsPublic);

           return getGlobalSettings();
       }
        return ResponseEntity.badRequest().body(new SettingsResponse());
    }
}
