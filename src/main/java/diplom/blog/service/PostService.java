package diplom.blog.service;

import diplom.blog.api.request.CommentRequest;
import diplom.blog.api.request.ModerationRequest;
import diplom.blog.api.request.NewPostRequest;
import diplom.blog.api.request.PostVotesRequest;
import diplom.blog.api.response.*;
import diplom.blog.model.*;
import diplom.blog.model.DtoModel.*;
import diplom.blog.model.Enum.ModerationStatus;
import diplom.blog.repo.*;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagToPostRepository tagToPostRepository;
    private final TagsRepository tagsRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostVotesRepository postVotesRepository;
    private final String UNAUTHORIZED = "UNAUTHORIZED";


    @Autowired
    public PostService(PostRepository postRepository
            , UserRepository userRepository
            , TagToPostRepository tagToPostRepository
            , TagsRepository tagsRepository
            , PostCommentRepository postCommentRepository
            , PostVotesRepository postVotesRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagToPostRepository = tagToPostRepository;
        this.tagsRepository = tagsRepository;
        this.postCommentRepository = postCommentRepository;
        this.postVotesRepository = postVotesRepository;
    }

    SimpleDateFormat formaterPostDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formaterYear = new SimpleDateFormat("yyyy");

    public AllPostResponse allPost(int offset, int limit, String mode) {
        ArrayList<PostDTO> postsList = new ArrayList<>();
        Page<Post> allPosts;
        var allPostResponse = new AllPostResponse();

        int countPosts;
        switch (mode) {
            case "popular":
                allPosts = postRepository.findPostsOrderByPostComments(PageRequest.of(offset / limit, limit));
                countPosts = Math.toIntExact(allPosts.getTotalElements());
                for (Post allPost : allPosts) {
                    PostDTO newRespPost = createNewResponsePosts(allPost);
                    if (!postsList.contains(newRespPost)) {
                        postsList.add(newRespPost);
                    }
                }
                break;
            case "best":
                allPosts = postRepository.findPostsOrderByLikeCount(PageRequest.of(offset / limit, limit));
                countPosts = Math.toIntExact(allPosts.getTotalElements());
                for (Post allPost : allPosts) {

                    PostDTO newRespPost = createNewResponsePosts(allPost);
                    if (!postsList.contains(newRespPost)) {
                        postsList.add(newRespPost);
                    }
                }
                break;
            case "early":
                allPosts = postRepository.findPostsOrderByTimeIncrease(PageRequest.of(offset / limit, limit));
                countPosts = Math.toIntExact(allPosts.getTotalElements());
                for (Post allPost : allPosts) {

                    PostDTO newRespPost = createNewResponsePosts(allPost);
                    if (!postsList.contains(newRespPost)) {
                        postsList.add(newRespPost);
                    }
                }
                break;
            default:
                allPosts = postRepository.findPostsOrderByTimeDesc(PageRequest.of(offset / limit, limit));
                countPosts = Math.toIntExact(allPosts.getTotalElements());
                for (Post allPost : allPosts) {
                    PostDTO newRespPost = createNewResponsePosts(allPost);
                    if (!postsList.contains(newRespPost)) {
                        postsList.add(newRespPost);
                    }
                }
                break;
        }
        allPostResponse.setCount(countPosts);
        allPostResponse.setPosts(postsList);
        return allPostResponse;
    }

    //=================================================================================
    public AllPostResponse postsSearch(int offset, int limit, String query) {
        ArrayList<PostDTO> postsList = new ArrayList<>();
        var allPostResponse = new AllPostResponse();
        int countPosts;

        Page<Post> allPosts = postRepository.findAllText(PageRequest.of(offset / limit, limit), query);
        countPosts = Math.toIntExact(allPosts.getTotalElements());

        for (Post post : allPosts) {
            postsList.add(createNewResponsePosts(post));
        }

        allPostResponse.setCount(countPosts);
        allPostResponse.setPosts(postsList);
        return allPostResponse;
    }
