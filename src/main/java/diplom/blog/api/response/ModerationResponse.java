package diplom.blog.api.response;

import org.springframework.stereotype.Component;

@Component
public class ModerationResponse {

    private boolean rezult;

    public boolean getRezult() {
        return rezult;
    }

    public void setRezult(boolean rezult) {
        this.rezult = rezult;
    }
}
