package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreate() {
        when(productRepository.create(product)).thenReturn(product);

        Product result = productService.create(product);

        assertEquals(product.getProductId(), result.getProductId(), "Produk yang dibuat harus memiliki ID yang sama");
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAll() {

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Iterator<Product> iterator = productList.iterator();

        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();

        boolean isSizeCorrect = result.size() == 1;
        boolean isNameCorrect = !result.isEmpty() && "Sampo Cap Bambang".equals(result.get(0).getProductName());

        assertTrue(isSizeCorrect && isNameCorrect, "Mencari semua produk harus mengembalikan list yang tepat");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.findById(product.getProductId())).thenReturn(product);

        Product result = productService.findById(product.getProductId());

        boolean isNotNull = result != null;
        boolean isIdCorrect = isNotNull && product.getProductId().equals(result.getProductId());

        assertTrue(isIdCorrect, "Mencari produk berdasarkan ID harus mengembalikan produk yang sesuai");
        verify(productRepository, times(1)).findById(product.getProductId());
    }

    @Test
    void testUpdate() {
        when(productRepository.update(product)).thenReturn(product);

        productService.update(product);

        verify(productRepository, times(1)).update(product);
    }

    @Test
    void testDelete() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        productService.delete(productId);

        verify(productRepository, times(1)).delete(productId);
    }
}