//=================================================================================

    public CalendarResponse calendar() {
        var calendarResponse = new CalendarResponse();
        ArrayList<String> years = new ArrayList<>();
        HashMap<String, Integer> posts = new HashMap<>();
        List<Post> allPosts = postRepository.findAllByCalendar();

        for (Post post : allPosts) {
            if ((post.getIsActive() == 1)
                    && (post.getModerationStatus().toString().equals("ACCEPTED"))
                    && (post.getTime().getTime() <= System.currentTimeMillis())) {
                var date = post.getTime();
                String year = formaterYear.format(date);
                String datePost = formaterPostDate.format(date);
                if (!years.contains(year)) {
                    years.add(year);
                }
                if (!posts.containsKey(datePost)) {
                    posts.put(datePost, 1);
                } else {
                    posts.replace(datePost, (posts.get(datePost) + 1));
                }
            }
        }

        calendarResponse.setYears(years);
        calendarResponse.setPosts(posts);
        return calendarResponse;
    }

    //=================================================================================
    public AllPostResponse findPostsByDate(int offset, int limit, String date) {
        var allPostResponse = new AllPostResponse();
        ArrayList<PostDTO> postsList = new ArrayList<>();
        Page<Post> allPosts = postRepository.findPostsByDate(PageRequest.of(offset / limit, limit), date);

        for (Post allPost : allPosts) {
            PostDTO newRespPost = createNewResponsePosts(allPost);
            var dateFromList = new Date(allPost.getTime().getTime());
            var dateFromListString = formaterPostDate.format(dateFromList);

            if (!postsList.contains(newRespPost) && (dateFromListString.equals(date))) {
                postsList.add(newRespPost);
            }
        }
        if (postsList.isEmpty()) {
            allPostResponse.setCount(0);
            allPostResponse.setPosts(postsList);
            return allPostResponse;
        }
        allPostResponse.setCount(postsList.size());
        allPostResponse.setPosts(postsList);
        return allPostResponse;
    }

    //=================================================================================
    public AllPostResponse findPostsByTag(int offset, int limit, String tag) {
        ArrayList<PostDTO> postsList = new ArrayList<>();
        var allPostResponse = new AllPostResponse();
        int countPosts;

        Page<Post> allPosts = postRepository.findPostByTag(PageRequest.of(offset / limit, limit), tag);
        countPosts = Math.toIntExact(allPosts.getTotalElements());

        for (Post post : allPosts) {
            postsList.add(createNewResponsePosts(post));
        }
        allPostResponse.setCount(countPosts);
        allPostResponse.setPosts(postsList);
        return allPostResponse;
    }

    //=================================================================================
    public PostByIdDTO findPostById(long id, Principal principal) {
        var postByIdDTO = new PostByIdDTO();
        var userDTO = new UserDTO();
        ArrayList<String> tagPostByIdDTO = new ArrayList<>();
        ArrayList<CommentDTO> comments = new ArrayList<>();
        Post post;
        if (principal == null) {
            post = postRepository.findById(id);
        } else {
            post = postRepository.findByIdAuth(id);
        }
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED);
        }
        postByIdDTO.setId(post.getId());// id
        postByIdDTO.setTimestamp(post.getTime().getTime() / 1000);//timestamp
        postByIdDTO.setActive(post.getIsActive() != 0);// isActive
        userDTO.setId((long) post.getUser().getId());//user id
        userDTO.setName(post.getUser().getName());//username
        postByIdDTO.setUser(userDTO);//user
        postByIdDTO.setTitle(post.getTitle());//title
        postByIdDTO.setText(post.getText());//text
        postByIdDTO.setLikeCount(post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());//likes
        postByIdDTO.setDislikeCount(post.getPostVotes().stream().filter(a -> a.getValue() != 1).count());//dislikes
        int viewCount = post.getViewCount();
        if (principal == null) {
            viewCount++;

        } else {
            if (!principal.getName().equals(post.getUser().getEmail()) &&
                    userRepository.findByEmail(principal.getName()).getIsModerator() != 1) {
                post.setViewCount(viewCount + 1);
                postRepository.save(post);
            }
        }
        postByIdDTO.setViewCount(viewCount);
        post.getPostComments().forEach(entry -> {//postComment start
            var userCommentDTO = new UserCommentDTO();
            var commentDTO = new CommentDTO();
            userCommentDTO.setId(entry.getUser().getId());
            userCommentDTO.setName(entry.getUser().getName());
            userCommentDTO.setPhoto(entry.getUser().getPhoto());
            commentDTO.setId(entry.getId());
            commentDTO.setTimestamp(entry.getTime().getTime() / 1000);
            commentDTO.setText(entry.getText());
            commentDTO.setUser(userCommentDTO);
            comments.add(commentDTO);
        });//postComment end

        comments.sort(Comparator.comparing(CommentDTO::getTimestamp).reversed());
        postByIdDTO.setComments(comments);//PostComment
        post.getTags().forEach(entry -> tagPostByIdDTO.add(entry.getName()));
        postByIdDTO.setTags(tagPostByIdDTO);//tags
        return postByIdDTO;
    }

    //=================================================================================
    public AllPostResponse moderation(int offset, int limit, String status, Principal principal) {
        ArrayList<PostDTO> postsList = new ArrayList<>();
        Page<Post> allPosts;
        var allPostResponse = new AllPostResponse();
        var moder = userRepository.findByEmail(principal.getName());
        var countPosts = 0;

        if (moder.getIsModerator() == 1) {

            switch (status) {
                case "new":
                    allPosts = postRepository.findAllPostsByModerationStatus(PageRequest.of(offset / limit, limit)
                            , ModerationStatus.NEW);
                    countPosts = Math.toIntExact(allPosts.getTotalElements());
                    for (Post allPost : allPosts) {
                        PostDTO newRespPost = createNewResponsePosts(allPost);
                        if (!postsList.contains(newRespPost)) {
                            postsList.add(newRespPost);
                        }
                    }
                    break;
                case "declined":
                    allPosts = postRepository.findAllPostsByModerationStatusAndModeratorId(PageRequest.of(offset / limit, limit)
                            , ModerationStatus.DECLINED, moder.getId());
                    countPosts = Math.toIntExact(allPosts.getTotalElements());
                    for (Post allPost : allPosts) {
                        PostDTO newRespPost = createNewResponsePosts(allPost);
                        if (!postsList.contains(newRespPost)) {
                            postsList.add(newRespPost);
                        }
                    }
                    break;
                case "accepted":
                    allPosts = postRepository.findAllPostsByModerationStatusAndModeratorId(PageRequest.of(offset / limit, limit)
                            , ModerationStatus.ACCEPTED, moder.getId());
                    countPosts = Math.toIntExact(allPosts.getTotalElements());
                    for (Post allPost : allPosts) {
                        PostDTO newRespPost = createNewResponsePosts(allPost);
                        if (!postsList.contains(newRespPost)) {
                            postsList.add(newRespPost);
                        }
                    }
                    break;
                default:
                    break;
            }
            allPostResponse.setCount(countPosts);
            allPostResponse.setPosts(postsList);
        }
        return allPostResponse;
    }

    //=================================================================================
    public AllPostResponse myPost(int offset, int limit, String status, Principal principal) {
        ArrayList<PostDTO> postsList = new ArrayList<>();
        Page<Post> allPosts;
        var allPostResponse = new AllPostResponse();
        var user = userRepository.findByEmail(principal.getName());
        var countPosts = 0;

        switch (status) {
            case ("inactive"):
                allPosts = postRepository.findAllMyPostInactive(PageRequest.of(offset / limit, limit)
                        , 0, user.getId());
                countPosts = Math.toIntExact(allPosts.getTotalElements());
                for (Post allPost : allPosts) {
                    PostDTO newRespPost = createNewResponsePosts(allPost);
                    if (!postsList.contains(newRespPost)) {
                        postsList.add(newRespPost);
                    }
                }
                break;
            case ("pending"):
                allPosts = postRepository.findAllMyPostIsActive(PageRequest.of(offset / limit, limit)
                        , ModerationStatus.NEW, user.getId());
                countPosts = Math.toIntExact(allPosts.getTotalElements());
                for (Post allPost : allPosts) {
                    PostDTO newRespPost = createNewResponsePosts(allPost);
                    if (!postsList.contains(newRespPost)) {
                        postsList.add(newRespPost);
                    }
                }
                break;
            case ("declined"):
                allPosts = postRepository.findAllMyPostIsActive(PageRequest.of(offset / limit, limit)
                        , ModerationStatus.DECLINED, user.getId());
                countPosts = Math.toIntExact(allPosts.getTotalElements());
                for (Post allPost : allPosts) {
                    PostDTO newRespPost = createNewResponsePosts(allPost);
                    if (!postsList.contains(newRespPost)) {
                        postsList.add(newRespPost);
                    }
                }
                break;
            case ("published"):
                allPosts = postRepository.findAllMyPostIsActive(PageRequest.of(offset / limit, limit)
                        , ModerationStatus.ACCEPTED, user.getId());
                countPosts = Math.toIntExact(allPosts.getTotalElements());
                for (Post allPost : allPosts) {
                    PostDTO newRespPost = createNewResponsePosts(allPost);
                    if (!postsList.contains(newRespPost)) {
                        postsList.add(newRespPost);
                    }
                }
                break;
            default:
                break;
        }
        allPostResponse.setCount(countPosts);
        allPostResponse.setPosts(postsList);
        return allPostResponse;
    }
