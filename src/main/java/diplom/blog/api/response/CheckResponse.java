package diplom.blog.api.response;

import org.springframework.stereotype.Component;

@Component
public class CheckResponse {

    private boolean result;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
