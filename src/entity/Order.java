package entity;

import service.GenerateIdService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private GenerateIdService generateIdService = new GenerateIdService();
    private final String orderId;
    private final Customer customer;
    private final Restaurant restaurant;
    private final Address address;
    private Map<MenuItem, Double> items;
    private final LocalDateTime localDate;
    private OrderState orderState;
    private Rider rider;
    private double totalPrice;

    public Order(Customer customer, Restaurant restaurant, Address address) {
        if (customer == null) throw new IllegalArgumentException("WRONG: customer can not be null");
        if (restaurant == null) throw new IllegalArgumentException("WRONG: restaurant can not be null");
        if (address == null) throw new IllegalArgumentException("WRONG: address can not be null");

        this.orderId = generateIdService.generateId(customer.getCustomerName() + '-'
                + restaurant.getRestaurantName());
        this.customer = customer;
        this.restaurant = restaurant;
        this.address = address;
        items = new HashMap<>();
        this.localDate = LocalDateTime.now();
        this.orderState = OrderState.PLACED;
        this.totalPrice = 0.0;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Address getAddress() {
        return address;
    }

    public Map<MenuItem, Double> getItems() {
        return items;
    }

    public void addItem(MenuItem item, double quantity) {
        if (item == null) throw new IllegalArgumentException("WRONG: order can not be empty");
        if (quantity <= 0) throw new IllegalArgumentException("WRONG: quantity can not be negative or 0");

        items.merge(item, quantity, Double::sum);
    }

    public void assignRider(Rider rider) {
        if (rider == null) throw new IllegalArgumentException("WRONG: rider can not be null");
        this.rider = rider;
    }

    public void setItems(Map<MenuItem, Double> items) {
        this.items = items;
    }

    public LocalDateTime getLocalDate() {
        return localDate;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        if (totalPrice < 0) throw new IllegalArgumentException("WRONG: total price can not be negative");
        this.totalPrice = totalPrice;
    }

    public void nextOp(OrderState newState) {
        if (newState == null) throw new IllegalArgumentException("WRONG: order state cannot be null");
        if (!isValidTransition(newState))
            throw new IllegalStateException("WRONG: you can not transition from " +
                    orderState + " to " + newState);

        this.orderState = newState;
    }

    private boolean isValidTransition(OrderState newState) {
        if (orderState == OrderState.CANCELLED || orderState == OrderState.DELIVERED) {
            return false;
        }
        if (newState == OrderState.CANCELLED) {
            return orderState != OrderState.OUT_FOR_DELIVERY;
        }

        return switch (orderState) {
            case PLACED -> newState == OrderState.ACCEPTED;
            case ACCEPTED -> newState == OrderState.PREPARING;
            case PREPARING -> newState == OrderState.READY;
            case READY -> newState == OrderState.ASSIGNED;
            case ASSIGNED -> newState == OrderState.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> newState == OrderState.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }



    @Override
    public String toString() {
        return "\nOrder{" +
                "   \nOrder Id: " + orderId +
                "   \nCustomer: " + customer +
                "   \nRestaurant: " + restaurant +
                "   \nAddress: " + address +
                "   \nItems: " + items +
                "   \nTotal Price: " + totalPrice +
                "   \nOrder Date: " + localDate +
                "   \nOrder State: " + orderState +
                "   \nRider: " + rider + '\n';
    }
}