package entity;

public record PromotionContext(
        double subTotal,
        double deleveryFees,
        Customer customer,
        Address address
) {
}
