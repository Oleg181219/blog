package diplom.blog.service;

import diplom.blog.api.response.Response;
import diplom.blog.api.response.ResultResponse;
import diplom.blog.api.response.StatisticResponse;
import diplom.blog.model.Post;
import diplom.blog.repo.GlobalSettingsRepository;
import diplom.blog.repo.PostRepository;
import diplom.blog.repo.PostVotesRepository;
import diplom.blog.repo.UserRepository;
import diplom.blog.util.AuthCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Comparator;

@Service
public class StatisticsService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GlobalSettingsRepository settingsRepository;
    private final PostVotesRepository votesRepository;
    private final AuthCheck authCheck;


    @Autowired
    public StatisticsService(PostRepository postRepository,
                             UserRepository userRepository,
                             GlobalSettingsRepository settingsRepository,
                             PostVotesRepository votesRepository,
                             AuthCheck authCheck) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
        this.votesRepository = votesRepository;
        this.authCheck = authCheck;
    }

    public ResponseEntity<Response> myStatistics() {


        if (authCheck.securityCheck()) {
            String userName = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            var myPosts = postRepository.findAllPostByUserEmail(userName);
            var votes = postRepository.findAlPostVotesByUserEmail(userName);
            var likeCount = 0;
            var disLikeCount = 0;
            for (diplom.blog.model.Post vote : votes) {
                likeCount = likeCount + (int) vote.getPostVotes().stream()
                        .filter(a -> a.getValue() == 1).count();
                disLikeCount = disLikeCount + (int) vote.getPostVotes().stream()
                        .filter(a -> a.getValue() == -1).count();
            }
            var viewCount = 0;
            for (int i = 0; i < myPosts.size(); i++) {
                viewCount = viewCount + myPosts.get(i).getViewCount();
            }
            Post post = new Post();
            if (!myPosts.isEmpty()) {
                post = myPosts
                        .stream()
                        .min(Comparator.comparing(Post::getTime)).get();
            }
            return ResponseEntity.ok(new StatisticResponse(myPosts.size(),
                    likeCount,
                    disLikeCount,
                    viewCount,
                    Math.toIntExact(post.getTime().getTime() / 1000)));
        }
        return ResponseEntity.badRequest().body(new ResultResponse());

    }

    public ResponseEntity<Response> allStatistics() {

        var settigs = settingsRepository.findById(3L).get().getValue();

        if (settigs.equalsIgnoreCase("YES")) {
            return createStatisticsResponse();
        }
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }

        if (userRepository.findByEmail(principal.getName()).getIsModerator() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }

        return createStatisticsResponse();
    }

    private StatisticResponse createStatisticsResponse() {

        var statisticResponse = new StatisticResponse();

        var postsCount = postRepository.findAll();
        statisticResponse.setPostsCount((long) postsCount.size());

        var likesCount = (long) votesRepository.findAllLikesAndDisLikes(1).size();
        statisticResponse.setLikesCount(likesCount);

        var disLikesCount = (long) votesRepository.findAllLikesAndDisLikes(-1).size();
        statisticResponse.setDislikesCount(disLikesCount);

        var countView = 0;
        for (Post view : postsCount) {
            countView = countView + view.getViewCount();
        }
        statisticResponse.setViewsCount((long) countView);

        Post oldestPost = postsCount.stream()
                .min(Comparator.comparing(Post::getTime)).get();
        statisticResponse.setFirstPublication(oldestPost.getTime().getTime() / 1000);
        return statisticResponse;
    }

}
