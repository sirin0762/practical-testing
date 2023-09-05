package sample.sirin.spring.api.service.order;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.sirin.spring.api.controller.order.request.OrderCreateRequest;
import sample.sirin.spring.api.service.order.response.OrderResponse;
import sample.sirin.spring.domain.order.OrderRepository;
import sample.sirin.spring.domain.orderproduct.OrderProductRepository;
import sample.sirin.spring.domain.product.Product;
import sample.sirin.spring.domain.product.ProductRepository;
import sample.sirin.spring.domain.product.ProductType;
import sample.sirin.spring.domain.product.SellingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.sirin.spring.domain.product.ProductType.BAKERY;
import static sample.sirin.spring.domain.product.ProductType.HANDMADE;
import static sample.sirin.spring.domain.product.SellingStatus.HOLD;
import static sample.sirin.spring.domain.product.SellingStatus.SELLING;
import static sample.sirin.spring.domain.product.SellingStatus.STOP_SELLING;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @DisplayName("상품번호 리스트를 받아 주문을 생성할 수 있다..")
    @Test
    public void createOrder() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떄", 4500);
        Product product3 = createProduct("003", BAKERY, STOP_SELLING, "베이커리", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers((List.of("001", "002")))
            .build();

        // when
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response).extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 8500);
        assertThat(response.getProducts()).hasSize(2);
        assertThat(response.getProducts()).extracting("productNumber", "price")
            .contains(
                tuple("001", 4000),
                tuple("002", 4500)
            );

    }

    @DisplayName("중복되는 상품번호 리스트를 받아 주문을 생성할 수 있다.")
    @Test
    public void createOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떄", 4500);
        Product product3 = createProduct("003", BAKERY, STOP_SELLING, "베이커리", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers((List.of("001", "001")))
            .build();

        // when
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response).extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 8000);
        assertThat(response.getProducts()).hasSize(2);
        assertThat(response.getProducts()).extracting("productNumber", "price")
            .contains(
                tuple("001", 4000),
                tuple("001", 4000)
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