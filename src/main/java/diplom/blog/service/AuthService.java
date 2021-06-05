package diplom.blog.service;


import com.github.cage.Cage;
import com.github.cage.YCage;
import diplom.blog.api.request.AuthPasswordRequest;
import diplom.blog.api.request.LoginRequest;
import diplom.blog.api.request.NewUserRequest;
import diplom.blog.api.response.ErrorResponse;
import diplom.blog.api.response.LoginResponse;
import diplom.blog.api.response.Response;
import diplom.blog.api.response.ResultResponse;
import diplom.blog.model.CaptchaCode;
import diplom.blog.model.DtoModel.CaptchaDTO;
import diplom.blog.model.DtoModel.UserLoginDTO;
import diplom.blog.model.User;
import diplom.blog.repo.CaptchaCodesRepository;
import diplom.blog.repo.GlobalSettingsRepository;
import diplom.blog.repo.PostRepository;
import diplom.blog.repo.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


@Service
public class AuthService {

    private final Logger logger = LogManager.getLogger(AuthService.class);

    private final GlobalSettingsRepository globalSettingsRepository;
    private final AuthenticationManager authenticationManager;
    private final CaptchaCodesRepository captchaCodesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @Autowired
    public JavaMailSender emailSender;
    @Value("${blog.lifeTimeCaptchaCode}")
    private String lifeTimeCaptchaCodeString;
    @Value("${blog.address}")
    private String pathToRestorePassword;
    private Cage cage;
    private StringBuilder secretCode;
    private StringBuilder captchaBaseCode;





    public AuthService(GlobalSettingsRepository globalSettingsRepository
            , AuthenticationManager authenticationManager
            , CaptchaCodesRepository captchaCodesRepository
            , UserRepository userRepository
            , PostRepository postRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
        this.authenticationManager = authenticationManager;
        this.captchaCodesRepository = captchaCodesRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public ResponseEntity<Response> register(NewUserRequest user, BindingResult errors) {

        HashMap<String, String> respMap = new HashMap<>();
        logger.error(String.format("NewUserRequest errors '%s'", errors.getAllErrors()));
        if(errors.hasErrors()) {
            logger.error(String.format("NewUserRequest errors '%s'", errors.getAllErrors()));
            if (user.getPassword().length() <= 6) {
                respMap.put("password", "Пароль короче 6-ти символов");
            }

            if (!user.getName().matches("([А-Яа-яA-Za-z0-9-_]+)")) {
                respMap.put("name", "Имя указано неверно. ");
            }
        }

        var emailResp = userRepository.findByEmail(user.getEmail());
        var capCod = captchaCodesRepository.findBySecretCode(user.getCaptchaSecret());

        if (!capCod.getCode().equals(user.getCaptcha())) {
            respMap.put("captcha", "Код с картинки введён неверно");
        }
        if (emailResp != null) {
            respMap.put("email", "Этот e-mail уже зарегистрирован");
        }


        cage = new YCage();
        BufferedImage image = cage.drawImage(user.getCaptcha());
        var authResponse = new ErrorResponse();
        if (respMap.isEmpty()) {
            authResponse.setResult(true);

            var newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setName(user.getName());
            newUser.setPassword(passwordEncoder().encode(user.getPassword()));
            newUser.setIsModerator(-1);
            newUser.setRegTime(new Date());
            userRepository.save(newUser);

            return ResponseEntity.ok(new ErrorResponse(true));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(false, respMap));
    }

    //=================================================================================
    public ResponseEntity<Response> login(LoginRequest loginRequest, Errors error) {

        logger.error(String.format("Problem with LoginRequest: '%s'", error.getAllErrors()));

        logger.info(String.format("Start data email: '%s': ", loginRequest.getEmail()));

        var curentUser = userRepository.findByEmail(loginRequest.getEmail());
        logger.info(String.format("Current User from DB : '%s':", (curentUser != null)));
        if (curentUser == null) {
            return ResponseEntity.ok(new ErrorResponse(false));
        }

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail()
                        , loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(getLoginResponse(curentUser));
    }

    //=================================================================================
    public ResponseEntity<Response> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.ok(new ErrorResponse(true));
    }

    //=================================================================================
    public ResponseEntity<Response> check() {
        var authName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (authName.isEmpty()) {
            return ResponseEntity.ok(new ResultResponse(false));
        }
        var curentUser = userRepository.findByEmail(authName);
        if (curentUser == null) {
            return ResponseEntity.ok(new ResultResponse(false));
        }
        return ResponseEntity.ok(getLoginResponse(curentUser));
    }

    //=================================================================================
    public CaptchaDTO captcha() {
        cage = new YCage();
        var dateForComparisons = new Date(new Date().getTime() - (Long.parseLong(lifeTimeCaptchaCodeString) * 1000));
        captchaCodesRepository.deleteAllByTimeBefore(dateForComparisons);
        var captcha = generateCaptcha();
        captchaBaseCode = new StringBuilder("data:image/png;base64,");
        BufferedImage image = cage.drawImage(captcha);
        captchaBaseCode.append(createCaptchaString(image));
//        captchaBaseCode.toString()
        captchaCodesRepository.save(new CaptchaCode(new Date(), captcha, secretCode.toString()));
        return new CaptchaDTO(secretCode.toString(), captchaBaseCode.toString());

    }

