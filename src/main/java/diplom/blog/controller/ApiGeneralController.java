package diplom.blog.controller;

import diplom.blog.api.request.CommentRequest;
import diplom.blog.api.request.ModerationRequest;
import diplom.blog.api.request.MyProfileRequest;
import diplom.blog.api.request.SettingRequest;
import diplom.blog.api.response.*;
import diplom.blog.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final PostService postService;
    private final TagService tagService;
    private final StatisticsService statisticsService;
    private final FileSystemStorageService storageService;
    private final ProfileService profileService;

    public ApiGeneralController(InitResponse initResponse
            , SettingsService settingsService
            , PostService postService
            , TagService tagService
            , StatisticsService statisticsService
            , FileSystemStorageService storageService
            , ProfileService profileService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.postService = postService;
        this.tagService = tagService;
        this.statisticsService = statisticsService;
        this.storageService = storageService;
        this.profileService = profileService;
    }

    @GetMapping("/settings")
    public SettingsResponse settings() {
        return settingsService.getGlobalSettings();

    }

    @PutMapping("/settings")
    public SettingsResponse setSettings(@RequestBody SettingRequest settingRequest
            , Principal principal) {
        return settingsService.setGlobalSettings(settingRequest, principal);

    }

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/tag")
    public TagResponse tag(@RequestParam(required = false) String query) {
        return tagService.getTags(query);
    }

    @GetMapping("/calendar")
    public CalendarResponse calendar() {
        return postService.calendar();
    }

    @PostMapping("/moderation")
    public ResponseEntity<ModerationResponse> moderation(@RequestBody ModerationRequest moderationRequest
            , Principal principal) {
        return postService.moderation(moderationRequest, principal);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody CommentRequest commentRequest, Principal principal) {
        return postService.comment(commentRequest, principal);
    }

    @GetMapping("/statistics/my")
    public StatisticResponse myStatistic(Principal principal) {
        return statisticsService.myStatistics(principal);
    }

    @GetMapping("/statistics/all")
    public StatisticResponse allStatistic(Principal principal) {
        return statisticsService.allStatistics(principal);
    }

    @PostMapping(value = "/image")
    public ResponseEntity<?> uploadImage(HttpServletRequest request,
                                         @RequestParam("image") MultipartFile image,
                                         Principal principal) throws IOException {

        return ResponseEntity.ok(storageService.store(request, image, principal));
    }

    @PostMapping(value = "/profile/my",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileWithPhoto(@RequestParam(value = "photo") MultipartFile photo,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "email", required = false) String email,
                                                    @RequestParam(value = "password", required = false) String password,
                                                    Principal principal) throws IOException {
        return profileService.profileMy(photo, name, email, password, principal);
    }


    @PostMapping(value = "/profile/my",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileWithOutPhoto(@RequestBody MyProfileRequest myProfileRequest,
                                                       Principal principal) {
        return profileService.profileMyWithoutFoto(myProfileRequest, principal);
    }
}
