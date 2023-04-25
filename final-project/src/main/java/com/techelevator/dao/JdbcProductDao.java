package com.techelevator.dao;

import java.util.ArrayList;
import java.util.List;

import com.techelevator.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcProductDao implements ProductDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM product ORDER BY product_id";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Product product = mapRowToProduct(results);
            list.add(product);
        }
        return list;
    }

    @Override
    public Product getById(int productId) {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, productId);
        if (results.next()) {
            return mapRowToProduct(results);
        } else {
            return null;
        }
    }

    @Override
    public List<Product> getProductsInUserCart(int userId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE product_id IN (SELECT product_id FROM cart_item WHERE user_id = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Product product = mapRowToProduct(results);
            list.add(product);
        }
        return list;
    }

    @Override
    public List<Product> getProductsInWishlist(int wishlistId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE product_id IN (SELECT product_id FROM wishlist_item WHERE wishlist_id = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, wishlistId);
        while (results.next()) {
            Product product = mapRowToProduct(results);
            list.add(product);
        }
        return list;
    }

    @Override
    public List<Product> find(String productSku, String name) {
        List<Product> list = new ArrayList<>();
        String nameLike = "%" + (name == null ? "" : name) + "%";

        boolean checkSku = productSku != null && productSku.trim().length() > 0;

        String sql =
                "SELECT * FROM product WHERE name ILIKE ? " +
                        (checkSku ? "AND product_sku = ? " : "") +
                        "ORDER BY product_id";
        SqlRowSet results;
        if (checkSku) {
            results = jdbcTemplate.queryForRowSet(sql, nameLike, productSku);
        } else {
            results = jdbcTemplate.queryForRowSet(sql, nameLike);
        }
        while (results.next()) {
            Product product = mapRowToProduct(results);
            list.add(product);
        }
        return list;
    }

    private Product mapRowToProduct(SqlRowSet rs) {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductSku(rs.getString("product_sku"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setImageName(rs.getString("image_name"));
        return product;
    }
}


