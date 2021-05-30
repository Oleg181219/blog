package diplom.blog.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyProfileRequest {

    private String name;

    private String email;

    private String password;

    private int removePhoto;

}
