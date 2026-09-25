package service;

import entity.*;

public class PricingService {

    private static final double BASE_DELIVERY_FEE = 15.0;
    private static final double EXTRA_FEE_PER_KM = 3.0;
    private static final double FREE_DISTANCE_KM = 3.0;
    private static final double SERVICE_FEE_RATE = 0.10;

    public double calculateTotal(Order order, Promotion promotion, double distance) {

        double subtotal = calculateSubtotal(order);

        double deliveryFee = calculateDeliveryFee(distance);

        deliveryFee = order.getCustomer().getDeliveryDiscount(deliveryFee);

        double serviceFee = Math.round(subtotal * SERVICE_FEE_RATE * 100.0) / 100.0;

        PromotionResult promotionResult = new PromotionResult(0.0, false);

        if (promotion != null) {

            PromotionContext context = new PromotionContext(subtotal, deliveryFee, order.getCustomer(), order.getAddress());

            promotionResult = promotion.apply(context);
        }

        double promotionDiscount = Math.min(promotionResult.subTotalDiscount(), subtotal);

        if (promotionResult.freeDelivery()) {
            deliveryFee = 0.0;
        }

        double total = subtotal + deliveryFee + serviceFee - promotionDiscount;

        return Math.max(Math.round(total * 100.0) / 100.0, 0.0);
    }

    private double calculateSubtotal(Order order) {

        return order.getItems().entrySet().stream().mapToDouble(entry -> entry.getKey().countPrice(entry.getValue())).sum();
    }

    private double calculateDeliveryFee(double distance) {
        if (distance <= FREE_DISTANCE_KM) {
            return BASE_DELIVERY_FEE;
        }

        return BASE_DELIVERY_FEE + (distance - FREE_DISTANCE_KM) * EXTRA_FEE_PER_KM;
    }
}
