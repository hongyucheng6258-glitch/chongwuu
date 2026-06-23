package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.CartRequest;
import com.petshop.entity.CartItem;
import com.petshop.entity.Product;
import com.petshop.repository.CartItemRepository;
import com.petshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final MemberDiscountService memberDiscountService;

    /**
     * 查询用户购物车 (带商品信息，含会员折扣价)
     */
    public List<CartItem> list(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        Integer memberLevel = com.petshop.security.CurrentUser.getMemberLevel();
        items.forEach(item -> {
            if (item.getProduct() != null) {
                // 触发懒加载
                item.getProduct().getName();
                // 注入会员折扣价格
                Product p = item.getProduct();
                if (p.getPrice() != null) {
                    p.setDiscountPrice(memberDiscountService.calculateDiscountPrice(p.getPrice(), memberLevel));
                    p.setDiscountRate(memberDiscountService.getDiscountRate(memberLevel));
                }
            }
        });
        return items;
    }

    @Transactional
    public CartItem add(Long userId, CartRequest req) {
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new BusinessException("商品不存在"));
        if (product.getStatus() == 0) {
            throw new BusinessException("商品已下架");
        }
        if (product.getStock() < req.getQuantity()) {
            throw new BusinessException("库存不足");
        }

        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, req.getProductId())
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setUserId(userId);
                    ci.setProductId(req.getProductId());
                    ci.setQuantity(0);
                    return ci;
                });
        item.setQuantity(item.getQuantity() + req.getQuantity());
        return cartItemRepository.save(item);
    }

    @Transactional
    public CartItem updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException("购物车项不存在"));
        if (!item.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作他人购物车");
        }
        if (quantity <= 0) {
            cartItemRepository.delete(item);
            return null;
        }
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void remove(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException("购物车项不存在"));
        if (!item.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作他人购物车");
        }
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clear(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
