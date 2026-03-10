package id.ac.ui.cs.advprog.eshop.model;

import enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private List<Product> products;

    private static final String ORDER_ID = "13652556-0128-4c07-b546-54eb1396d79b";
    private static final String AUTHOR = "Safira Sudrajat";

    @BeforeEach
    void setUp() {
        this.products = new ArrayList<>();

        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);

        Product product2 = new Product();
        product2.setProductId("a2c62328-4837-4664-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);

        this.products.add(product1);
        this.products.add(product2);
    }

    @Test
    void testCreateOrderEmptyProduct() {
        this.products.clear();
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(ORDER_ID, this.products, 1708560000L, AUTHOR);
        }, "Harus melempar IllegalArgumentException jika produk kosong");
    }

    @Test
    void testCreateOrderDefaultStatus() {
        Order order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                this.products, 1708560000L, AUTHOR);

        boolean isProductsSame = this.products.equals(order.getProducts());
        boolean isSizeCorrect = order.getProducts().size() == 2;
        boolean isName1Correct = "Sampo Cap Bambang".equals(order.getProducts().get(0).getProductName());
        boolean isName2Correct = "Sabun Cap Usep".equals(order.getProducts().get(1).getProductName());
        boolean isIdCorrect = "13652556-012a-4c07-b546-54eb1396d79b".equals(order.getId());
        boolean isTimeCorrect = order.getOrderTime() == 1708560000L;
        boolean isAuthorCorrect = AUTHOR.equals(order.getAuthor());
        boolean isStatusCorrect = OrderStatus.WAITING_PAYMENT.getValue().equals(order.getStatus());

        assertTrue(isProductsSame && isSizeCorrect && isName1Correct && isName2Correct
                        && isIdCorrect && isTimeCorrect && isAuthorCorrect && isStatusCorrect,
                "Pembuatan order dengan status default harus menginisialisasi semua atribut dengan benar");
    }

    @Test
    void testCreateOrderSuccessStatus() {
        Order order = new Order(ORDER_ID, this.products, 1708560000L, AUTHOR, OrderStatus.SUCCESS.getValue());
        assertEquals("SUCCESS", order.getStatus(), "Status order harus SUCCESS");
    }

    @Test
    void testCreateOrderInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(ORDER_ID, this.products, 1708560000L, AUTHOR, "MEOW");
        }, "Harus melempar IllegalArgumentException jika status tidak valid");
    }

    @Test
    void testSetStatusToCancelled() {
        Order order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                this.products, 1708560000L, AUTHOR);
        order.setStatus(OrderStatus.CANCELLED.getValue());
        assertEquals("CANCELLED", order.getStatus(), "Status order harus berubah menjadi CANCELLED");
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Order order = new Order(ORDER_ID, this.products, 1708560000L, AUTHOR);
        assertThrows(IllegalArgumentException.class, () -> order.setStatus("MEOW"),
                "Harus melempar IllegalArgumentException saat mengatur status yang tidak valid");
    }
}