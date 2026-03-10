package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    private Payment payment;

    // Konstanta untuk menghindari teguran PMD
    private static final String PAYMENT_LIST_URL = "/payment/list";
    private static final String PAYMENT_LIST_VIEW = "paymentList";
    private static final String PAYMENTS_ATTR = "payments";
    private static final String REDIRECT_URL = "redirect:/payment/list";
    private static final String RESULT_MESSAGE = "Hasil MvcResult tidak boleh null";
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String REJECTED_STATUS = "REJECTED";

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order("13652556-0128-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        payment = new Payment("payment-1", order, "VOUCHER", new HashMap<>());
    }

    @Test
    void testPaymentListPage() throws Exception {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);

        when(paymentService.getAllPayments()).thenReturn(payments);

        MvcResult result = mockMvc.perform(get(PAYMENT_LIST_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(PAYMENT_LIST_VIEW))
                .andExpect(model().attributeExists(PAYMENTS_ATTR))
                .andReturn();

        verify(paymentService, times(1)).getAllPayments();
        assertNotNull(result, RESULT_MESSAGE);
    }

    @Test
    void testUpdatePaymentStatusSuccess() throws Exception {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        MvcResult result = mockMvc.perform(post("/payment/" + payment.getId() + "/success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_URL))
                .andReturn();

        verify(paymentService, times(1)).setStatus(payment, SUCCESS_STATUS);
        assertNotNull(result, RESULT_MESSAGE);
    }

    @Test
    void testUpdatePaymentStatusReject() throws Exception {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        MvcResult result = mockMvc.perform(post("/payment/" + payment.getId() + "/reject"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_URL))
                .andReturn();

        verify(paymentService, times(1)).setStatus(payment, REJECTED_STATUS);
        assertNotNull(result, RESULT_MESSAGE);
    }
}