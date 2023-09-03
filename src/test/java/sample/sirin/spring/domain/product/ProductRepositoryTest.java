package sample.sirin.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.sirin.spring.domain.product.ProductType.*;
import static sample.sirin.spring.domain.product.SellingStatus.HOLD;
import static sample.sirin.spring.domain.product.SellingStatus.SELLING;
import static sample.sirin.spring.domain.product.SellingStatus.STOP_SELLING;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("판매상품 조회 시, 판매 중 상태와 판매 보류 상태의 상품을 보여준다.")
    @Test
    public void findAllBySellingStatusIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떄", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "베이커리", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, SellingStatus.HOLD));

        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", SELLING),
                tuple("002", "카페라떄", SellingStatus.HOLD)
            );

    }

    private static Product createProduct(String productNumber, ProductType productType, SellingStatus sellingStatus, String name, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .type(productType)
            .sellingStatus(sellingStatus)
            .name(name)
            .price(price)
            .build();
    }

}