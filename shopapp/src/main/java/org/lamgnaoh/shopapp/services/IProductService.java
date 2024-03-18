package org.lamgnaoh.shopapp.services;

import org.lamgnaoh.shopapp.dtos.ProductDTO;
import org.lamgnaoh.shopapp.dtos.ProductImageDTO;
import org.lamgnaoh.shopapp.models.*;
import org.lamgnaoh.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long id) throws Exception;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existsByName(String name);
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception;

}
