package diplom.blog.controller;

import diplom.blog.api.request.NewPostRequest;
import diplom.blog.api.request.PostVotesRequest;
import diplom.blog.api.response.AllPostResponse;
import diplom.blog.api.response.ErrorResponse;
import diplom.blog.api.response.ResultResponse;
import diplom.blog.model.DtoModel.PostByIdDTO;
import diplom.blog.service.PostService;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api")
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
    public AllPostResponse posts(@RequestParam("mode") String mode,
                                 @RequestParam("offset") int offset,
                                 @RequestParam("limit") int limit) throws NotFoundException {
        return postService.allPost(offset, limit, mode);
    }

    /**
     * Поиск постов
     */
    @GetMapping("/post/search")
    @ApiOperation(value = "Поиск постов")
    public AllPostResponse postsSearch(@RequestParam("offset") int offset,
                                       @RequestParam("limit") int limit,
                                       @RequestParam("query") String query) {
        return postService.postsSearch(offset, limit, query);
    }

    /**
     * Список постов за указанную дату
     */
    @GetMapping("/post/byDate")
    @ApiOperation(value = "Список постов за указанную дату")
    public AllPostResponse postSearchByDate(@RequestParam("offset") int offset,
                                            @RequestParam("limit") int limit,
                                            @RequestParam("date") String date) {
        return postService.findPostsByDate(offset, limit, date);
    }

    /**
     * Список постов по тэгу
     */
    @GetMapping("/post/byTag")
    @ApiOperation(value = "Список постов по тэгу")
    public AllPostResponse postSearchByTag(@RequestParam("offset") int offset,
                                           @RequestParam("limit") int limit,
                                           @RequestParam("tag") String tag) {
        return postService.findPostsByTag(offset, limit, tag);
    }

    /**
     * Получение поста
     */
    @GetMapping("/post/{id}")
    @ApiOperation(value = "Получение поста")
    public PostByIdDTO postSearchById(@PathVariable long id, Principal principal) {
        return postService.findPostById(id, principal);
    }

    /**
     * Добавление поста
     */
    @PostMapping("/post")
    @ApiOperation(value = "Добавление поста")
    public ResponseEntity<ErrorResponse> postNewPost(@RequestBody NewPostRequest postRequest,
                                                     Principal principal) {
        return postService.newPost(postRequest, principal);
    }

    /**
     * Редактирование поста
     */
    @PutMapping("/post/{id}")
    @ApiOperation(value = "Редактирование поста")
    public ResponseEntity<ErrorResponse> editPost(@RequestBody NewPostRequest postRequest,
                                                  Principal principal,
                                                  @PathVariable long id) {
        return postService.editPost(postRequest, principal, id);
    }

    /**
     * Список постов на модерацию
     */
    @GetMapping("/post/moderation")
    @ApiOperation(value = "Список постов на модерацию")
    public AllPostResponse moderation(@RequestParam("offset") int offset,
                                      @RequestParam("limit") int limit,
                                      @RequestParam("status") String status,
                                      Principal principal) {
        return postService.moderation(offset, limit, status, principal);
    }

    /**
     * Список моих постов
     */
    @GetMapping("/post/my")
    @ApiOperation(value = "Список моих постов")
    public AllPostResponse myPost(@RequestParam("offset") int offset,
                                  @RequestParam("limit") int limit,
                                  @RequestParam("status") String status,
                                  Principal principal) {
        return postService.myPost(offset, limit, status, principal);
    }

    /**
     * Лайк поста
     */
    @PostMapping("/post/like")
    @ApiOperation(value = "Лайк поста")
    public ResponseEntity<ResultResponse> like(@RequestBody PostVotesRequest postVotesRequest,
                                               Principal principal) {
        return postService.likeVotes(postVotesRequest, principal);
    }

    /**
     * Дизлайк поста
     */
    @PostMapping("/post/dislike")
    @ApiOperation(value = "Дизлайк поста")
    public ResponseEntity<ResultResponse> disLike(@RequestBody PostVotesRequest postVotesRequest,
                                                  Principal principal) {
        return postService.disLikeVotes(postVotesRequest, principal);
    }


    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }
}

