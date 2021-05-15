package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    private boolean result;

    @JsonProperty("user")
    private UserLoginResponse userLoginResponse;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public UserLoginResponse getUserLoginResponse() {
        return userLoginResponse;
    }

    public void setUserLoginResponse(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }
}
