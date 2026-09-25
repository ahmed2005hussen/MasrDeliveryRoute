package entity;

import service.GenerateIdService;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String customerId;
    private String customerName;
    private String customerNumber;

    private List<Address> addressList;

    private double walletBalance;

    private int completeOrderCount;

    private GenerateIdService generateIdService = new GenerateIdService();

    public Customer(String customerName,
                    String customerNumber) {
        this.customerId = generateIdService.generateId(customerName);
        setCustomerName(customerName);
        setCustomerNumber(customerNumber);
        this.addressList = new ArrayList<>();
        this.walletBalance = 0.0;
        this.completeOrderCount = 0;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.isBlank())
            throw new IllegalArgumentException("WRONG: name can not be empty");

        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {

        if (customerNumber == null || customerNumber.isBlank())
            throw new IllegalArgumentException("WRONG: customer number can not be empty");

        if (customerNumber.length() != 11)
            throw new IllegalArgumentException("WRONG: customer number must be 11 digits");

        if (!customerNumber.startsWith("01"))
            throw new IllegalArgumentException("WRONG: customer number must start with 01");

        this.customerNumber = customerNumber;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void addAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("WRONG: Address can not be null");
        }

        addressList.add(address);
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void addBalance(double walletBalance) {
        if (walletBalance <= 0) {
            throw new IllegalArgumentException("WRONG: customer wallet balance can not be negative");

        }
        this.walletBalance += walletBalance;
    }

    public void withdrawalMoney(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("WRONG: amount can not be negative");
        }

        if (walletBalance - amount < 0)
            throw new IllegalArgumentException("WRONG: amount can not exceed wallet balance which is: " + walletBalance);


        this.walletBalance -= amount;

    }

    public int getCompleteOrderCount() {
        return completeOrderCount;
    }

    public void addCompletedOrder() {
        completeOrderCount++;
    }

    public LoyaltyTier getLoyaltyTier() {
        if (completeOrderCount >= 30) return LoyaltyTier.GOLD;
        if (completeOrderCount >= 10) return LoyaltyTier.SILVER;
        return LoyaltyTier.BRONZE;
    }

    public double getDeliveryDiscount(double fees) {
        if (fees < 0) throw new IllegalArgumentException("WRONG: fees can not be negative");


        return fees * (1 - getLoyaltyTier().getDiscount());
    }

    @Override
    public String toString() {
        return "\nCustomer" +
                "   \ncustomer Id: " + customerId +
                "   \ncustomer Name: " + customerName +
                "   \ncustomer Number: " + customerNumber +
                "   \naddress List: " + addressList +
                "   \nwallet Balance: " + walletBalance +
                "   \ncomplete Order Count: " + completeOrderCount +
                "   \ncustomer Type: " + getLoyaltyTier().toString() + '\n';
    }
}
