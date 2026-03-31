package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {
            // Memastikan context Spring Boot berhasil dimuat tanpa error
        });
    }

    @Test
    void testMain() {
        assertDoesNotThrow(() -> EshopApplication.main(new String[] {}));
    }

}