package diplom.blog.configs.exeption;

import java.util.Map;

public abstract class AppExeption extends Exception{

    private Boolean result;

    private Map<String, String> errors;

    protected AppExeption(Boolean result, Map<String, String> errors){
        super((Throwable) errors);
        this.result = result;
        this.errors = errors;
    }

    public Boolean getResult() {
        return result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
