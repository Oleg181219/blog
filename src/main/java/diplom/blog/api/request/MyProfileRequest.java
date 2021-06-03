package diplom.blog.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyProfileRequest {

    private String name;

    private String email;

    private String password;

    private Integer removePhoto;

    private MultipartFile photo;

}
