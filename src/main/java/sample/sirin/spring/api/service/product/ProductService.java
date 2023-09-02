package sample.sirin.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.sirin.spring.api.service.product.repsonse.ProductResponse;
import sample.sirin.spring.domain.product.Product;
import sample.sirin.spring.domain.product.ProductRepository;
import sample.sirin.spring.domain.product.SellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(SellingStatus.forDisplay());

        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

}
