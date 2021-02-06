package com.shopprototype.resources;

import com.shopprototype.services.ProductService;
import com.shopprototype.services.UserService;
import com.shopprototype.views.ProductView;
import com.shopprototype.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductView>> getProduct(@RequestParam(name = "id", required = false) Integer id,
                                                        @RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "price", required = false) String price,
                                                        @RequestParam(name = "description", required = false) String description,
                                                        @RequestParam(name = "quantity", required = false) Integer quantity,
                                                        Pageable pageable){
        return productService.getProduct(id, name, price, description, quantity, pageable);
    }
}
