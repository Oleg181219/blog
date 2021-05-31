package diplom.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarResponse  implements Response{

    List<String> years;

    Map<String, Integer> posts;

}
