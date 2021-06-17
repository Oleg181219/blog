package diplom.blog.configs.exeption;

import java.util.Map;

public class BadRequestEx extends AppExeption{
    protected BadRequestEx(Map<String, String> errors) {
        super(false, errors);
    }
}
