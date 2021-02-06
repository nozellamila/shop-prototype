package com.shopprototype.services;

import com.shopprototype.domain.Product;
import com.shopprototype.domain.User;
import com.shopprototype.repositories.ProductRepository;
import com.shopprototype.services.exceptions.ObjectNotFoundException;
import com.shopprototype.views.ProductView;
import com.shopprototype.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ResponseEntity<Page<ProductView>> getProduct(Integer id, String name, String price, String description, Integer quantity, Pageable pageable) {
        Page<Product> products = productRepository.findByParameters(id, name, price, description, quantity, pageable);

        if(products != null && !products.isEmpty()){
            return new ResponseEntity<Page<ProductView>>(products.map(ProductView::new), HttpStatus.OK);
        }else {
            throw new ObjectNotFoundException("Produto não encontrado");
        }
    }
}
