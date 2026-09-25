package service;

import entity.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class CustomerService {
    public Customer customer;

    public ResturantService restaurantService;
    public RiderService riderService;

    public List<Order> orders = new ArrayList<>();

    private PricingService pricingService = new PricingService();
    private Scanner sc = new Scanner(System.in);

    public CustomerService(Customer customer, ResturantService restaurantService, RiderService riderService) {
        this.customer = customer;
        this.restaurantService = restaurantService;
        this.riderService = riderService;
    }

    public void browseResturant() {
        restaurantService.viewResturants();
    }

    public void viewMenu() {
        System.out.print("Enter restaurant id: ");
        String restId = sc.nextLine();
        restaurantService.viewMenu(restId);
    }

    public void placeOrder() {
        System.out.print("Enter restaurant id: ");
        String restId = sc.nextLine();

        Restaurant restaurant = restaurantService.findById(restId);
        if (restaurant == null) {
            System.out.println("Restaurant does not exist");
            return;
        }

        if (!restaurant.isOpen()) {
            System.out.println("Restaurant is currently closed");
            return;
        }

        Map<MenuItem, Double> items = new HashMap<>();

        while (true) {
            restaurant.getMenu().displayMenu();
            System.out.print("Enter item number to add (0 to finish): ");
            int itemChoice = readInt();

            if (itemChoice == 0) break;

            List<MenuItem> menuItemList = restaurant.getMenu().getMenuItemList();

            if (itemChoice < 1 || itemChoice > menuItemList.size()) {
                System.out.println("Wrong input, try again");
                continue;
            }

            MenuItem chosen = menuItemList.get(itemChoice - 1);

            if (!chosen.isAvailable()) {
                System.out.println("Item is unavailable: " + chosen.getName());
                continue;
            }

            System.out.print("Enter quantity: ");
            double qty = readDouble();

            if (qty <= 0) {
                System.out.println("Quantity must be greater than zero");
                continue;
            }

            items.merge(chosen, qty, Double::sum);
        }

        if (items.isEmpty()) {
            System.out.println("Order must contain at least one item");
            return;
        }

        if (customer.getAddressList().isEmpty()) {
            System.out.println("You have no saved address, add one first");
            return;
        }

        System.out.println("Choose delivery address:");
        List<Address> addresses = customer.getAddressList();
        for (int i = 0; i < addresses.size(); i++) {
            System.out.println((i + 1) + "- " + addresses.get(i));
        }

        System.out.print("Enter address number: ");
        int addrChoice = readInt();

        if (addrChoice < 1 || addrChoice > addresses.size()) {
            System.out.println("Wrong address choice");
            return;
        }

        Address address = addresses.get(addrChoice - 1);

        Order order = new Order(customer, restaurant, address);
        for (Map.Entry<MenuItem, Double> entry : items.entrySet()) {
            order.addItem(entry.getKey(), entry.getValue());
        }

        System.out.print("Enter promo code (or leave empty): ");
        String promoCode = sc.nextLine();
        Promotion promotion = null;
        if (!promoCode.isBlank()) {
            promotion = restaurantService.findPromotion(promoCode);
            if (promotion == null) {
                System.out.println("Promo code not found, ignoring promo");
            }
        }

        double distance = distanceBetween(
                restaurant.getAddress().isEmpty() ? null : restaurant.getAddress().get(0).getDistrict(),
                address.getDistrict()
        );

        double total = pricingService.calculateTotal(order, promotion, distance);
        order.setTotalPrice(total);

        orders.add(order);
        restaurantService.receiveOrder(order);

        System.out.println("Order placed successfully.");
        System.out.println("Total price: " + total + " EGP");
        System.out.println(order);
    }

    // Not the real distance matrix required by the spec, just a rough
    // placeholder so pricing has something to work with.
    private double distanceBetween(District from, District to) {
        if (from == null || to == null) return 5.0;
        if (from == to) return 2.0;
        return 8.0;
    }

    public void payFromWallet() {
        System.out.print("Enter order id: ");
        String orderId = sc.nextLine();

        Order order = findOrder(orderId);
        if (order == null) {
            System.out.println("Order does not exist");
            return;
        }

        try {
            customer.withdrawalMoney(order.getTotalPrice());
            System.out.println("Paid. New balance: " + customer.getWalletBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Insufficient funds: " + e.getMessage());
        }
    }

    public void TrackOrder() {
        System.out.print("Enter order id: ");
        String orderId = sc.nextLine();

        Order order = findOrder(orderId);
        if (order == null) {
            System.out.println("Order does not exist");
            return;
        }

        Duration elapsed = Duration.between(order.getLocalDate(), LocalDateTime.now());
        System.out.println("Status: " + order.getOrderState());
        System.out.println("Elapsed time since placement: " + elapsed.toMinutes() + " minutes");
    }

    public void cancleOrder() {
        System.out.print("Enter order id: ");
        String orderId = sc.nextLine();

        Order order = findOrder(orderId);
        if (order == null) {
            System.out.println("Order does not exist");
            return;
        }

        try {
            order.nextOp(OrderState.CANCELLED);
            if (order.getTotalPrice() > 0) {
                customer.addBalance(order.getTotalPrice());
            }
            System.out.println("Order cancelled and refunded: " + order.getTotalPrice() + " EGP");
        } catch (IllegalStateException e) {
            System.out.println("Cannot cancel this order: " + e.getMessage());
        }
    }

    public void orderHistory() {
        if (orders.isEmpty()) {
            System.out.println("No orders yet");
            return;
        }

        List<Order> sorted = new ArrayList<>(orders);
        sorted.sort(Comparator.comparing(Order::getLocalDate).reversed());

        double totalSpent = 0;
        for (Order o : sorted) {
            System.out.println(o);
            totalSpent += o.getTotalPrice();
        }

        System.out.println("Lifetime total spent: " + totalSpent + " EGP");
    }

    private Order findOrder(String orderId) {
        for (Order o : orders) {
            if (o.getOrderId().equals(orderId)) return o;
        }
        return null;
    }

    private int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Wrong input, enter a number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    private double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Wrong input, enter a number: ");
            sc.next();
        }
        double val = sc.nextDouble();
        sc.nextLine();
        return val;
    }
}