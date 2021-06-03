package diplom.blog.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import diplom.blog.api.response.ErrorResponse;
import diplom.blog.repo.UserRepository;
import diplom.blog.util.AuthCheck;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class FileSystemStorageService {
    private final UserRepository userRepository;
    private final AuthCheck authCheck;

    public FileSystemStorageService(UserRepository userRepository, AuthCheck authCheck) {
        this.userRepository = userRepository;
        this.authCheck = authCheck;
    }

    @Value("${blog.cloud_name}")
    private String CLOUD_NAME;
    @Value("${blog.api_key}")
    private String API_KEY;
    @Value("${blog.api_secret}")
    private String API_SECRET;


    public String cloudStore(BufferedImage photo, String name) throws IOException {

        var cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
        String path = "upload/" + getRandomPath() + "/" +
                name.substring(0, name.lastIndexOf('.'));

        var params = ObjectUtils.asMap(
                "public_id", path,
                "overwrite", true);




        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(photo, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var imageString = "data:image/png;base64," +
                DatatypeConverter.printBase64Binary(baos.toByteArray());
        var upload = cloudinary.uploader().upload(imageString, params);


        return upload.get("url").toString();
    }


    public Object store(HttpServletRequest request, MultipartFile image) {

        final var FILE_PATTERN = Pattern.compile("^(.*)(.)(png|jpe?g)$");

        if(authCheck.securityCheck()) {
            if (image.isEmpty()){
                return ResponseEntity.badRequest().body(new ErrorResponse(false));
            }
            if(!FILE_PATTERN.matcher(Objects.requireNonNull(image.getOriginalFilename())).matches()) {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("image", "Файл должен быть изображением png, jpg, jpeg");
                return ResponseEntity.badRequest().body(new ErrorResponse(false, errors));
            }
            if (image.getSize() > 5242880) {

                HashMap<String, String> errors = new HashMap<>();
                errors.put("image", "Размер файла превышает допустимый размер");
                return ResponseEntity.ok(new ErrorResponse(false, errors));
            }

            String path = "/upload/" + getRandomPath() + "/" + image.getOriginalFilename();

            String realPath = request.getServletContext().getRealPath(path);


            try {
                byte[] photo = image.getBytes();

                File file = new File(realPath);
                FileUtils.writeByteArrayToFile(file, photo);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return path;
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(false));
    }


    private static String getRandomPath() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            for (int ch = 0; ch < 2; ch++) {
                sb.append((char) (new Random().nextInt('z' - 'a') + 'a'));
            }
            sb.append("/");
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }


    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        var bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
}
