package sample.sirin.spring.domain.product;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public enum SellingStatus {

    SELLING("판매중"),
    HOLD("판매 보류"),
    STOP_SELLING("판매중지");

    private final String text;

    public static List<SellingStatus> forDisplay() {
        return List.of(SELLING, HOLD);
    }
}
