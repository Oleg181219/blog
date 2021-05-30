package diplom.blog.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRestoreRequest {

    private String email;

}
