package product.trial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import product.trial.model.Product;
import product.trial.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest extends IntegrationTestBase {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/products";
        productRepository.deleteAll(); // Nettoie la base de données avant chaque test
    }

    @Test
    void testCreateProductAndRetrieveIt() {
        Product product = new Product();
        product.setName("Integration Test Product");
        HttpEntity<Product> request = new HttpEntity<>(product);

        ResponseEntity<Product> createResponse = restTemplate.postForEntity(baseUrl, request, Product.class);
        assertEquals(CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody().getId());

        Long productId = createResponse.getBody().getId();
        ResponseEntity<Product> retrieveResponse = restTemplate.getForEntity(baseUrl + "/" + productId, Product.class);
        assertEquals(OK, retrieveResponse.getStatusCode());
        assertEquals("Integration Test Product", retrieveResponse.getBody().getName());
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        Product product2 = new Product();
        product2.setName("Product 2");

        productRepository.save(product1);
        productRepository.save(product2);

        ResponseEntity<Product[]> response = restTemplate.getForEntity(baseUrl, Product[].class);
        assertEquals(OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setName("Product to Delete");
        product = productRepository.save(product);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(baseUrl + "/" + product.getId(), HttpMethod.DELETE, null, Void.class);
        assertEquals(OK, deleteResponse.getStatusCode());

        assertEquals(0, productRepository.findAll().size());
    }
}
