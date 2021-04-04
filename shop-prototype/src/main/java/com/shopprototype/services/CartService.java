package com.shopprototype.services;

import com.shopprototype.domain.Cart;
import com.shopprototype.domain.Product;
import com.shopprototype.domain.User;
import com.shopprototype.forms.CartForm;
import com.shopprototype.forms.ProductCartForm;
import com.shopprototype.repositories.CartRepository;
import com.shopprototype.repositories.ProductRepository;
import com.shopprototype.repositories.UserRepository;
import com.shopprototype.services.exceptions.ObjectNotFoundException;
import com.shopprototype.services.exceptions.ServiceException;
import com.shopprototype.views.CartMessage;
import com.shopprototype.views.CartView;
import com.shopprototype.views.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Page<CartView>> getCart(Integer id, Float totalPrice, Integer totalQuantity, Integer userId, Pageable pageable) {
        Page<Cart> carts = cartRepository.findByParameters(id, totalPrice, totalQuantity, userId, pageable);

        if(carts != null && !carts.isEmpty()){
            return new ResponseEntity<Page<CartView>>(carts.map(CartView::new), HttpStatus.OK);
        }else {
            throw new ObjectNotFoundException("Carrinho não encontrado");
        }
    }

    public ResponseEntity<CartView> getCartById(Integer id) {
        Optional<Cart> cart = cartRepository.findById(id);
        
        if(cart.isPresent()){
            Cart cart1 = cart.get();
            return new ResponseEntity<CartView>(new CartView(cart1), HttpStatus.OK);
        }
        else {
            throw new ObjectNotFoundException("Carrinho não encontrado");
        }
    }

    public ResponseEntity<CartMessage> postCart(CartForm cartForm, UriComponentsBuilder builder) throws ServiceException {
        //Carrinho é vinculado ao token de autorização, acrescentar.
        //É permitido somente um carrinho por vez para cada usuário
        Cart cart = new Cart();

        Optional<User> user = userRepository.findById(cartForm.getUserId());

        if (user.isPresent()){
            Cart obj = cartRepository.findByUserId(user.get().getId());

            if(obj == null)
                cart.setUser(user.get());
            else
                throw new ServiceException("Não é possível cadastrar dois carrinhos para o mesmo usuário");
        }else {
            throw new ObjectNotFoundException("Usuário não encontrado");
        }

        List<ProductCartForm> productsIds = cartForm.getProducts();
        List<Product> products = new ArrayList<>();

        for (ProductCartForm productId : productsIds) {
            Optional<Product> obj = productRepository.findById(productId.getProductId());

            if (obj.isPresent()) {
                products.add(obj.get());

                if (obj.get().getQuantity() >= productId.getQuantity()) {
                    Integer productQuantity = productId.getQuantity();
                    cart.setTotalQuantity((cart.getTotalQuantity() + productQuantity));
                    cart.setTotalPrice(cart.getTotalPrice() + (obj.get().getPrice()*productQuantity));

                    obj.get().setQuantity((obj.get().getQuantity() - productId.getQuantity()));
                    productRepository.save(obj.get());
                } else throw new ServiceException("Não é possível cadastrar quantidade maior que a disponível");
            } else {
                throw new ObjectNotFoundException("Produto não existe");
            }
        }
        cart.setProducts(products);
        cartRepository.save(cart);

        for (Product product : products) {
            List<Cart> cartList = new ArrayList<>();
            cartList.add(cart);
            product.setCarts(cartList);
            //productRepository.save(product);
        }

        URI uri = builder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(new CartMessage("Cadastro realizado com sucesso", cart.getId()));
        //Para salvar carrinho: usuário id, lista de produtos, preço total, quantidade total
    }
}
