package id.ac.ui.cs.advprog.eshop.service;

import enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    OrderRepository orderRepository;

    List<Order> orders;

    private static final String INVALID_ID = "zczc";

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void testCreateOrder() {
        Order order = orders.get(1);
        doReturn(order).when(orderRepository).save(order);

        Order result = orderService.createOrder(order);

        verify(orderRepository, times(1)).save(order);
        assertEquals(order.getId(), result.getId(), "Order yang dibuat harus memiliki ID yang sama");
    }

    @Test
    void testCreateOrderIfAlreadyExists() {
        Order order = orders.get(1);
        doReturn(order).when(orderRepository).findById(order.getId());

        assertNull(orderService.createOrder(order), "Membuat order yang sudah ada harus mengembalikan null");
        verify(orderRepository, times(0)).save(order);
    }

    @Test
    void testUpdateStatus() {
        Order order = orders.get(1);
        Order newOrder = new Order(order.getId(), order.getProducts(), order.getOrderTime(),
                order.getAuthor(), OrderStatus.SUCCESS.getValue());

        doReturn(order).when(orderRepository).findById(order.getId());
        doReturn(newOrder).when(orderRepository).save(any(Order.class));

        Order result = orderService.updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());

        boolean isIdCorrect = order.getId().equals(result.getId());
        boolean isStatusCorrect = OrderStatus.SUCCESS.getValue().equals(result.getStatus());

        assertTrue(isIdCorrect && isStatusCorrect, "Status order harus berhasil diperbarui menjadi SUCCESS");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateStatusInvalidStatus() {
        Order order = orders.get(1);
        doReturn(order).when(orderRepository).findById(order.getId());

        assertThrows(IllegalArgumentException.class,
                () -> orderService.updateStatus(order.getId(), "MEOW"), "Harus melempar IllegalArgumentException jika status invalid");
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void testUpdateStatusInvalidOrderId() {
        doReturn(null).when(orderRepository).findById(INVALID_ID);

        assertThrows(NoSuchElementException.class,
                () -> orderService.updateStatus(INVALID_ID, OrderStatus.SUCCESS.getValue()), "Harus melempar NoSuchElementException jika ID tidak ditemukan");
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void testFindByIdIfIdFound() {
        Order order = orders.get(1);
        doReturn(order).when(orderRepository).findById(order.getId());

        Order result = orderService.findById(order.getId());
        assertEquals(order.getId(), result.getId(), "Mencari order dengan ID valid harus mengembalikan order yang sesuai");
    }

    @Test
    void testFindByIdIfIdNotFound() {
        doReturn(null).when(orderRepository).findById(INVALID_ID);
        assertNull(orderService.findById(INVALID_ID), "Mencari order dengan ID invalid harus mengembalikan null");
    }

    @Test
    void testFindAllByAuthorIfAuthorCorrect() {
        Order order = orders.get(1);
        doReturn(orders).when(orderRepository).findAllByAuthor(order.getAuthor());

        List<Order> results = orderService.findAllByAuthor(order.getAuthor());

        boolean allAuthorsMatch = true;
        for (Order result : results) {
            if (!order.getAuthor().equals(result.getAuthor())) {
                allAuthorsMatch = false;
                break;
            }
        }
        boolean isSizeCorrect = results.size() == 2;

        assertTrue(allAuthorsMatch && isSizeCorrect, "Mencari berdasarkan author harus mengembalikan semua order dari author tersebut");
    }

    @Test
    void testFindAllByAuthorIfAllLowercase() {
        Order order = orders.get(1);
        String lowercaseAuthor = order.getAuthor().toLowerCase(Locale.ROOT);
        doReturn(new ArrayList<Order>()).when(orderRepository).findAllByAuthor(lowercaseAuthor);

        List<Order> results = orderService.findAllByAuthor(lowercaseAuthor);
        assertTrue(results.isEmpty(), "Pencarian author bersifat case-sensitive, harus kosong jika menggunakan huruf kecil semua");
    }
}