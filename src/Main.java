import entity.Address;
import entity.Customer;
import entity.District;
import entity.Rider;
import entity.VehicleType;
import service.AdminService;
import service.CustomerService;
import service.ResturantService;
import service.RiderService;

import java.util.Scanner;

Scanner sc = new Scanner(System.in);

ResturantService resturantService = new ResturantService();
RiderService riderService = new RiderService(resturantService);

Customer customer = new Customer("Ahmed", "01012345678");
CustomerService customerService = new CustomerService(customer, resturantService, riderService);

AdminService adminService = new AdminService(resturantService, customerService, riderService);

Rider rider = new Rider("Mohamed", VehicleType.MOTORCYCLE, new Address(District.DOKKI, "near square"), true, 0);


int menu() {
    System.out.println("""
            ============================================
            MASR DELIVERY — Main Menu
            ============================================
            1. Customer
            2. Restaurant
            3. Rider
            4. Admin & Reports
            0. Exit
            ============================================
            """
    );

    System.out.print("Enter your choice: ");
    int choice = sc.nextInt();
    sc.nextLine();

    return choice;

}

int customerMenu() {
    System.out.println("""
            Welcome Customer
            =-=-=-=-=-=-=-=-
            1.Browse restaurants
            2.View Menu
            3.Place Order
            4.Pay from Wallet
            5.Track Order
            6.Cancle Order
            7.Order History
            """);
    System.out.print("Enter your choice: ");
    int choice = sc.nextInt();
    sc.nextLine();

    return choice;

}

int restaurantMenu() {
    System.out.println("""
            
            ========== Restaurant ==========
            1. Accept pending order
            2. Reject pending order
            3. Mark order preparing
            4. Mark order ready
            5. Toggle item availability
            6. Add menu item
            7. Remove menu item
            8. Adjust daily stock
            9. View today's orders and revenue
            """);
    System.out.print("Enter your choice: ");
    int choice = sc.nextInt();
    sc.nextLine();

    return choice;

}

int riderMenu() {
    System.out.println("""
            ========== Rider ==========
            1. Go on duty
            2. Go off duty
            3. View assigned order
            4. Mark order picked up
            5. Mark order delivered
            6. View delivery statistics
            """);
    System.out.print("Enter your choice: ");
    int choice = sc.nextInt();
    sc.nextLine();

    return choice;

}

int adminMenu() {
    System.out.println("""
            
            ========== Admin & Reports ==========
            1. Add restaurant
            2. Remove restaurant
            3. Create promotion
            4. Run reports
            5. Platform statistics
            """);

    System.out.print("Enter your choice: ");
    int choice = sc.nextInt();
    sc.nextLine();

    return choice;

}


void main() {

    customer.addAddress(new Address(District.FAISAL, "Building 5, street 10"));
    riderService.riderList.add(rider);
    riderService.setCurrentRider(rider);

    while (true) {
        int choice = menu();

        if (choice == 1) {

            int c = customerMenu();

            switch (c) {
                case 1 -> customerService.browseResturant();
                case 2 -> customerService.viewMenu();
                case 3 -> customerService.placeOrder();
                case 4 -> customerService.payFromWallet();
                case 5 -> customerService.TrackOrder();
                case 6 -> customerService.cancleOrder();
                case 7 -> customerService.orderHistory();
                default -> System.out.println("Wrong input, try again");
            }


        } else if (choice == 2) {
            int c = restaurantMenu();

            switch (c) {
                case 1 -> resturantService.acceptPendingOrder();
                case 2 -> resturantService.rejectPendingOrder();
                case 3 -> resturantService.markOrderPreparing();
                case 4 -> {
                    resturantService.markOrderReady();
                    riderService.assignReadyOrders();
                }
                case 5 -> resturantService.toggleItemAvailability();
                case 6 -> resturantService.addMenuItem();
                case 7 -> resturantService.removeMenuItem();
                case 8 -> resturantService.AdjustDailyStock();
                case 9 -> resturantService.viewTodayOrdersAndRevenue();
                default -> System.out.println("Wrong input, try again");
            }

        } else if (choice == 3) {
            int c = riderMenu();

            switch (c) {
                case 1 -> riderService.goOnDuty();
                case 2 -> riderService.goOffDuty();
                case 3 -> riderService.viewAssignedOrder();
                case 4 -> riderService.markOrderPickedUp();
                case 5 -> riderService.markOrderDelivered();
                case 6 -> riderService.viewDeliveryStatistics();
                default -> System.out.println("Wrong input, try again");

            }

        } else if (choice == 4) {
            int c = adminMenu();
            switch (c) {
                case 1 -> adminService.addRestaurant();
                case 2 -> adminService.removeRestaurant();
                case 3 -> adminService.createPromotion();
                case 4 -> adminService.runReports();
                case 5 -> adminService.platformStatistics();
                default -> System.out.println("Wrong input, try again");
            }

        } else if (choice == 0) {
            System.out.println("Good bye!");
            break;
        } else {
            System.out.println("Wrong input choice number between 0 - 4");
        }
    }

}
