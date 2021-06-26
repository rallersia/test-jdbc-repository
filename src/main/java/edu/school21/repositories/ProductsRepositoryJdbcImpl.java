package edu.school21.repositories;

import edu.school21.models.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {
    private final Connection connection;

    public ProductsRepositoryJdbcImpl(DataSource ds) throws SQLException {
        connection = ds.getConnection();
    }

    @Override
    public List<Product> findAll() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * from PRODUCT");
        ResultSet result = ps.executeQuery();
        List<Product> products = new ArrayList<>();
        while (result.next()) {
            Product product = new Product(result.getLong("identifier"),
                    result.getString("name"),
                    result.getLong("price"));
            products.add(product);
        }
        return products;
    }

    @Override
    public Optional<Product> findById(Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * from PRODUCT WHERE IDENTIFIER = " + id);
        ResultSet resultSet = ps.executeQuery();
        if (!resultSet.next())
            return Optional.empty();
        return Optional.of(new Product(resultSet.getLong("identifier"),
                            resultSet.getString("name"),
                            resultSet.getLong("price")));
    }

    @Override
    public void update(Product product) throws SQLException {
        PreparedStatement ps = connection.prepareStatement( "UPDATE PRODUCT " +
                                                                "SET NAME = ?, " +
                                                                "PRICE = ? " +
                                                                "WHERE IDENTIFIER = " + product.getIdentifier());
        ps.setString(1, product.getName());
        ps.setLong(2, product.getPrice());
        ps.execute();
    }

    @Override
    public void save(Product product) throws SQLException {
        PreparedStatement ps = connection.prepareStatement( "INSERT INTO PRODUCT(NAME, PRICE) VALUES (?, ?)");
        ps.setString(1, product.getName());
        ps.setLong(2, product.getPrice());
        ps.execute();
        ps = connection.prepareStatement("CALL IDENTITY()");
        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        product.setIdentifier(resultSet.getLong(1));
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE from PRODUCT WHERE IDENTIFIER = " + id);
        ps.execute();
    }
}
