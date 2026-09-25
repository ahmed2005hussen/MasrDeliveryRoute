package entity;

public class PromotionPrecentge implements Promotion {

    private final double precentge;
    private final double maxDiscount;

    public PromotionPrecentge(double precentge, double maxDiscount) {
        this.precentge = precentge;
        this.maxDiscount = maxDiscount;
    }

    @Override
    public PromotionResult apply(PromotionContext context) {

        double discount = context.subTotal() * precentge;

        discount = Math.min(discount, maxDiscount);

        return new PromotionResult(discount, false);
    }
}
