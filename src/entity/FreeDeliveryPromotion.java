package entity;

public class FreeDeliveryPromotion implements Promotion {

    @Override
    public PromotionResult apply(PromotionContext context) {
        return new PromotionResult(0.0, true);
    }
}
