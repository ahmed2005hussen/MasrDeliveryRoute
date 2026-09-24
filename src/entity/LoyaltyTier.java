package entity;

public enum LoyaltyTier {

    BRONZE(0),
    SILVER(0.10),
    GOLD(1.0);

    private final double discount;

    LoyaltyTier(double discount) {
        this.discount = discount;
    }

    public double getDiscount(){
        return discount;
    }
}