    //=================================================================================
    public ResponseEntity<Response> restorePassword(String email, Errors errors)  {

        var user = userRepository.findByEmail(email);
        logger.info(String.format("Find user in restorePassword email: '$s'", (user == null)));
        if (user == null) {
            return ResponseEntity.ok(new ResultResponse(false));
        }
        var hash = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 45; i++) {
            String CODE = "abcdefghijklmnopqrstuvwxyz1234567890";
            int index = (int) (random.nextFloat() * CODE.length());
            hash.append(CODE.charAt(index));
        }
        var text = new StringBuilder(pathToRestorePassword).append(hash).toString();//TODO прилепить время


        Runnable task = () -> {
            try {
                sendMail(text, email);
            } catch (MessagingException e) {
                logger.error(String.format("MessagingException  '$s'", e.toString() ));
            }

        };
        Thread thread = new Thread(task);
        thread.start();

        user.setCode(hash.toString() + (new Date().getTime()));
        userRepository.save(user);
        return ResponseEntity.ok(new ResultResponse(true));
    }
    //=================================================================================

    private void sendMail (String text, String email) throws MessagingException {


        var message = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "utf-8");
        var htmlMsg = "<a href=\"" + text + "\">Follow the link to change the password on the site</a>";
        message.setContent(htmlMsg, "text/html");
        helper.setTo(email);
        helper.setSubject("Test html email");
        logger.info(String.format("Message content '%s':", message.toString()));
        this.emailSender.send(message);
    }

    public ResponseEntity<Response> authPassword(AuthPasswordRequest authPasswordRequest, Errors error) {

        var authResponse = new ErrorResponse();
        var errors = new HashMap<String, String>();
        int userId = 0;
        var code = userRepository.findAll();

        for (User user : code) {
            if (user.getCode() != null) {
                if (user.getCode().length() > 44) {
                    if (user.getCode().substring(0, 45).equals(authPasswordRequest.getCode())) {
                        userId = user.getId();
                        if (new Date().getTime() - Long.parseLong(user.getCode().substring(45)) > 3600000L) {
                            errors.put("code", "Ссылка для восстановления пароля устарела. <a href=\"/auth/restore\">Запросить ссылку снова</a>");
                        }
                    }
                }
            }
        }
        if (authPasswordRequest.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        var capCod = captchaCodesRepository.findBySecretCode(authPasswordRequest.getCaptchaSecret());

        if (!capCod.getCode().equals(authPasswordRequest.getCaptcha())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        if (!errors.isEmpty()) {
            authResponse.setResult(false);
            authResponse.setErrors(errors);
            return ResponseEntity.ok(new ErrorResponse(false, errors));
        }

        var user = userRepository.findById(userId);

        user.setPassword(passwordEncoder().encode(authPasswordRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ResultResponse(true));


    }

    //----------------------------------------------------------------------------------
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private byte[] toByteArray(BufferedImage bi)
            throws IOException {

        var baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        return baos.toByteArray();

    }

    private String generateCaptcha() {
        secretCode = new StringBuilder();

        StringBuilder captchaBuffer = new StringBuilder();
        Random random = new Random();
        int codeLength = 15 + (int) (Math.random() * 10);
        for (int i = 0; i < codeLength; i++) {
            String CODE = "abcdefghijklmnopqrstuvwxyz1234567890";
            int index = (int) (random.nextFloat() * CODE.length());
            secretCode.append(CODE.charAt(index));
        }
        int captchaLength = 3;
        while (captchaBuffer.length() < captchaLength) {
            var captcha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            int index = (int) (random.nextFloat() * captcha.length());
            captchaBuffer.append(captcha.charAt(index));
        }
        return captchaBuffer.toString();
    }

    private BufferedImage resize(BufferedImage img) {
        Image tmp = img.getScaledInstance(100, 35, Image.SCALE_SMOOTH);
        var dimg = new BufferedImage(100, 35, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    private String createCaptchaString(BufferedImage image) {
        BufferedImage resizedImage = (resize(image));
        var bytes = new byte[0];
        try {
            bytes = toByteArray(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    private LoginResponse getLoginResponse(User curentUser) {

        var userLoginResponse = new UserLoginDTO();
        userLoginResponse.setEmail(curentUser.getEmail());
        userLoginResponse.setModeration(curentUser.getIsModerator() == 1);
        userLoginResponse.setModerationCount(curentUser.getIsModerator() == 1 ? postRepository.findAllByModerationStatus() : 0);
        userLoginResponse.setId((long) curentUser.getId());
        userLoginResponse.setName(curentUser.getName());
        userLoginResponse.setPhoto(curentUser.getPhoto());
        userLoginResponse.setSettings(curentUser.getIsModerator() == 1);

        return new LoginResponse(true, userLoginResponse);
    }
}