//=================================================================================

    public ResponseEntity<ErrorResponse> newPost(NewPostRequest postRequest, Principal principal) {


        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED);
        }
        ErrorResponse errorResponse;
        HashMap<String, String> errors;


        if (postRequest.getTitle().length() >= 3 &&
                Jsoup.parse(postRequest.getText()).text().length() >= 50) {
            errorResponse = new ErrorResponse();
            var newPost = new Post();
            newPost.setIsActive(postRequest.getActive());
            newPost.setModerationStatus(ModerationStatus.NEW);
            newPost.setModeratorId(0);
            var user = userRepository.findByEmail(principal.getName());
            newPost.setUser(user);
            newPost.setTime(new Date(postRequest.getTimestamp() * 1000));
            newPost.setTitle(postRequest.getTitle());
            newPost.setText(postRequest.getText());
            newPost.setViewCount(0);
            postRepository.save(newPost);

            Long idNewPost = postRepository.findByTimeAndTitleAndModerationStatusAndText(
                    new Date(postRequest.getTimestamp() * 1000)
                    , postRequest.getTitle()
                    , ModerationStatus.NEW
                    , postRequest.getText()).getId();

            for (String tag : postRequest.getTags()) {
                var newTag = new Tag();
                newTag.setName(tag);
                if (tagsRepository.findByName(tag).isEmpty()) {
                    tagsRepository.save(newTag);
                }
                var tagToPost = new TagToPost();
                tagToPost.setPostId(idNewPost);
                tagToPost.setTagId(tagsRepository.findByName(newTag.getName()).get(0).getId());
                tagToPostRepository.save(tagToPost);
            }

            errorResponse.setResult(true);
            return ResponseEntity.ok(errorResponse);

        }
