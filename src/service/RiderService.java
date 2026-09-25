package service;

import entity.*;

import java.util.List;
import java.util.ArrayList;


public class RiderService {

    public List<Rider> riderList = new ArrayList<>();

    private ResturantService restaurantService;
    private Rider currentRider;

    public RiderService(ResturantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public void setCurrentRider(Rider rider) {
        this.currentRider = rider;
    }

    public void goOnDuty() {
        if (currentRider == null) {
            System.out.println("No rider logged in");
            return;
        }
        currentRider.setAvailable(true);
        System.out.println(currentRider.getRiderName() + " is now on duty");
    }

    public void goOffDuty() {
        if (currentRider == null) {
            System.out.println("No rider logged in");
            return;
        }
        currentRider.setAvailable(false);
        System.out.println(currentRider.getRiderName() + " is now off duty");
    }

    public void viewAssignedOrder() {
        if (currentRider == null) {
            System.out.println("No rider logged in");
            return;
        }

        try {
            System.out.println(currentRider.getActiveOrder());
        } catch (NullPointerException e) {
            System.out.println("No active order assigned");
        }
    }

    public void assignReadyOrders() {
        List<Order> readyOrders = restaurantService.orders.stream()
                .filter(o -> o.getOrderState() == OrderState.READY)
                .sorted((a, b) -> {
                    boolean aGold = a.getCustomer().getLoyaltyTier() == LoyaltyTier.GOLD;
                    boolean bGold = b.getCustomer().getLoyaltyTier() == LoyaltyTier.GOLD;
                    if (aGold != bGold) return aGold ? -1 : 1;
                    return a.getLocalDate().compareTo(b.getLocalDate());
                })
                .toList();

        for (Order order : readyOrders) {
            Rider available = riderList.stream()
                    .filter(Rider::isAvailable)
                    .findFirst()
                    .orElse(null);

            if (available == null) {
                System.out.println("No available riders");
                return;
            }

            available.assignOrder(order);
            order.assignRider(available);
            order.nextOp(OrderState.ASSIGNED);
            System.out.println("Order " + order.getOrderId() + " assigned to " + available.getRiderName());
        }
    }

    public void markOrderPickedUp() {
        if (currentRider == null) {
            System.out.println("No rider logged in");
            return;
        }

        try {
            Order order = currentRider.getActiveOrder();
            order.nextOp(OrderState.OUT_FOR_DELIVERY);
            System.out.println("Order picked up");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void markOrderDelivered() {
        if (currentRider == null) {
            System.out.println("No rider logged in");
            return;
        }

        try {
            Order order = currentRider.getActiveOrder();
            order.nextOp(OrderState.DELIVERED);
            order.getCustomer().addCompletedOrder();
            currentRider.completeDelivery();
            System.out.println("Order delivered");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewDeliveryStatistics() {
        if (currentRider == null) {
            System.out.println("No rider logged in");
            return;
        }

        System.out.println("Completed deliveries: " + currentRider.getCompletedDeliveries());
    }
}