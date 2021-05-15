package diplom.blog.service;

import diplom.blog.api.request.MyProfileRequest;
import diplom.blog.api.response.ErrorResponse;
import diplom.blog.api.response.ResultResponse;
import diplom.blog.model.User;
import diplom.blog.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import static java.awt.Image.SCALE_DEFAULT;

@Component
public class ProfileService {
    private final FileSystemStorageService fileSystemStorageService;
    private final UserRepository userRepository;

    public ProfileService(FileSystemStorageService fileSystemStorageService
            , UserRepository userRepository) {
        this.fileSystemStorageService = fileSystemStorageService;
        this.userRepository = userRepository;
    }


    public ResponseEntity<?> profileMy(MultipartFile photo,
                                       String name,
                                       String email,
                                       String password,
                                       Principal principal) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse();
        var error = new HashMap<String, String>();
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        var user = userRepository.findByEmail(principal.getName());

        if (photo.getSize() > 5242880) {
            error.put("photo", "Фото слишком большое, нужно не более 5 Мб");
        } else {
            BufferedImage bufferedImage = ImageIO.read(photo.getInputStream());
            var ant = bufferedImage.getScaledInstance(36, 36, SCALE_DEFAULT);
            var fileName = photo.getOriginalFilename();
            user.setPhoto(fileSystemStorageService.cloudStore(principal, ant, fileName));
        }


        if (email != null) {
            if (!email.equals(user.getEmail())) {
                List<User> userByEmail = userRepository.findAllUserByEmail(email);
                if (userByEmail.isEmpty()) {
                    user.setEmail(email);
                } else {
                    error.put("email", "Этот e-mail уже зарегистрирован");
                }
            }
        }
        if (password != null) {
            if (password.length() >= 6) {
                user.setPassword(passwordEncoder().encode(password));
            } else {
                error.put("password", "Пароль короче 6-ти символов");
            }
        }

        if (name != null) {
            if (!name.matches("([А-Яа-яA-Za-z0-9-_]+)")) {
                error.put("name", "Имя указано неверно. ");
            } else {
                user.setName(name);
            }
        }

        if (!error.isEmpty()) {
            errorResponse.setResult(false);
            errorResponse.setErrors(error);
            return ResponseEntity.ok(errorResponse);
        }

        userRepository.save(user);
        var resultResponse = new ResultResponse();
        resultResponse.setResult(true);
        return ResponseEntity.ok(resultResponse);
    }


    public ResponseEntity<?> profileMyWithoutFoto(MyProfileRequest myProfileRequest,
                                                  Principal principal) {
        ErrorResponse errorResponse = new ErrorResponse();
        var resultResponse = new ResultResponse();
        var error = new HashMap<String, String>();

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        var user = userRepository.findByEmail(principal.getName());

        if (myProfileRequest.getRemovePhoto() == 1) {
            user.setPhoto(null);
        }

        if (myProfileRequest.getName() != null) {
            if (myProfileRequest.getName().matches("([А-Яа-яA-Za-z0-9-_]+)")) {
                if (!user.getName().equals(myProfileRequest.getName())) {
                    user.setName(myProfileRequest.getName());
                }
            } else {
                error.put("name", "Имя указано неверно. ");
            }
        }

        if (myProfileRequest.getPassword() != null) {
            if (myProfileRequest.getPassword().length() >= 6) {
                user.setPassword(passwordEncoder().encode(myProfileRequest.getPassword()));
            } else {
                error.put("password", "Пароль короче 6-ти символов");
            }
        }

        if (myProfileRequest.getEmail() != null) {
            if (!myProfileRequest.getEmail().equals(user.getEmail())) {
                List<User> userByEmail = userRepository.findAllUserByEmail(myProfileRequest.getEmail());
                if (userByEmail.isEmpty()) {
                    user.setEmail(myProfileRequest.getEmail());
                } else {
                    error.put("email", "Этот e-mail уже зарегистрирован");
                }
            }
        }

        if (!error.isEmpty()) {
            errorResponse.setResult(false);
            errorResponse.setErrors(error);
            return ResponseEntity.ok(errorResponse);
        }

        userRepository.save(user);
        resultResponse.setResult(true);
        return ResponseEntity.ok(resultResponse);
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }



}
