package service;

import entity.*;

import java.util.*;

public class ResturantService {

    public List<Restaurant> restaurantList = new ArrayList<>();
    public List<Order> orders = new ArrayList<>();

    private Map<String, Promotion> promotionsByCode = new HashMap<>();

    private Scanner sc = new Scanner(System.in);

    public void receiveOrder(Order order) {
        orders.add(order);
    }

    public Restaurant findById(String restId) {
        for (Restaurant r : restaurantList) {
            if (r.getRestaurantId().equals(restId)) return r;
        }
        return null;
    }

    public void addPromotion(String code, Promotion promotion) {
        promotionsByCode.put(code.toUpperCase(), promotion);
    }

    public Promotion findPromotion(String code) {
        return promotionsByCode.get(code.toUpperCase());
    }

    public void acceptPendingOrder() {
        Order order = pickOrder(OrderState.PLACED);
        if (order == null) return;

        try {
            order.nextOp(OrderState.ACCEPTED);
            System.out.println("Order accepted");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void rejectPendingOrder() {
        Order order = pickOrder(OrderState.PLACED);
        if (order == null) return;

        try {
            order.nextOp(OrderState.CANCELLED);
            System.out.println("Order rejected");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void markOrderPreparing() {
        Order order = pickOrder(OrderState.ACCEPTED);
        if (order == null) return;

        try {
            order.nextOp(OrderState.PREPARING);
            System.out.println("Order is preparing");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void markOrderReady() {
        Order order = pickOrder(OrderState.PREPARING);
        if (order == null) return;

        try {
            order.nextOp(OrderState.READY);
            System.out.println("Order is ready");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void toggleItemAvailability() {
        System.out.print("Enter restaurant id: ");
        String restId = sc.nextLine();
        Restaurant restaurant = findById(restId);
        if (restaurant == null) {
            System.out.println("Restaurant does not exist");
            return;
        }

        restaurant.getMenu().displayMenu();
        System.out.print("Enter item number: ");
        int idx = readInt();

        List<MenuItem> items = restaurant.getMenu().getMenuItemList();
        if (idx < 1 || idx > items.size()) {
            System.out.println("Wrong input");
            return;
        }

        MenuItem item = items.get(idx - 1);
        item.setAvailable(!item.isAvailable());
        System.out.println(item.getName() + " availability set to " + item.isAvailable());
    }

    public void addMenuItem() {
        System.out.print("Enter restaurant id: ");
        String restId = sc.nextLine();
        Restaurant restaurant = findById(restId);
        if (restaurant == null) {
            System.out.println("Restaurant does not exist");
            return;
        }

        System.out.print("Enter item name: ");
        String name = sc.nextLine();
        System.out.print("Enter price: ");
        double price = readDouble();
        System.out.print("Enter category: ");
        String category = sc.nextLine();
        System.out.print("Enter preparation time (minutes): ");
        int prepTime = readInt();
        System.out.print("Type (1-standard, 2-weighted): ");
        int type = readInt();

        MenuItem item;
        if (type == 2) {
            item = new WeightedItem(name, price, category, prepTime, true);
        } else {
            item = new StandardItem(name, price, category, prepTime, true);
        }

        restaurant.getMenu().addItem(item);
        System.out.println("Item added: " + item.getName());
    }

    public void removeMenuItem() {
        System.out.print("Enter restaurant id: ");
        String restId = sc.nextLine();
        Restaurant restaurant = findById(restId);
        if (restaurant == null) {
            System.out.println("Restaurant does not exist");
            return;
        }

        restaurant.getMenu().displayMenu();
        System.out.print("Enter item number to remove: ");
        int idx = readInt();

        List<MenuItem> items = restaurant.getMenu().getMenuItemList();
        if (idx < 1 || idx > items.size()) {
            System.out.println("Wrong input");
            return;
        }

        MenuItem item = items.get(idx - 1);
        restaurant.getMenu().removeItem(item);
        System.out.println("Item removed: " + item.getName());
    }

    public void AdjustDailyStock() {
        System.out.println("Stock tracking is not implemented yet");
    }

    public void viewTodayOrdersAndRevenue() {
        System.out.print("Enter restaurant id: ");
        String restId = sc.nextLine();

        List<Order> todayOrders = orders.stream()
                .filter(o -> o.getRestaurant().getRestaurantId().equals(restId))
                .filter(o -> o.getLocalDate().toLocalDate().equals(java.time.LocalDate.now()))
                .toList();

        double revenue = todayOrders.stream().mapToDouble(Order::getTotalPrice).sum();

        System.out.println("Today's orders: " + todayOrders.size());
        System.out.println("Today's revenue: " + revenue + " EGP");
    }

    public void viewResturants() {
        if (restaurantList.isEmpty()) {
            System.out.println("We Don't have any resturants yet");
            return;
        }

        System.out.println("Resturants: ");
        restaurantList.stream()
                .filter(Restaurant::isOpen)
                .sorted(
                        Comparator.comparingDouble(Restaurant::getAverageRating)
                                .reversed()
                                .thenComparing(Restaurant::getRestaurantName)
                )
                .forEach(System.out::println);
    }

    public void viewMenu(String restId) {
        Restaurant restaurant = findById(restId);

        if (restaurant == null) {
            System.out.println("resturant does not exsit");
            return;
        }

        restaurant.getMenu().displayMenu();
    }

    private Order pickOrder(OrderState expectedState) {
        List<Order> matching = orders.stream()
                .filter(o -> o.getOrderState() == expectedState)
                .toList();

        if (matching.isEmpty()) {
            System.out.println("No orders in state " + expectedState);
            return null;
        }

        System.out.println("Orders:");
        for (int i = 0; i < matching.size(); i++) {
            System.out.println((i + 1) + "- " + matching.get(i).getOrderId());
        }

        System.out.print("Choose order number: ");
        int choice = readInt();

        if (choice < 1 || choice > matching.size()) {
            System.out.println("Wrong input");
            return null;
        }

        return matching.get(choice - 1);
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