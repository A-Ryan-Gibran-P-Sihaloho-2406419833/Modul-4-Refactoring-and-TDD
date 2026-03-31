package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;

    @BeforeEach
    void setUp() {
        this.product = new Product();
        this.product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        this.product.setProductName("Sampo Cap Bambang");
        this.product.setProductQuantity(100);
    }

    @Test
    void testGetProductId() {
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", this.product.getProductId(), "ID produk harus sesuai dengan yang di-set");
    }

    @Test
    void testGetProductName() {
        assertEquals("Sampo Cap Bambang", this.product.getProductName(), "Nama produk harus sesuai dengan yang di-set");
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(100, this.product.getProductQuantity(), "Kuantitas produk harus sesuai dengan yang di-set");
    }

    @Test
    void testGetProductIdNegative() {
        assertNotEquals("wrong-id-123", this.product.getProductId(), "ID produk tidak boleh sama dengan ID yang salah");
    }

    @Test
    void testGetProductNameNegative() {
        assertNotEquals("Sampo Cap Bango", this.product.getProductName(), "Nama produk tidak boleh sama dengan nama yang salah");
    }

    @Test
    void testGetProductQuantityNegative() {
        assertNotEquals(0, this.product.getProductQuantity(), "Kuantitas produk tidak boleh sama dengan kuantitas yang salah");
    }
}