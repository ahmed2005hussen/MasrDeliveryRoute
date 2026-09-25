package entity;

public class FixedPromotion implements Promotion {
    private final double amount;

    public FixedPromotion(double amount) {
        this.amount = amount;
    }


    @Override
    public PromotionResult apply(PromotionContext context) {

        return new PromotionResult(
                Math.min(amount, context.subTotal()), false
        );
    }
}
