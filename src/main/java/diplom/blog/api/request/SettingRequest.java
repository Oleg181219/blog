package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingRequest {

    @JsonProperty("MULTIUSER_MODE")
    private Boolean multiuserMode;

    @JsonProperty("POST_PREMODERATION")
    private Boolean postPremoderation;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    private Boolean statisticsIsPublic;

    public Boolean getMultiuserMode() {
        return multiuserMode;
    }

    public void setMultiuserMode(Boolean multiuserMode) {
        this.multiuserMode = multiuserMode;
    }

    public Boolean getPostPremoderation() {
        return postPremoderation;
    }

    public void setPostPremoderation(Boolean postPremoderation) {
        this.postPremoderation = postPremoderation;
    }

    public Boolean getStatisticsIsPublic() {
        return statisticsIsPublic;
    }

    public void setStatisticsIsPublic(Boolean statisticsIsPublic) {
        this.statisticsIsPublic = statisticsIsPublic;
    }
}
