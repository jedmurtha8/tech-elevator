package com.techelevator.controller;

import com.techelevator.dao.ProductDao;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    private ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getProductByNameOrSku(@RequestParam(required = false) String name, @RequestParam(required = false) String sku) {
        if (name == null && sku == null) {
            return productDao.getAll();
        }
       return productDao.find(name, sku);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product getProductById(@PathVariable int id) {
        Product product = productDao.getById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        } else {
            return product;
        }
    }
}
