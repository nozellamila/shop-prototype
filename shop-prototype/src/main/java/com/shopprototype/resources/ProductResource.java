package com.shopprototype.resources;

import com.shopprototype.form.ProductForm;
import com.shopprototype.form.UserForm;
import com.shopprototype.services.ProductService;
import com.shopprototype.services.UserService;
import com.shopprototype.services.exceptions.ServiceException;
import com.shopprototype.views.ProductMessage;
import com.shopprototype.views.ProductView;
import com.shopprototype.views.UserMessage;
import com.shopprototype.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

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


    @Transactional
    @PostMapping
    public ResponseEntity<ProductView> postProduct(@RequestBody @Valid ProductForm productForm, UriComponentsBuilder builder) throws ServiceException {
        return productService.postProduct(productForm, builder);
    }

    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductMessage> putProduct(@PathVariable Integer id, @RequestBody @Valid ProductForm productForm) throws ServiceException {
        return productService.putProduct(id, productForm);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductMessage> deleteProduct(@PathVariable Integer id) throws ServiceException {
        return productService.deleteProduct(id);
    }

}
