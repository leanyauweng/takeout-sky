package com.itheima.service;

import com.itheima.pojo.dto.ShoppingCartDTO;
import com.itheima.pojo.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void addShopppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> showShoppingCart();

    void cleanShoppingCart();

    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
