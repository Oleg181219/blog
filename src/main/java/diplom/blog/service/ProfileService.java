package diplom.blog.service;

import diplom.blog.api.request.MyProfileRequest;
import diplom.blog.api.response.ErrorResponse;
import diplom.blog.api.response.Response;
import diplom.blog.api.response.ResultResponse;
import diplom.blog.model.User;
import diplom.blog.repo.UserRepository;
import diplom.blog.util.AuthCheck;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
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


    public ResponseEntity<Response> profileMy(MyProfileRequest myProfileRequest) throws IOException {
        String name = myProfileRequest.getName();
        String email = myProfileRequest.getEmail();
        String password = myProfileRequest.getPassword();
        Integer removePhoto = myProfileRequest.getRemovePhoto();
        MultipartFile photo = myProfileRequest.getPhoto();
        var error = new HashMap<String, String>();
        User user = userRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        if (authCheck.securityCheck()) {
            if (name != null) {
                if (!name.matches("([А-Яа-яA-Za-z0-9-_]+)")) {
                    error.put("name", "Имя указано неверно. ");
                } else {
                    user.setName(name);

                }
            }
            if (password != null) {
                if (password.length() < 6) {
                    error.put("password", "Пароль короче 6-ти символов");
                } else {
                    user.setName(passwordEncoder().encode(password));

                }
            }
            if (email != null) {
                if (!email.equals(user.getEmail()) && userRepository.findByEmail(email) != null) {
                    error.put("email", "Этот e-mail уже зарегистрирован");
                } else {
                    user.setEmail(email);

                }
            }
            if (removePhoto != null) {
                if (removePhoto == 1) {
                    user.setPhoto("empty");
                }
                if (removePhoto == 0) {
                    if (photo.isEmpty()) {
                        error.put("photo", "файл отсутствует");
                    }else {
                        BufferedImage bufferedImageFromFile = Scalr
                                .resize(ImageIO.read(photo.getInputStream()), 36, 36);
                        user.setPhoto(fileSystemStorageService
                                .cloudStore(bufferedImageFromFile, Objects.requireNonNull(photo.getOriginalFilename())));

                    }
                }
            }
        }
        if (error.isEmpty()) {
            log.info(String.format("user.getName())  '%s': ", user.getName()));
            log.info(String.format("user.getEmail()  '%s': ", user.getEmail()));
            log.info(String.format("user.getPassword()  '%s': ", user.getPassword()));
            log.info(String.format("user.getName())  '%s': ", user.getPhoto()));
            userRepository.save(user);
            return ResponseEntity.ok(new ResultResponse(true));
        }
        return ResponseEntity.ok(new ErrorResponse(false, error));
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}


