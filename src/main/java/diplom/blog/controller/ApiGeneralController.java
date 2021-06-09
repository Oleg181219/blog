package diplom.blog.controller;

import diplom.blog.api.request.CommentRequest;
import diplom.blog.api.request.ModerationRequest;
import diplom.blog.api.request.MyProfileRequest;
import diplom.blog.api.request.SettingRequest;
import diplom.blog.api.response.InitResponse;
import diplom.blog.api.response.Response;
import diplom.blog.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
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
    public ResponseEntity<Response> settings() {
        return settingsService.getGlobalSettings();
    }

    /**
     * Сохранение настроек
     */
    @PutMapping("/settings")
    @ApiOperation(value = "Сохранение настроек")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> setSettings(@RequestBody SettingRequest settingRequest) {
        return settingsService.setGlobalSettings(settingRequest);
    }

    /**
     * Общие данные блога
     */
    @GetMapping("/init")
    @ApiOperation(value = "Общие данные блога")
    public Response init() {
        return initResponse;
    }

    /**
     * Получение списка тэгов
     */
    @GetMapping("/tag")
    @ApiOperation(value = "Получение списка тэгов")
    public ResponseEntity<Response> tag(@RequestParam(required = false) String query) {
        return tagService.getTags(query);
    }

    /**
     * Календарь (количества публикаций)
     */
    @GetMapping("/calendar")
    @ApiOperation(value = "Календарь (количества публикаций)")
    public ResponseEntity<Response> calendar() {
        return postService.calendar();
    }

    /**
     * Получение списков постов на модерацию
     */
    @PostMapping("/moderation")
    @ApiOperation(value = "Получение списков постов на модерацию")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> moderation(@Valid @RequestBody ModerationRequest moderationRequest, BindingResult errors) {
        System.out.println(errors.getAllErrors());
        return postService.moderationModer(moderationRequest, errors);
    }

    /**
     * Отправка комментария к посту
     */
    @PostMapping("/comment")
    @ApiOperation(value = "Отправка комментария к посту")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> comment(@Valid @RequestBody CommentRequest commentRequest, BindingResult errors) {
        return postService.comment(commentRequest, errors);
    }

    /**
     * Моя статистика
     */
    @GetMapping("/statistics/my")
    @ApiOperation(value = "Моя статистика")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> myStatistic() {
        return statisticsService.myStatistics();
    }

    /**
     * Статистика по всему блогу
     */
    @GetMapping("/statistics/all")
    @ApiOperation(value = "Статистика по всему блогу")
    public ResponseEntity<Response> allStatistic() {
        return statisticsService.allStatistics();
    }

    /**
     * Загрузка изображений
     */
    @PostMapping(value = "/image")
    @ApiOperation(value = "Загрузка изображений")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity uploadImage(HttpServletRequest request,
                                         @RequestParam("image") MultipartFile image)  {

        return ResponseEntity.ok(storageService.store(request, image));
    }

    /**
     * Редактирование моего профиля
     */
    @PostMapping(value = "/profile/my",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Редактирование моего профиля")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> updateProfile(@RequestParam(value = "photo") MultipartFile photo,
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "email", required = false) String email,
                                                  @RequestParam(value = "password", required = false) String password,
                                                  @RequestParam(value = "removePhoto", required = false) Integer removePhoto) throws IOException {

        return profileService.profileMyWithPhoto(photo, name, email, password, removePhoto);
    }

    @PostMapping(value = "/profile/my",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Редактирование моего профиля")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> updateProfileMy(@Valid @RequestBody MyProfileRequest myProfileRequest) throws IOException {

        return profileService.profileMy(myProfileRequest);
    }
}
