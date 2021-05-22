package diplom.blog.service;

import diplom.blog.TestConfiguration.TagServiceTestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TagServiceTestConfiguration.class)
public class TagServiceTest {
}