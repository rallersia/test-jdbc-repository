package edu.school21.repositories;

import edu.school21.models.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ProductsRepositoryJdbcImplTest extends EmbeddedDataSourceTest {
    static final List<Product> EXPECTED_FIND_ALL_PRODUCTS = new ArrayList<>(
            Arrays.asList(  new Product(0L, "product1", 11L),
                            new Product(1L, "product2", 22L),
                            new Product(2L, "product3", 33L),
                            new Product(3L, "product4", 44L)));
    static final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(0L, "product1", 11L);
    static final Product EXPECTED_UPDATED_PRODUCT = new Product(2L, "product_to_update", 5010L);

    public ProductsRepositoryJdbcImplTest() throws SQLException {
    }

    @Test
    void testFindAll() throws SQLException {
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(db);
        Assertions.assertEquals(EXPECTED_FIND_ALL_PRODUCTS, productsRepositoryJdbc.findAll());
    }

    @ParameterizedTest
    @MethodSource
    void testFindByID(Product product) throws SQLException {
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(db);
        if (product != null)
            Assertions.assertEquals(product, productsRepositoryJdbc.findById(product.getIdentifier()).orElse(null));
        else
            Assertions.assertNull(productsRepositoryJdbc.findById(100L).orElse(null));
    }

    private static Stream<Product> testFindByID() {
        return Stream.of(null, EXPECTED_FIND_BY_ID_PRODUCT);
    }

    @Test
    void testUpdate() throws SQLException {
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(db);
        productsRepositoryJdbc.update(new Product(2L, "product_to_update", 5010L));
        Assertions.assertEquals(EXPECTED_UPDATED_PRODUCT, productsRepositoryJdbc.findById(2L).orElse(null));
    }

    @Test
    void testSave() throws SQLException {
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(db);
        Product product = new Product(null, "save_product", 44L);
        productsRepositoryJdbc.save(product);
        Assertions.assertEquals(4L, product.getIdentifier());
    }

    @Test
    void testDelete() throws SQLException {
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(db);
        productsRepositoryJdbc.delete(1L);
        Assertions.assertNull(productsRepositoryJdbc.findById(1L).orElse(null));
    }
}
