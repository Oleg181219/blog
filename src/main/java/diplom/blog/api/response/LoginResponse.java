package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import diplom.blog.model.dtoModel.UserLoginDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoginResponse  implements Response{

    @JsonProperty("result")
    private boolean result;

    @JsonProperty("user")
    private UserLoginDTO userLoginDTO;

    public LoginResponse(boolean result) {
        this.result = result;
    }
}
