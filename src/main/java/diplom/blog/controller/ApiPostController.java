package diplom.blog.controller;

import diplom.blog.api.request.NewPostRequest;
import diplom.blog.api.request.PostVotesRequest;
import diplom.blog.api.response.AllPostResponse;
import diplom.blog.api.response.ErrorResponse;
import diplom.blog.api.response.ResultResponse;
import diplom.blog.model.DtoModel.PostByIdDTO;
import diplom.blog.service.PostService;
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


    @GetMapping("/post")
    public AllPostResponse posts(@RequestParam("mode") String mode,
                                  @RequestParam("offset") int offset,
                                  @RequestParam("limit") int limit) {
        return postService.allPost(offset, limit, mode);
    }

    @GetMapping("/post/search")
    public AllPostResponse postsSearch(@RequestParam("offset") int offset,
                                        @RequestParam("limit") int limit,
                                        @RequestParam("query") String query) {
        return postService.postsSearch(offset, limit, query);
    }

    @GetMapping("/post/byDate")
    public AllPostResponse postSearchByDate(@RequestParam("offset") int offset,
                                             @RequestParam("limit") int limit,
                                             @RequestParam("date") String date) {
        return postService.findPostsByDate(offset, limit, date);
    }

    @GetMapping("/post/byTag")
    public AllPostResponse postSearchByTag(@RequestParam("offset") int offset,
                                            @RequestParam("limit") int limit,
                                            @RequestParam("tag") String tag) {
        return postService.findPostsByTag(offset, limit, tag);
    }

    @GetMapping("/post/{id}")
    public PostByIdDTO postSearchById(@PathVariable long id,
                                       Principal principal) {

        return postService.findPostById(id, principal);
    }

    @PostMapping("/post")
    public ResponseEntity<ErrorResponse> postNewPost(@RequestBody NewPostRequest postRequest
            , Principal principal) {
        return postService.newPost(postRequest, principal);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<ErrorResponse> editPost(@RequestBody NewPostRequest postRequest
            , Principal principal
            , @PathVariable long id) {
        return postService.editPost(postRequest, principal, id);
    }

    @GetMapping("/post/moderation")
    public AllPostResponse moderation(@RequestParam("offset") int offset,
                                       @RequestParam("limit") int limit,
                                       @RequestParam("status") String status,
                                       Principal principal) {
        return postService.moderation(offset, limit, status, principal);
    }

    @GetMapping("/post/my")
    public AllPostResponse myPost(@RequestParam("offset") int offset,
                                   @RequestParam("limit") int limit,
                                   @RequestParam("status") String status,
                                   Principal principal) {
        return postService.myPost(offset, limit, status, principal);
    }

    @PostMapping("/post/like")
    public ResponseEntity<ResultResponse> like (@RequestBody PostVotesRequest postVotesRequest
            , Principal principal){
        return postService.likeVotes(postVotesRequest, principal);
    }

    @PostMapping("/post/dislike")
    public ResponseEntity<ResultResponse> disLike (@RequestBody PostVotesRequest postVotesRequest
            , Principal principal){
        return postService.disLikeVotes(postVotesRequest, principal);
    }


    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }
}

