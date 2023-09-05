package sample.sirin.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StockTest {

    @DisplayName("재고가 부족한지 체크한다.")
    @Test
    void isQuantityLessThan() {
        // given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        // when & then
        assertThat(stock.isQuantityLessThan(quantity)).isEqualTo(true);
    }

    @DisplayName("재고를 주어진 개수만큼 차감 할 수 있다.")
    @Test
    void deductQuantity() {
        // given
        Stock stock = Stock.create("001", 1);
        int quantity = 1;

        // when
        stock.deductQuantity(quantity);

        // then
        assertThat(stock.getQuantity()).isZero();
    }

    @DisplayName("재고보다 많은 수량으로 차감 시도하면 예외가 발생한다.")
    @Test
    void deductOverQuantity() {
        // given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        // when & then
        assertThatThrownBy(() -> stock.deductQuantity(quantity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("재고가 부족합니다.");
    }

}