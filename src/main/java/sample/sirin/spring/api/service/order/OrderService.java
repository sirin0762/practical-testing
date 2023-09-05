package sample.sirin.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.sirin.spring.api.controller.order.request.OrderCreateRequest;
import sample.sirin.spring.api.service.order.response.OrderResponse;
import sample.sirin.spring.domain.order.Order;
import sample.sirin.spring.domain.order.OrderRepository;
import sample.sirin.spring.domain.product.Product;
import sample.sirin.spring.domain.product.ProductRepository;
import sample.sirin.spring.domain.product.ProductType;
import sample.sirin.spring.domain.stock.Stock;
import sample.sirin.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = findProductsBy(productNumbers);

        // 제고 관련 상품 조회
        List<String> stockProductNumbers = products.stream()
            .filter(product -> ProductType.containsStockType(product.getType()))
            .map(Product::getProductNumber)
            .toList();

        // 제고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
            .collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));

        // 상품 갯수 counting
        Map<String, Long> productCounting = stockProductNumbers.stream()
            .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        // 제고 갯수 차감
        for (String stockProductNumber : stockMap.keySet()) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCounting.get(stockProductNumber).intValue();
            if (stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }
        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
            .map(productMap::get)
            .toList();
    }

}
