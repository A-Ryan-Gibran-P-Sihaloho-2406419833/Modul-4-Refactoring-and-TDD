package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {
    OrderRepository orderRepository;
    List<Order> orders;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        List<Product> products = new ArrayList<>();

        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        orders = new ArrayList<>();
        Order order1 = new Order("13652556-0128-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
        orders.add(order1);

        Order order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
                products, 1708570000L, "Safira Sudrajat");
        orders.add(order2);

        Order order3 = new Order("2334ef40-9eff-4da8-9487-8ee697ecbf1e",
                products, 1708570000L, "Bambang Sudrajat");
        orders.add(order3);
    }

    @Test
    void testSaveCreate() {
        Order order = orders.get(1);
        Order result = orderRepository.save(order);
        Order findResult = orderRepository.findById(orders.get(1).getId());

        boolean isResultIdCorrect = order.getId().equals(result.getId());
        boolean isFindIdCorrect = order.getId().equals(findResult.getId());
        boolean isTimeCorrect = order.getOrderTime().equals(findResult.getOrderTime());
        boolean isAuthorCorrect = order.getAuthor().equals(findResult.getAuthor());
        boolean isStatusCorrect = order.getStatus().equals(findResult.getStatus());

        assertTrue(isResultIdCorrect && isFindIdCorrect && isTimeCorrect && isAuthorCorrect && isStatusCorrect,
                "Menyimpan order baru harus mengembalikan order yang tepat dan dapat ditemukan");
    }

    @Test
    void testSaveUpdate() {
        Order order = orders.get(1);
        orderRepository.save(order);
        Order newOrder = new Order(order.getId(), order.getProducts(), order.getOrderTime(),
                order.getAuthor(), enums.OrderStatus.SUCCESS.getValue());
        Order result = orderRepository.save(newOrder);
        Order findResult = orderRepository.findById(orders.get(1).getId());

        boolean isResultIdCorrect = order.getId().equals(result.getId());
        boolean isFindIdCorrect = order.getId().equals(findResult.getId());
        boolean isTimeCorrect = order.getOrderTime().equals(findResult.getOrderTime());
        boolean isAuthorCorrect = order.getAuthor().equals(findResult.getAuthor());
        boolean isStatusCorrect = enums.OrderStatus.SUCCESS.getValue().equals(findResult.getStatus());

        assertTrue(isResultIdCorrect && isFindIdCorrect && isTimeCorrect && isAuthorCorrect && isStatusCorrect,
                "Menyimpan order yang sudah ada harus memperbarui datanya dengan benar");
    }

    @Test
    void testFindByIdIfIdFound() {
        for (Order order : orders) {
            orderRepository.save(order);
        }
        Order findResult = orderRepository.findById(orders.get(1).getId());

        boolean isIdCorrect = orders.get(1).getId().equals(findResult.getId());
        boolean isTimeCorrect = orders.get(1).getOrderTime().equals(findResult.getOrderTime());
        boolean isAuthorCorrect = orders.get(1).getAuthor().equals(findResult.getAuthor());
        boolean isStatusCorrect = orders.get(1).getStatus().equals(findResult.getStatus());

        assertTrue(isIdCorrect && isTimeCorrect && isAuthorCorrect && isStatusCorrect,
                "Mencari order dengan ID valid harus mengembalikan data yang benar");
    }

    @Test
    void testFindByIdIfIdNotFound() {
        for (Order order : orders) {
            orderRepository.save(order);
        }
        Order findResult = orderRepository.findById("zczc");
        assertNull(findResult, "Mencari order dengan ID tidak valid harus mengembalikan null");
    }

    @Test
    void testFindAllByAuthorIfAuthorCorrect() {
        for (Order order : orders) {
            orderRepository.save(order);
        }
        List<Order> orderList = orderRepository.findAllByAuthor(orders.get(1).getAuthor());
        assertEquals(2, orderList.size(), "Mencari order dengan author valid harus mengembalikan list dengan ukuran yang benar");
    }

    @Test
    void testFindAllByAuthorIfAllLowercase() {
        orderRepository.save(orders.get(1));
        List<Order> orderList = orderRepository.findAllByAuthor(orders.get(1).getAuthor().toLowerCase(Locale.ROOT));
        assertTrue(orderList.isEmpty(), "Mencari order dengan author yang semuanya huruf kecil (case-sensitive) harus mengembalikan list kosong");
    }
}