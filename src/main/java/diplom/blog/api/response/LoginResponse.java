package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class LoginResponse  implements Response{

    private boolean result;

    @JsonProperty("user")
    private UserLoginResponse userLoginResponse;

}
