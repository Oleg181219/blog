package diplom.blog.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import diplom.blog.api.response.ErrorResponse;
import diplom.blog.repo.UserRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class FileSystemStorageService {
    private final UserRepository userRepository;

    public FileSystemStorageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String cloudStore(Principal principal, Image photo, String name) throws IOException {
        final var CLOUD_NAME = "dsnia8hfx";
        final var API_KEY = "567365452572383";
        final var API_SECRET = "6_-E13f997sdsvq7F4oykc9DnEE";
        var cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
        String path = "upload/" + getRandomPath() + "/" +
                name.substring(0, name.lastIndexOf('.'));

        var params = ObjectUtils.asMap(
                "public_id", path,
                "overwrite", true);

       var qwer =  toBufferedImage(photo);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(qwer, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var imageString = "data:image/png;base64," +
                DatatypeConverter.printBase64Binary(baos.toByteArray());
        var upload = cloudinary.uploader().upload(imageString, params);



        return upload.get("url").toString();
    }


    public Object store(HttpServletRequest request, MultipartFile image, Principal principal) {

        final var FILE_PATTERN = Pattern.compile("^(.*)(.)(png|jpe?g)$");


        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (image.isEmpty()
                || !FILE_PATTERN.matcher(Objects.requireNonNull(image.getOriginalFilename())).matches()) {
            var errorResponse = new ErrorResponse();
            HashMap<String, String> errors = new HashMap<>();
            errors.put("image", "Файл должен быть изображением png, jpg, jpeg");
            errorResponse.setResult(false);
            errorResponse.setErrors(errors);
            return new ResponseEntity<ErrorResponse>(HttpStatus.NOT_FOUND);
        }
        if (image.getSize() > 5242880) {

            var errorResponse = new ErrorResponse();
            HashMap<String, String> errors = new HashMap<>();
            errors.put("image", "Размер файла превышает допустимый размер");
            errorResponse.setResult(false);
            errorResponse.setErrors(errors);
            return new ResponseEntity<ErrorResponse>(HttpStatus.NOT_FOUND);
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


    private  BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        var bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
}
