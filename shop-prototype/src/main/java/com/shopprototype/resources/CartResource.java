package com.shopprototype.resources;

import com.shopprototype.forms.CartForm;
import com.shopprototype.forms.FinishBuyForm;
import com.shopprototype.services.CartService;
import com.shopprototype.services.exceptions.ServiceException;
import com.shopprototype.views.CartMessage;
import com.shopprototype.views.CartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/carts")
public class CartResource {

    @Autowired
    CartService cartService;

    @GetMapping
    public ResponseEntity<Page<CartView>> getCart(@RequestParam(name = "id", required = false) Integer id,
                                                  @RequestParam(name = "totalPrice", required = false) Float totalPrice,
                                                  @RequestParam(name = "totalQuantity", required = false) Integer totalQuantity,
                                                  @RequestParam(name = "userId", required = false) Integer userId,
                                                  Pageable pageable){
        return cartService.getCart(id, totalPrice, totalQuantity, userId, pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CartView> getCartById(@PathVariable Integer id){
        return cartService.getCartById(id);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<CartMessage> postCart(@RequestBody @Valid CartForm cartForm, UriComponentsBuilder builder) throws ServiceException {
        return cartService.postCart(cartForm, builder);
    }

    @Transactional
    @DeleteMapping(value = "/finish-buy")
    public ResponseEntity<CartMessage> deleteCart(@RequestBody @Valid FinishBuyForm finishBuyForm) throws ServiceException {
        return cartService.deleteCart(finishBuyForm);
    }

    @Transactional
    @DeleteMapping(value = "/cancel-buy")
    public ResponseEntity<CartMessage> cancelCart(@RequestBody @Valid FinishBuyForm finishBuyForm) throws ServiceException {
        return cartService.cancelCart(finishBuyForm);
    }
}
