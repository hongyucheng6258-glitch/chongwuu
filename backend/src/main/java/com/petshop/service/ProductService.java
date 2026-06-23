package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.ProductRequest;
import com.petshop.entity.Product;
import com.petshop.repository.CategoryRepository;
import com.petshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberDiscountService memberDiscountService;

    /**
     * 商品列表 (前台 - 只显示上架商品)
     */
    public Page<Product> listForUser(Long categoryId, String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> result;
        if (keyword != null && !keyword.isBlank()) {
            result = productRepository.search(1, keyword.trim(), pageable);
        } else if (categoryId != null) {
            result = productRepository.findByStatusAndCategoryId(1, categoryId, pageable);
        } else {
            result = productRepository.findByStatus(1, pageable);
        }
        applyDiscountPrice(result);
        return result;
    }

    /**
     * 商品列表 (后台 - 显示全部)
     */
    public Page<Product> listForAdmin(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return productRepository.findAll(pageable);
    }

    public Product detail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("商品不存在"));
        // 注入会员折扣价格
        applyDiscountPrice(product);
        return product;
    }

    /**
     * 为商品注入会员折扣价格
     */
    private void applyDiscountPrice(Product product) {
        if (product != null && product.getPrice() != null) {
            Integer memberLevel = com.petshop.security.CurrentUser.getMemberLevel();
            product.setDiscountPrice(memberDiscountService.calculateDiscountPrice(product.getPrice(), memberLevel));
            product.setDiscountRate(memberDiscountService.getDiscountRate(memberLevel));
        }
    }

    /**
     * 为商品列表注入会员折扣价格
     */
    private void applyDiscountPrice(org.springframework.data.domain.Page<Product> page) {
        page.getContent().forEach(this::applyDiscountPrice);
    }

    @Transactional
    public Product create(ProductRequest req) {
        Product product = new Product();
        applyRequest(product, req);
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, ProductRequest req) {
        Product product = detail(id);
        applyRequest(product, req);
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    private void applyRequest(Product product, ProductRequest req) {
        if (req.getName() == null || req.getName().isBlank()) {
            throw new BusinessException("商品名称不能为空");
        }
        if (req.getCategoryId() != null) {
            categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new BusinessException("分类不存在"));
        }
        product.setName(req.getName());
        product.setCategoryId(req.getCategoryId());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setDescription(req.getDescription());
        product.setImage(req.getImage());
        if (req.getStatus() != null) {
            product.setStatus(req.getStatus());
        }
    }

    /**
     * 扣减库存
     */
    @Transactional
    public void reduceStock(Long productId, int quantity) {
        Product product = detail(productId);
        if (product.getStock() < quantity) {
            throw new BusinessException("商品【" + product.getName() + "】库存不足");
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
