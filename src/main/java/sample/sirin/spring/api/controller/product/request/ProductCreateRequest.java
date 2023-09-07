package sample.sirin.spring.api.controller.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.sirin.spring.domain.product.Product;
import sample.sirin.spring.domain.product.ProductType;
import sample.sirin.spring.domain.product.SellingStatus;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    private ProductType type;
    private SellingStatus sellingStatus;
    private String name;
    private int price;

    @Builder
    private ProductCreateRequest(ProductType type, SellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String productNumber) {
        return Product.builder()
            .productNumber(productNumber)
            .type(this.type)
            .sellingStatus(this.sellingStatus)
            .name(this.name)
            .price(this.price)
            .build();
    }

}
