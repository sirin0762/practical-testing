package sample.sirin.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.sirin.spring.domain.product.Product;
import sample.sirin.spring.domain.product.ProductRepository;
import sample.sirin.spring.domain.product.ProductType;
import sample.sirin.spring.domain.product.SellingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.sirin.spring.domain.product.ProductType.HANDMADE;
import static sample.sirin.spring.domain.product.SellingStatus.HOLD;
import static sample.sirin.spring.domain.product.SellingStatus.SELLING;

@ActiveProfiles("test")
@SpringBootTest
class OrderTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문 생성 시, 주문상태는 INIT 이다")
    @Test
    public void init() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떄", 4500);
        List<Product> products = List.of(product1, product2);

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시, 주문 등록 시간을 기록한다.")
    @Test
    public void registeredDateTime() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떄", 4500);
        List<Product> products = List.of(product1, product2);

        // when
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    @DisplayName("주문 생성 시, 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    public void calculateTotalPrice() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떄", 4500);
        List<Product> products = List.of(product1, product2);

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(8500);
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