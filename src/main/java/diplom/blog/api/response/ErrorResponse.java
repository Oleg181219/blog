package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@AllArgsConstructor
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private boolean result;

    private Map<String, String> errors;

    public ErrorResponse(boolean result) {
        this.result = result;
    }
}
