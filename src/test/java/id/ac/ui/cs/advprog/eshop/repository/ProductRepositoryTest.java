package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    private static final String UUID_1 = "uuid-1";

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        boolean hasNext = productIterator.hasNext();
        Product savedProduct = hasNext ? productIterator.next() : new Product();

        boolean isIdCorrect = product.getProductId().equals(savedProduct.getProductId());
        boolean isNameCorrect = product.getProductName().equals(savedProduct.getProductName());
        boolean isQuantityCorrect = product.getProductQuantity() == savedProduct.getProductQuantity();

        assertTrue(hasNext && isIdCorrect && isNameCorrect && isQuantityCorrect,
                "Produk yang dibuat harus dapat ditemukan dengan data yang sesuai");
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext(), "Mencari pada repository kosong harus mengembalikan iterator kosong");
    }

    @Test
    void testFindAllMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        boolean hasNext1 = productIterator.hasNext();
        Product savedProduct1 = hasNext1 ? productIterator.next() : new Product();
        boolean isId1Correct = product1.getProductId().equals(savedProduct1.getProductId());

        boolean hasNext2 = productIterator.hasNext();
        Product savedProduct2 = hasNext2 ? productIterator.next() : new Product();
        boolean isId2Correct = product2.getProductId().equals(savedProduct2.getProductId());

        boolean hasNoMoreNext = !productIterator.hasNext();

        assertTrue(hasNext1 && isId1Correct && hasNext2 && isId2Correct && hasNoMoreNext,
                "Harus bisa mengiterasi semua produk yang telah dibuat dengan benar");
    }

    @Test
    void testCreateWithNullId() {
        Product product = new Product();
        product.setProductName("Product No ID");
        product.setProductQuantity(10);

        productRepository.create(product);

        assertNotNull(product.getProductId(), "Produk yang dibuat tanpa ID harus secara otomatis diberikan ID");
    }

    @Test
    void testFindByIdFound() {
        Product product = new Product();
        product.setProductId(UUID_1);
        productRepository.create(product);

        Product found = productRepository.findById(UUID_1);
        assertNotNull(found, "Mencari produk dengan ID valid tidak boleh mengembalikan null");
    }

    @Test
    void testFindByIdNotFound() {
        Product found = productRepository.findById("uuid-random-404");
        assertNull(found, "Mencari produk dengan ID tidak valid harus mengembalikan null");
    }

    @Test
    void testUpdateSuccess() {
        Product product = new Product();
        product.setProductId(UUID_1);
        product.setProductName("Original Name");
        productRepository.create(product);

        Product updatedData = new Product();
        updatedData.setProductId(UUID_1);
        updatedData.setProductName("New Name");
        updatedData.setProductQuantity(99);

        Product result = productRepository.update(updatedData);

        boolean isResultNotNull = result != null;
        boolean isNameUpdated = isResultNotNull && "New Name".equals(result.getProductName());
        boolean isQuantityUpdated = isResultNotNull && result.getProductQuantity() == 99;

        assertTrue(isResultNotNull && isNameUpdated && isQuantityUpdated,
                "Memperbarui produk harus mengembalikan produk yang diperbarui dengan data baru");
    }

    @Test
    void testUpdateNotFound() {
        Product product = new Product();
        product.setProductId(UUID_1);

        Product result = productRepository.update(product);
        assertNull(result, "Memperbarui produk yang tidak ada harus mengembalikan null");
    }

    @Test
    void testDeleteSuccess() {
        Product product = new Product();
        product.setProductId(UUID_1);
        productRepository.create(product);

        productRepository.delete(UUID_1);

        assertNull(productRepository.findById(UUID_1), "Produk yang telah dihapus tidak boleh ditemukan lagi");
    }

    @Test
    void testDeleteNotFound() {
        productRepository.delete("uuid-random");
        Iterator<Product> iterator = productRepository.findAll();
        assertFalse(iterator.hasNext(), "Menghapus ID yang tidak ada tidak boleh mengubah repository kosong");
    }

    @Test
    void testFindByIdNotFoundWhenDataExists() {
        Product product1 = new Product();
        product1.setProductId(UUID_1);
        product1.setProductName("Sampo");
        productRepository.create(product1);

        Product found = productRepository.findById("uuid-zombie");
        assertNull(found, "Mencari produk dengan ID tidak valid saat data lain ada harus mengembalikan null");
    }

    @Test
    void testUpdateNotFoundWhenDataExists() {
        Product product1 = new Product();
        product1.setProductId(UUID_1);
        product1.setProductName("Sampo");
        productRepository.create(product1);

        Product productZ = new Product();
        productZ.setProductId("uuid-zombie");
        productZ.setProductName("Sabun");

        Product result = productRepository.update(productZ);
        assertNull(result, "Memperbarui produk dengan ID tidak valid saat data lain ada harus mengembalikan null");
    }
}