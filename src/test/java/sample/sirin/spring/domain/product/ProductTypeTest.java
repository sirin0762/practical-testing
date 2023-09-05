package sample.sirin.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static sample.sirin.spring.domain.product.ProductType.*;

class ProductTypeTest {
    
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    public void containsStockType() {
        // when
        boolean handmadeResult = ProductType.containsStockType(HANDMADE);
        boolean bottleResult = ProductType.containsStockType(BOTTLE);
        boolean bakeryResult = ProductType.containsStockType(BAKERY);

        // then
        assertThat(handmadeResult).isEqualTo(false);
        assertThat(bottleResult).isEqualTo(true);
        assertThat(bakeryResult).isEqualTo(true);
    }

}