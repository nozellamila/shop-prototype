package com.shopprototype.services;

import com.shopprototype.domain.Product;
import com.shopprototype.domain.User;
import com.shopprototype.form.ProductForm;
import com.shopprototype.repositories.ProductRepository;
import com.shopprototype.services.exceptions.ObjectNotFoundException;
import com.shopprototype.services.exceptions.ServiceException;
import com.shopprototype.views.ProductMessage;
import com.shopprototype.views.ProductView;
import com.shopprototype.views.UserMessage;
import com.shopprototype.views.UserView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

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

    public ResponseEntity<ProductView> postProduct(ProductForm productForm, UriComponentsBuilder builder) throws ServiceException {
        Product product = productRepository.findByName(productForm.getName());

        if(product != null)
            throw new ServiceException("Produto já cadastrado com o nome: " + product.getName());
        else {
            if(productForm.getQuantity() < 0)
                throw new ServiceException("Produto não pode ter quantidade negativa");

            ModelMapper modelMapper = new ModelMapper();
            product = modelMapper.map(productForm, Product.class);

            productRepository.save(product);

            URI uri = builder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
            return ResponseEntity.created(uri).body(new ProductView(product));
        }
    }

    public ResponseEntity<ProductMessage> putProduct(Integer id, ProductForm productForm) throws ServiceException {
        Optional<Product> product = productRepository.findById(id);
        Product productByName = productRepository.findByName(productForm.getName());
        
        if(!product.isPresent())
            throw new ObjectNotFoundException("Produto não encontrado");
        if(product.isPresent() && productByName != null){
           if(product.get().getId() != productByName.getId())
               throw new ServiceException("Produto já cadastrado com o nome: " + productByName.getName());
        }
        if(product.isPresent()){
            if(productForm.getQuantity() < 0)
                throw new ServiceException("Produto não pode ter quantidade negativa");
            Product productFound = product.get();
            productFound.setName(productForm.getName());
            productFound.setPrice(productForm.getPrice());
            productFound.setDescription(productForm.getDescription());
            productFound.setQuantity(productForm.getQuantity());

        }
        return ResponseEntity.ok(new ProductMessage("Produto atualizado com sucesso"));
    }

    public ResponseEntity<ProductMessage> deleteProduct(Integer id) throws ServiceException {
        Optional<Product> product = productRepository.findById(id);

        if(!product.isPresent())
            return ResponseEntity.ok(new ProductMessage("Nenhum produto excluído"));
        else {
            if(!product.get().getCarts().isEmpty())
                throw new ServiceException("Não é possível excluir produto pertencente a algum carrinho");
            else{
                productRepository.deleteById(product.get().getId());
                return ResponseEntity.ok(new ProductMessage("Produto excluído com sucesso"));
            }

        }
    }
}
