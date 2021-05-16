package diplom.blog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void register() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void check() {
    }

    @Test
    void captcha() {
    }

    @Test
    void restorePassword() {
    }

    @Test
    void authPassword() {
    }
}