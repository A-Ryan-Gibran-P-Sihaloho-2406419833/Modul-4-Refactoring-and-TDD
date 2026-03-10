package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Order order;

    private static final String VOUCHER_CODE_KEY = "voucherCode";
    private static final String VOUCHER_METHOD = "VOUCHER";
    private static final String REJECTED_STATUS = "REJECTED";

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        this.order = new Order("13652556-0128-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testCreatePaymentVoucherSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put(VOUCHER_CODE_KEY, "ESHOP1234ABC5678");

        Payment payment = new Payment("payment-1", order, VOUCHER_METHOD, paymentData);
        assertEquals("SUCCESS", payment.getStatus(), "Status payment voucher valid harus SUCCESS");
    }

    @Test
    void testCreatePaymentVoucherRejected_Not16Chars() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put(VOUCHER_CODE_KEY, "ESHOP123");

        Payment payment = new Payment("payment-2", order, VOUCHER_METHOD, paymentData);
        assertEquals(REJECTED_STATUS, payment.getStatus(), "Status payment voucher < 16 karakter harus REJECTED");
    }

    @Test
    void testCreatePaymentVoucherRejected_NotStartWithEshop() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put(VOUCHER_CODE_KEY, "PAYMT1234ABC5678");

        Payment payment = new Payment("payment-3", order, VOUCHER_METHOD, paymentData);
        assertEquals(REJECTED_STATUS, payment.getStatus(), "Status payment voucher tanpa awalan ESHOP harus REJECTED");
    }

    @Test
    void testCreatePaymentVoucherRejected_Not8Numerics() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put(VOUCHER_CODE_KEY, "ESHOP1234ABCDE56");

        Payment payment = new Payment("payment-4", order, VOUCHER_METHOD, paymentData);
        assertEquals(REJECTED_STATUS, payment.getStatus(), "Status payment voucher angka bukan 8 digit harus REJECTED");
    }

    @Test
    void testCreatePaymentCODSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jalan Margonda Raya");
        paymentData.put("deliveryFee", "15000");

        Payment payment = new Payment("payment-5", order, "COD", paymentData);
        assertEquals("SUCCESS", payment.getStatus(), "Status payment COD valid harus SUCCESS");
    }

    @Test
    void testCreatePaymentCODRejected_EmptyAddress() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "15000");

        Payment payment = new Payment("payment-6", order, "COD", paymentData);
        assertEquals(REJECTED_STATUS, payment.getStatus(), "Status payment COD address kosong harus REJECTED");
    }

    @Test
    void testCreatePaymentCODRejected_NullDeliveryFee() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jalan Margonda Raya");

        Payment payment = new Payment("payment-7", order, "COD", paymentData);
        assertEquals(REJECTED_STATUS, payment.getStatus(), "Status payment COD fee null harus REJECTED");
    }
}