//        if
        errorResponse = new ErrorResponse();
        errors = new HashMap<>();
        if (postRequest.getTitle().length() < 3) {
            errors.put("title", "Заголовок не установлен");
        }
        if (Jsoup.parse(postRequest.getText()).text().length() < 50) {
            errors.put("text", "Текст публикации слишком короткий");
        }
        errorResponse.setResult(false);
        errorResponse.setErrors(errors);
        return ResponseEntity.ok(errorResponse);
    }

    //=================================================================================
    public ResponseEntity<ErrorResponse> editPost(NewPostRequest postRequest, Principal principal, Long id) {
        var newPostResponse = new ErrorResponse();
        HashMap<String, String> errors = new HashMap<>();

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED);
        }

        if (postRequest.getTitle().length() >= 3 &&
                Jsoup.parse(postRequest.getText()).text().length() >= 50) {
            var editPost = postRepository.findByIdAuth(id);
            if (editPost == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND");
            }
            if (editPost.getUser().getEmail().equals(principal.getName())
                    || userRepository.findByEmail(principal.getName()).getIsModerator() == 1) {

                editPost.setTitle(postRequest.getTitle());
                editPost.setText(postRequest.getText());
                editPost.setTime(new Date(postRequest.getTimestamp() * 1000));
                editPost.setIsActive(postRequest.getActive());
                editPost.setModerationStatus(ModerationStatus.NEW);
                postRepository.save(editPost);

                for (String tag : postRequest.getTags()) {
                    var newTag = new Tag();
                    newTag.setName(tag);
                    if (tagsRepository.findByName(tag).isEmpty()) {
                        tagsRepository.save(newTag);
                    }
                    var tagToPost = new TagToPost();
                    tagToPost.setPostId(editPost.getId());
                    tagToPost.setTagId(tagsRepository.findByName(newTag.getName()).get(0).getId());
                    tagToPostRepository.save(tagToPost);
                }
            }
            newPostResponse.setResult(true);
            return ResponseEntity.ok(newPostResponse);
        }
        if (postRequest.getTitle().length() < 3) {
            errors.put("title", "Заголовок не установлен");
        }
        if (Jsoup.parse(postRequest.getText()).text().length() < 50) {
            errors.put("text", "Текст публикации слишком короткий");
        }
        newPostResponse.setResult(false);
        newPostResponse.setErrors(errors);
        return ResponseEntity.ok(newPostResponse);
    }

