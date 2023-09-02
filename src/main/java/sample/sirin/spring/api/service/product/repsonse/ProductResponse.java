package sample.sirin.spring.api.service.product.repsonse;

import lombok.Builder;
import lombok.Getter;
import sample.sirin.spring.domain.product.Product;
import sample.sirin.spring.domain.product.ProductType;
import sample.sirin.spring.domain.product.SellingStatus;

import java.util.List;

@Getter
public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductType type;
    private SellingStatus sellingStatus;
    private String name;
    private int price;

    @Builder
    private ProductResponse(Long id, String productNumber, ProductType type, SellingStatus sellingStatus, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .productNumber(product.getProductNumber())
            .type(product.getType())
            .sellingStatus(product.getSellingStatus())
            .name(product.getName())
            .price(product.getPrice())
            .build();
    }

}
