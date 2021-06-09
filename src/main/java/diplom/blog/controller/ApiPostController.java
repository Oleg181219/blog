package diplom.blog.controller;

import diplom.blog.api.request.NewPostRequest;
import diplom.blog.api.request.PostVotesRequest;
import diplom.blog.api.response.Response;
import diplom.blog.service.PostService;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Список постов
     */
    @GetMapping("/post")
    @ApiOperation(value = "Список постов")
    public ResponseEntity<Response> posts(@RequestParam("mode") String mode,
                                          @RequestParam("offset") int offset,
                                          @RequestParam("limit") int limit) throws NotFoundException {
        return postService.allPost(offset, limit, mode);
    }

    /**
     * Поиск постов
     */
    @GetMapping("/post/search")
    @ApiOperation(value = "Поиск постов")
    public ResponseEntity<Response> postsSearch(@RequestParam("offset") int offset,
                                                @RequestParam("limit") int limit,
                                                @RequestParam("query") String query) {
        return postService.postsSearch(offset, limit, query);
    }

    /**
     * Список постов за указанную дату
     */
    @GetMapping("/post/byDate")
    @ApiOperation(value = "Список постов за указанную дату")
    public ResponseEntity<Response> postSearchByDate(@RequestParam("offset") int offset,
                                                     @RequestParam("limit") int limit,
                                                     @RequestParam("date") String date) {
        return postService.findPostsByDate(offset, limit, date);
    }

    /**
     * Список постов по тэгу
     */
    @GetMapping("/post/byTag")
    @ApiOperation(value = "Список постов по тэгу")
    public ResponseEntity<Response> postSearchByTag(@RequestParam("offset") int offset,
                                                    @RequestParam("limit") int limit,
                                                    @RequestParam("tag") String tag) {
        return postService.findPostsByTag(offset, limit, tag);
    }

    /**
     * Получение поста
     */
    @GetMapping("/post/{id}")
    @ApiOperation(value = "Получение поста")
    public ResponseEntity<Response> postSearchById(@PathVariable long id) {
        return postService.findPostById(id);
    }

    /**
     * Добавление поста
     */
    @PostMapping("/post")
    @ApiOperation(value = "Добавление поста")
    public ResponseEntity<Response> postNewPost(@Valid @RequestBody NewPostRequest postRequest, Errors errors) {
        return postService.newPost(postRequest, errors);
    }

    /**
     * Редактирование поста
     */
    @PutMapping("/post/{id}")
    @ApiOperation(value = "Редактирование поста")
    public ResponseEntity<Response> editPost(@Valid @RequestBody NewPostRequest postRequest,
                                             @PathVariable long id,
                                             Errors errors) {
        return postService.editPost(postRequest, id, errors);
    }

    /**
     * Список постов на модерацию
     */
    @GetMapping("/post/moderation")
    @ApiOperation(value = "Список постов на модерацию")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> moderation(@RequestParam("offset") int offset,
                                               @RequestParam("limit") int limit,
                                               @RequestParam("status") String status) {
        return postService.moderation(offset, limit, status);
    }

    /**
     * Список моих постов
     */
    @GetMapping("/post/my")
    @ApiOperation(value = "Список моих постов")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> myPost(@RequestParam("offset") int offset,
                                           @RequestParam("limit") int limit,
                                           @RequestParam("status") String status) {
        return postService.myPost(offset, limit, status);
    }

    /**
     * Лайк поста
     */
    @PostMapping("/post/like")
    @ApiOperation(value = "Лайк поста")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> like(@RequestBody PostVotesRequest postVotesRequest) {
        return postService.likeVotes(postVotesRequest);
    }

    /**
     * Дизлайк поста
     */
    @PostMapping("/post/dislike")
    @ApiOperation(value = "Дизлайк поста")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> disLike(@RequestBody PostVotesRequest postVotesRequest) {
        return postService.disLikeVotes(postVotesRequest);
    }


    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }
}

