package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private CarService carService;

    private Product product;

    private static final String PRODUCT_ATTR = "product";
    private static final String LIST_REDIRECT = "list";
    private static final String RESULT_MESSAGE = "Hasil MvcResult tidak boleh null";

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateProductPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists(PRODUCT_ATTR))
                .andReturn();

        assertNotNull(result, RESULT_MESSAGE);
    }

    @Test
    void testCreateProductPost() throws Exception {
        when(productService.create(any(Product.class))).thenReturn(product);

        MvcResult result = mockMvc.perform(post("/product/create")
                        .flashAttr(PRODUCT_ATTR, product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LIST_REDIRECT))
                .andReturn();

        verify(productService, times(1)).create(any(Product.class));
        assertNotNull(result, RESULT_MESSAGE);
    }

    @Test
    void testProductListPage() throws Exception {
        List<Product> products = Arrays.asList(product);
        when(productService.findAll()).thenReturn(products);

        MvcResult result = mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"))
                .andReturn();

        assertNotNull(result, RESULT_MESSAGE);
    }

    @Test
    void testEditProductPage() throws Exception {
        String id = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        when(productService.findById(id)).thenReturn(product);

        MvcResult result = mockMvc.perform(get("/product/edit/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists(PRODUCT_ATTR))
                .andReturn();

        assertNotNull(result, RESULT_MESSAGE);
    }

    @Test
    void testEditProductPost() throws Exception {
        MvcResult result = mockMvc.perform(post("/product/edit")
                        .flashAttr(PRODUCT_ATTR, product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LIST_REDIRECT))
                .andReturn();

        verify(productService, times(1)).update(any(Product.class));
        assertNotNull(result, RESULT_MESSAGE);
    }

    @Test
    void testDeleteProduct() throws Exception {
        String id = "eb558e9f-1c39-460e-8860-71af6af63bd6";

        MvcResult result = mockMvc.perform(get("/product/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("../list"))
                .andReturn();

        verify(productService, times(1)).delete(id);
        assertNotNull(result, RESULT_MESSAGE);
    }
}