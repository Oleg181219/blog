package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private Boolean result;

    private Map<String, String> errors;

    public AuthResponse(Boolean result, Map<String, String> errors) {
        this.result = result;
        this.errors = errors;
    }

    public AuthResponse() {
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
