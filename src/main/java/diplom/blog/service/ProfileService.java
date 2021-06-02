package diplom.blog.service;

import diplom.blog.api.request.MyProfileRequest;
import diplom.blog.api.response.ErrorResponse;
import diplom.blog.api.response.Response;
import diplom.blog.api.response.ResultResponse;
import diplom.blog.model.User;
import diplom.blog.repo.UserRepository;
import diplom.blog.util.AuthCheck;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.awt.Image.SCALE_DEFAULT;

@Service
public class ProfileService {
    private final FileSystemStorageService fileSystemStorageService;
    private final UserRepository userRepository;
    private final AuthCheck authCheck;


    public ProfileService(FileSystemStorageService fileSystemStorageService
            , UserRepository userRepository, AuthCheck authCheck) {
        this.fileSystemStorageService = fileSystemStorageService;
        this.userRepository = userRepository;
        this.authCheck = authCheck;
    }


    public ResponseEntity<Response> profileMy(MultipartFile photo, MyProfileRequest myProfileRequest) throws IOException {

        if (authCheck.securityCheck()) {
            var user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            var checkError = checkMyProfileRequest(myProfileRequest, user);

            if (photo.getSize() > 5242880) {
                checkError.put("photo", "Фото слишком большое, нужно не более 5 Мб");
            } else {
                var bufferedImage = ImageIO.read(photo.getInputStream());
                var ant = bufferedImage.getScaledInstance(36, 36, SCALE_DEFAULT);
                var fileName = photo.getOriginalFilename();
                user.setPhoto(fileSystemStorageService.cloudStore(ant, fileName));
            }

            if (!checkError.isEmpty()) {
                return ResponseEntity.ok(new ErrorResponse(false, checkError));
            }


            user.setName(myProfileRequest.getName());
            user.setPassword(passwordEncoder().encode(myProfileRequest.getPassword()));
            user.setEmail(myProfileRequest.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(new ResultResponse(true));
        }
        return ResponseEntity.ok(new ResultResponse());
    }


    public ResponseEntity<Response> profileMyWithoutFoto(MyProfileRequest myProfileRequest) {

        if (authCheck.securityCheck()) {

            var user = userRepository.findByEmail(SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName());

            var checkError = checkMyProfileRequest(myProfileRequest, user);
            if (!checkError.isEmpty()) {
                return ResponseEntity.ok(new ErrorResponse(false, checkError));
            }
            if (myProfileRequest.getRemovePhoto() == 1) {
                user.setPhoto(null);
            }
            user.setName(myProfileRequest.getName());
            user.setPassword(passwordEncoder().encode(myProfileRequest.getPassword()));
            user.setEmail(myProfileRequest.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(new ResultResponse(true));
        }
        return ResponseEntity.ok(new ResultResponse());
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private HashMap<String, String> checkMyProfileRequest(MyProfileRequest myProfileRequest, User user) {

        var error = new HashMap<String, String>();
        if (myProfileRequest.getName() != null) {
            if (!myProfileRequest.getName().matches("([А-Яа-яA-Za-z0-9-_]+)")) {
                error.put("name", "Имя указано неверно. ");
            }
        }
        if (myProfileRequest.getPassword() != null) {
            if (myProfileRequest.getPassword().length() < 6) {
                error.put("password", "Пароль короче 6-ти символов");
            }
        }
        if (myProfileRequest.getEmail() != null) {
            List<User> userByEmail = userRepository.findAllUserByEmail(myProfileRequest.getEmail());
            if (!myProfileRequest.getEmail().equals(user.getEmail())) {
                if (!userByEmail.isEmpty()) {
                    error.put("email", "Этот e-mail уже зарегистрирован");
                }
            }
        }
        return error;
    }

}


