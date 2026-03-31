package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    List<Payment> payments;
    Order order;

    private static final String VOUCHER_METHOD = "VOUCHER";
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String REJECTED_STATUS = "REJECTED";
    private static final String FAILED_STATUS = "FAILED";
    private static final String INVALID_ID = "zczc";

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-0128-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        payments = new ArrayList<>();
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("payment-1", order, VOUCHER_METHOD, paymentData1);
        payments.add(payment1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("address", "Jalan Margonda Raya");
        paymentData2.put("deliveryFee", "15000");
        Payment payment2 = new Payment("payment-2", order, "COD", paymentData2);
        payments.add(payment2);
    }

    @Test
    void testAddPayment() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, VOUCHER_METHOD, paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));

        boolean isMethodCorrect = VOUCHER_METHOD.equals(result.getMethod());
        boolean isStatusCorrect = SUCCESS_STATUS.equals(result.getStatus());

        assertTrue(isMethodCorrect && isStatusCorrect, "Penambahan payment harus memiliki method dan status yang tepat");
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = payments.get(1); // COD payment

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, SUCCESS_STATUS);

        boolean isPaymentStatusSuccess = SUCCESS_STATUS.equals(result.getStatus());
        boolean isOrderStatusSuccess = SUCCESS_STATUS.equals(result.getOrder().getStatus());

        assertTrue(isPaymentStatusSuccess && isOrderStatusSuccess, "Jika payment SUCCESS, status order juga harus berubah menjadi SUCCESS");
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = payments.get(1); // COD payment

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, REJECTED_STATUS);

        boolean isPaymentStatusRejected = REJECTED_STATUS.equals(result.getStatus());
        boolean isOrderStatusFailed = FAILED_STATUS.equals(result.getOrder().getStatus());

        assertTrue(isPaymentStatusRejected && isOrderStatusFailed, "Jika payment REJECTED, status order harus berubah menjadi FAILED");
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusInvalidPaymentId() {
        doReturn(null).when(paymentRepository).findById(INVALID_ID);

        Payment dummyPayment = new Payment(INVALID_ID, order, VOUCHER_METHOD, new HashMap<>());
        assertThrows(NoSuchElementException.class, () -> paymentService.setStatus(dummyPayment, SUCCESS_STATUS),
                "Mengatur status pada payment ID tidak valid harus melempar NoSuchElementException");
    }

    @Test
    void testGetPayment() {
        Payment payment = payments.get(1);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId(), "Mencari payment dengan ID valid harus mengembalikan payment yang tepat");
    }

    @Test
    void testGetPaymentNotFound() {
        doReturn(null).when(paymentRepository).findById(INVALID_ID);
        assertNull(paymentService.getPayment(INVALID_ID), "Mencari payment dengan ID invalid harus mengembalikan null");
    }

    @Test
    void testGetAllPayments() {
        doReturn(payments).when(paymentRepository).findAll();
        List<Payment> results = paymentService.getAllPayments();
        assertEquals(2, results.size(), "Mencari semua payment harus mengembalikan jumlah yang sesuai di repositori");
    }
}