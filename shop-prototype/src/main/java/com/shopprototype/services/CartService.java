package com.shopprototype.services;

import com.shopprototype.domain.Cart;
import com.shopprototype.domain.Product;
import com.shopprototype.domain.AuxProductCart;
import com.shopprototype.domain.User;
import com.shopprototype.forms.CartForm;
import com.shopprototype.forms.FinishBuyForm;
import com.shopprototype.forms.ProductCartForm;
import com.shopprototype.repositories.AuxProductCartRepository;
import com.shopprototype.repositories.CartRepository;
import com.shopprototype.repositories.ProductRepository;
import com.shopprototype.repositories.UserRepository;
import com.shopprototype.services.exceptions.ObjectNotFoundException;
import com.shopprototype.services.exceptions.ServiceException;
import com.shopprototype.views.CartMessage;
import com.shopprototype.views.CartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
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

    @Autowired
    AuxProductCartRepository auxProductCartRepository;

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
        //TODO: Carrinho é vinculado ao token de autorização, acrescentar.
        //TODO: Validar se id e quantidade da lista de produtos são nulos, não permitir

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
        List<AuxProductCart> auxProductCarts = new ArrayList<>();

        for (ProductCartForm productId : productsIds) {
            Optional<Product> obj = productRepository.findById(productId.getProductId());

            if (obj.isPresent()) {
                products.add(obj.get());

                AuxProductCart auxProductCartAux = new AuxProductCart();
                auxProductCartAux.setProductId(obj.get().getId());
                auxProductCartAux.setPrice(obj.get().getPrice());
                auxProductCartAux.setQuantity(productId.getQuantity());
                auxProductCarts.add(auxProductCartAux);

                auxProductCartRepository.save(auxProductCartAux);


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
        cart.setAuxProductCarts(auxProductCarts);
        cartRepository.save(cart);

        for (Product product : products) {
            List<Cart> cartList = new ArrayList<>();
            cartList.add(cart);
            product.setCarts(cartList);
            //productRepository.save(product);
        }

        for (AuxProductCart auxProductCart : auxProductCarts) {
            auxProductCart.setCart(cart);
        }

        URI uri = builder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(new CartMessage("Cadastro realizado com sucesso", cart.getId()));
    }

    public ResponseEntity<CartMessage> deleteCart(FinishBuyForm finishBuyForm) throws ServiceException {
        Optional<User> user = userRepository.findById(finishBuyForm.getUserId());

        if (user.isPresent()) {
            Cart obj = cartRepository.findByUserId(user.get().getId());

            if (obj != null) {
                cartRepository.delete(obj);
                return new ResponseEntity<CartMessage>(new CartMessage("Compra finalizada com sucesso", obj.getId()), HttpStatus.OK);
            } else {
                throw new ServiceException("Não há carrinho cadastrado para o usuário");
            }
        } else {
            throw new ObjectNotFoundException("Usuário não encontrado");
        }
    }

    public ResponseEntity<CartMessage> cancelCart(FinishBuyForm finishBuyForm) throws ServiceException {
        Optional<User> user = userRepository.findById(finishBuyForm.getUserId());

        if (user.isPresent()) {
            Cart obj = cartRepository.findByUserId(user.get().getId());

            if (obj != null) {
                List<Product> products = new ArrayList<>();
                products.addAll(obj.getProducts());
                for(Product product : products){
                    Optional<Product> productEntity = productRepository.findById(product.getId());
                    productEntity.get().setQuantity(productEntity.get().getQuantity() + product.getQuantity());
                }
                cartRepository.delete(obj);
                return new ResponseEntity<CartMessage>(new CartMessage("Compra cancelada com sucesso", obj.getId()), HttpStatus.OK);
            } else {
                throw new ServiceException("Não há carrinho cadastrado para o usuário");
            }
        } else {
            throw new ObjectNotFoundException("Usuário não encontrado");
        }
    }
}
