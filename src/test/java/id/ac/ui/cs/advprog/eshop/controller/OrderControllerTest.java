package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private Order order;

    // Konstanta agar lolos PMD
    private static final String AUTHOR_NAME = "Safira Sudrajat";
    private static final String ORDER_LIST_URL = "/order/list/";
    private static final String ORDER_LIST_VIEW = "orderList";
    private static final String ORDERS_ATTR = "orders";
    private static final String RESULT_MESSAGE = "Hasil MvcResult tidak boleh null";

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-0128-4c07-b546-54eb1396d79b",
                products, 1708560000L, AUTHOR_NAME);
    }

    @Test
    void testOrderListPage() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        // Mengatur perilaku mock
        when(orderService.findAllByAuthor(AUTHOR_NAME)).thenReturn(orders);

        // Simulasi request GET
        MvcResult result = mockMvc.perform(get(ORDER_LIST_URL + AUTHOR_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(ORDER_LIST_VIEW))
                .andExpect(model().attributeExists(ORDERS_ATTR))
                .andReturn();

        // Verifikasi service dipanggil dan assert tidak null
        verify(orderService, times(1)).findAllByAuthor(AUTHOR_NAME);
        assertNotNull(result, RESULT_MESSAGE);
    }
}