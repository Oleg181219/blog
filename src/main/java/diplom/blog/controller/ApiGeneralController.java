package diplom.blog.controller;

import diplom.blog.api.request.CommentRequest;
import diplom.blog.api.request.ModerationRequest;
import diplom.blog.api.request.MyProfileRequest;
import diplom.blog.api.request.SettingRequest;
import diplom.blog.api.response.*;
import diplom.blog.service.*;
import io.swagger.annotations.ApiOperation;
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

    /**
     * Получение настроек
     */
    @GetMapping("/settings")
    @ApiOperation(value = "Получение настроек")
    public SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    /**
     * Сохранение настроек
     */
    @PutMapping("/settings")
    @ApiOperation(value = "Сохранение настроек")
    public SettingsResponse setSettings(@RequestBody SettingRequest settingRequest
            , Principal principal) {
        return settingsService.setGlobalSettings(settingRequest, principal);
    }

    /**
     * Общие данные блога
     */
    @GetMapping("/init")
    @ApiOperation(value = "Общие данные блога")
    public InitResponse init() {
        return initResponse;
    }

    /**
     * Получение списка тэгов
     */
    @GetMapping("/tag")
    @ApiOperation(value = "Получение списка тэгов")
    public TagResponse tag(@RequestParam(required = false) String query) {
        return tagService.getTags(query);
    }

    /**
     * Календарь (количества публикаций)
     */
    @GetMapping("/calendar")
    @ApiOperation(value = "Календарь (количества публикаций)")
    public CalendarResponse calendar() {
        return postService.calendar();
    }

    /**
     * Получение списков постов на модерацию
     */
    @PostMapping("/moderation")
    @ApiOperation(value = "Получение списков постов на модерацию")
    public ResponseEntity<ResultResponse> moderation(@RequestBody ModerationRequest moderationRequest
            , Principal principal) {
        return postService.moderation(moderationRequest, principal);
    }

    /**
     * Отправка комментария к посту
     */
    @PostMapping("/comment")
    @ApiOperation(value = "Отправка комментария к посту")
    public ResponseEntity<?> comment(@RequestBody CommentRequest commentRequest, Principal principal) {
        return postService.comment(commentRequest, principal);
    }

    /**
     * Моя статистика
     */
    @GetMapping("/statistics/my")
    @ApiOperation(value = "Моя статистика")
    public StatisticResponse myStatistic(Principal principal) {
        return statisticsService.myStatistics(principal);
    }

    /**
     * Статистика по всему блогу
     */
    @GetMapping("/statistics/all")
    @ApiOperation(value = "Статистика по всему блогу")
    public StatisticResponse allStatistic(Principal principal) {
        return statisticsService.allStatistics(principal);
    }

    /**
     * Загрузка изображений
     */
    @PostMapping(value = "/image")
    @ApiOperation(value = "Загрузка изображений")
    public ResponseEntity<?> uploadImage(HttpServletRequest request,
                                         @RequestParam("image") MultipartFile image,
                                         Principal principal)  {
        return ResponseEntity.ok(storageService.store(request, image, principal));
    }

    /**
     * Редактирование моего профиля
     */
    @PostMapping(value = "/profile/my",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Редактирование моего профиля")
    public ResponseEntity<?> updateProfileWithPhoto(@RequestParam(value = "photo") MultipartFile photo,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "email", required = false) String email,
                                                    @RequestParam(value = "password", required = false) String password,
                                                    Principal principal) throws IOException {
        return profileService.profileMy(photo, name, email, password, principal);
    }

    /**
     * Редактирование моего профиля
     */
    @PostMapping(value = "/profile/my",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Редактирование моего профиля")
    public ResponseEntity<?> updateProfileWithOutPhoto(@RequestBody MyProfileRequest myProfileRequest,
                                                       Principal principal) {
        return profileService.profileMyWithoutFoto(myProfileRequest, principal);
    }
}