//=================================================================================

    public ResponseEntity<ModerationResponse> moderation(ModerationRequest moderationRequest
            , Principal principal) {
        var moderationResponse = new ModerationResponse();

        var a = new ArrayList();
        if(principal == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        var moderator = userRepository.findByEmail(principal.getName());
        if (moderator.getIsModerator() == 1) {
            var postForModeration = postRepository.findPostById(moderationRequest.getId());
            switch (moderationRequest.getDecision()) {
                case "accept":
                    postForModeration.setModerationStatus(ModerationStatus.ACCEPTED);
                    postForModeration.setModeratorId(moderator.getId());
                    postRepository.save(postForModeration);
                    break;
                case "decline":
                    postForModeration.setModerationStatus(ModerationStatus.DECLINED);
                    postForModeration.setModeratorId(moderator.getId());
                    postRepository.save(postForModeration);
                    break;
                default:
                    break;
            }
            moderationResponse.setRezult(true);
        }
        return ResponseEntity.ok(moderationResponse);
    }

//=================================================================================

    public ResponseEntity<?> comment(CommentRequest commentRequest, Principal principal) {

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED);
        }

        var successfullyCommentResponse = new SuccessfullyCommentResponse();
        var unsuccessfullyCommentResponse = new UnsuccessfullyCommentResponse();
        HashMap<String, String> errors = new HashMap<>();


        if (commentRequest.getParentId() != null) {
            if (postCommentRepository.findById(commentRequest.getParentId()).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong parent post id");
            }
        }
        if (postCommentRepository.findById(commentRequest.getPostId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong post id");
        }

        var text = Jsoup.parse(commentRequest.getText()).text();

        if (text.length() <= 10) {
            errors.put("text", "Текст комментария не задан или слишком короткий");
            unsuccessfullyCommentResponse.setErrors(errors);
            unsuccessfullyCommentResponse.setResult(false);
            return ResponseEntity.ok(unsuccessfullyCommentResponse);

        } else {
            var postComment = new PostComment();
            if (commentRequest.getParentId() != null) {
                postComment.setParentId(commentRequest.getParentId());
            } else postComment.setParentId(null);
            postComment.setPost(postRepository.findPostById(commentRequest.getPostId()));
            postComment.setText(commentRequest.getText());
            postComment.setTime(new Date());
            postComment.setUser(userRepository.findByEmail(principal.getName()));
            postCommentRepository.save(postComment);
            successfullyCommentResponse.setId(postCommentRepository.findByText(commentRequest.getText()).getId());
        }
        return ResponseEntity.ok(successfullyCommentResponse);
    }

//=================================================================================

    public ResponseEntity<ResultResponse> likeVotes(PostVotesRequest postVotesRequest, Principal principal) {
        var response = new ResultResponse();

        if (principal == null) {
            response.setResult(false);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(setVote(postVotesRequest, 1, principal));
    }

//=================================================================================

    public ResponseEntity<ResultResponse> disLikeVotes(PostVotesRequest postVotesRequest, Principal principal) {
        var response = new ResultResponse();

        if (principal == null) {
            response.setResult(false);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(setVote(postVotesRequest, -1, principal));
    }

//=================================================================================

    private ResultResponse setVote(PostVotesRequest postVotesRequest, Integer number, Principal principal) {
        var response = new ResultResponse();
        var postVotes = new PostVotes();
        var userID = userRepository.findByEmail(principal.getName());
        var postVote = postVotesRepository.findAllVotes(postVotesRequest.getPostId());
        var votes = postVote.stream().filter(a -> a.getUser() == userID).collect(Collectors.toList());
        if (votes.isEmpty()) {
            postVotes.setUser(userID);
            postVotes.setPost(postRepository.findPostById(postVotesRequest.getPostId()));
            postVotes.setTime(new Date());
            postVotes.setValue(number);
            postVotesRepository.save(postVotes);
            response.setResult(true);
        } else {
            var editPostVotes = postVotesRepository.getOne(postVote.get(0).getId());
            if (editPostVotes.getValue() == number) {
                response.setResult(false);
                return response;
            } else {
                editPostVotes.setValue(number);
                postVotesRepository.save(editPostVotes);
                response.setResult(true);
                return response;
            }
        }
        return response;
    }

//=================================================================================

    private PostDTO createNewResponsePosts(Post post) {
        var postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTimestamp(post.getTime().getTime() / 1000);
        var userDTO = new UserDTO();
        userDTO.setId((long) post.getUser().getId());
        userDTO.setName(post.getUser().getName());
        postDTO.setUser(userDTO);
        postDTO.setTitle(post.getTitle());
        String plainText = Jsoup.parse(post.getText()).text();
        if (plainText.length() > 150) {
            String temp = (plainText.substring(0, 150));
            postDTO.setAnnounce(temp.substring(0, temp.lastIndexOf(" ")) + "...");
        } else {
            postDTO.setAnnounce(plainText + "...");
        }

        postDTO.setLikeCount(post.getPostVotes().stream().filter(a -> a.getValue() == 1).count());
        postDTO.setDislikeCount(post.getPostVotes().stream().filter(a -> a.getValue() != 1).count());
        postDTO.setCommentCount(post.getPostComments().size());
        postDTO.setViewCount(post.getViewCount());
        return postDTO;
    }

}
