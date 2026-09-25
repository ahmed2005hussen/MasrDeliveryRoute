package service;

import entity.*;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class AdminService {

    private ResturantService restaurantService;
    private CustomerService customerService;
    private RiderService riderService;

    private Scanner sc = new Scanner(System.in);

    public AdminService(ResturantService restaurantService, CustomerService customerService, RiderService riderService) {
        this.restaurantService = restaurantService;
        this.customerService = customerService;
        this.riderService = riderService;
    }

    public void addRestaurant() {
        System.out.print("Enter restaurant name: ");
        String name = sc.nextLine();
        System.out.print("Enter district (MAADI, DOKKI, FAISAL, NASER_CITY, HELIOPOLIS): ");
        String districtStr = sc.nextLine();

        District district;
        try {
            district = District.valueOf(districtStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong district");
            return;
        }

        System.out.print("Enter address description: ");
        String desc = sc.nextLine();

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(district, desc));

        System.out.print("Enter cuisine (comma separated): ");
        String cuisineLine = sc.nextLine();
        Set<String> cuisines = new HashSet<>(Arrays.asList(cuisineLine.split(",")));

        Restaurant restaurant = new Restaurant(name, addresses, cuisines, 0.0, true, new Menu(new ArrayList<>()));
        restaurantService.restaurantList.add(restaurant);

        System.out.println("Restaurant added with id: " + restaurant.getRestaurantId());
    }

    public void removeRestaurant() {
        System.out.print("Enter restaurant id: ");
        String restId = sc.nextLine();

        Restaurant restaurant = restaurantService.findById(restId);
        if (restaurant == null) {
            System.out.println("Restaurant does not exist");
            return;
        }

        restaurantService.restaurantList.remove(restaurant);
        System.out.println("Restaurant removed");
    }

    public void createPromotion() {
        System.out.print("Enter promo code: ");
        String code = sc.nextLine();

        System.out.println("Type (1-percentage, 2-fixed, 3-free delivery): ");
        int type = readInt();

        Promotion promotion;
        if (type == 1) {
            System.out.print("Enter percentage (0-1): ");
            double pct = readDouble();
            System.out.print("Enter max discount: ");
            double max = readDouble();
            promotion = new PromotionPrecentge(pct, max);
        } else if (type == 2) {
            System.out.print("Enter fixed amount: ");
            double amount = readDouble();
            promotion = new FixedPromotion(amount);
        } else {
            promotion = new FreeDeliveryPromotion();
        }

        restaurantService.addPromotion(code, promotion);
        System.out.println("Promotion created: " + code);
    }

    public void runReports() {
        System.out.println("""
                1. Total revenue
                2. Top 5 restaurants by revenue this month
                3. Average order value per district
                4. Restaurants rating > 4.5 and >= 20 completed orders
                5. Order count by status
                6. Rider stats
                7. Most ordered item
                8. Peak ordering hour
                """);
        System.out.print("Choose report: ");
        int choice = readInt();

        List<Order> allOrders = customerService.orders;

        switch (choice) {
            case 1 -> {
                double revenue = allOrders.stream().mapToDouble(Order::getTotalPrice).sum();
                System.out.println("Total revenue: " + revenue + " EGP");
            }
            case 2 -> {
                YearMonth thisMonth = YearMonth.now();
                Map<Restaurant, Double> revenueByRestaurant = allOrders.stream()
                        .filter(o -> YearMonth.from(o.getLocalDate()).equals(thisMonth))
                        .collect(Collectors.groupingBy(Order::getRestaurant, Collectors.summingDouble(Order::getTotalPrice)));

                revenueByRestaurant.entrySet().stream()
                        .sorted(Map.Entry.<Restaurant, Double>comparingByValue().reversed())
                        .limit(5)
                        .forEach(e -> System.out.println(e.getKey().getRestaurantName() + ": " + e.getValue() + " EGP"));
            }
            case 3 -> {
                Map<District, Double> avgByDistrict = allOrders.stream()
                        .collect(Collectors.groupingBy(o -> o.getAddress().getDistrict(),
                                Collectors.averagingDouble(Order::getTotalPrice)));
                avgByDistrict.forEach((d, avg) -> System.out.println(d + ": " + avg + " EGP"));
            }
            case 4 -> {
                Map<Restaurant, Long> completedByRestaurant = allOrders.stream()
                        .filter(o -> o.getOrderState() == OrderState.DELIVERED)
                        .collect(Collectors.groupingBy(Order::getRestaurant, Collectors.counting()));

                restaurantService.restaurantList.stream()
                        .filter(r -> r.getAverageRating() > 4.5)
                        .filter(r -> completedByRestaurant.getOrDefault(r, 0L) >= 20)
                        .forEach(System.out::println);
            }
            case 5 -> {
                Map<OrderState, Long> countByStatus = allOrders.stream()
                        .collect(Collectors.groupingBy(Order::getOrderState, Collectors.counting()));
                countByStatus.forEach((s, c) -> System.out.println(s + ": " + c));
            }
            case 6 -> riderService.riderList.forEach(r ->
                    System.out.println(r.getRiderName() + " - deliveries: " + r.getCompletedDeliveries()));
            case 7 -> {
                Map<MenuItem, Long> itemCounts = allOrders.stream()
                        .flatMap(o -> o.getItems().keySet().stream())
                        .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

                itemCounts.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .ifPresentOrElse(
                                e -> System.out.println("Most ordered item: " + e.getKey().getName()),
                                () -> System.out.println("No orders yet, no most ordered item")
                        );
            }
            case 8 -> allOrders.stream()
                    .collect(Collectors.groupingBy(o -> o.getLocalDate().getHour(), Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresentOrElse(
                            e -> System.out.println("Peak hour: " + e.getKey() + ":00"),
                            () -> System.out.println("No orders yet")
                    );
            default -> System.out.println("Wrong input");
        }
    }

    public void platformStatistics() {
        System.out.println("Restaurants: " + restaurantService.restaurantList.size());
        System.out.println("Riders: " + riderService.riderList.size());
        System.out.println("Orders: " + customerService.orders.size());
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