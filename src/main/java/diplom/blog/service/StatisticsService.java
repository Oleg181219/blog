package diplom.blog.service;

import diplom.blog.api.response.StatisticResponse;
import diplom.blog.model.Post;
import diplom.blog.repo.GlobalSettingsRepository;
import diplom.blog.repo.PostRepository;
import diplom.blog.repo.PostVotesRepository;
import diplom.blog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Comparator;

@Component
public class StatisticsService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GlobalSettingsRepository settingsRepository;
    private final PostVotesRepository votesRepository;


    @Autowired
    public StatisticsService(PostRepository postRepository
            , UserRepository userRepository
            , GlobalSettingsRepository settingsRepository
            , PostVotesRepository votesRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
        this.votesRepository = votesRepository;
    }

    public StatisticResponse myStatistics(Principal principal) {

        var statisticResponse = new StatisticResponse();

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }

        var myPosts = postRepository.findAllPostByUserEmail(principal.getName());
        statisticResponse.setPostsCount((long) myPosts.size());
        var votes = postRepository.findAlPostVotesByUserEmail(principal.getName());
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

        statisticResponse.setDislikesCount((long) disLikeCount);
        statisticResponse.setLikesCount((long) likeCount);
        statisticResponse.setViewsCount((long) viewCount);
        statisticResponse.setFirstPublication(post.getTime().getTime() / 1000);


        return statisticResponse;
    }

    public StatisticResponse allStatistics(Principal principal) {

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
