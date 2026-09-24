package entity;


import service.GenerateIdService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Restaurant {

    private final String restaurantId;
    private String restaurantName;
    private List<Address> addresses;
    private Set<String> cuisineCategories;
    private double averageRating;
    private boolean isOpen;
    private Menu menu;

    private GenerateIdService generateId = new GenerateIdService();

    public Restaurant(String restaurantName, List<Address> addresses,
                      Set<String> cuisineCategories, double averageRating,
                      boolean isOpen, Menu menu) {

        this.restaurantId = generateId.generateId(restaurantName);
        this.restaurantName = restaurantName;
        this.addresses = addresses;
        this.cuisineCategories = cuisineCategories;
        this.averageRating = averageRating;
        this.isOpen = isOpen;
        this.menu = menu;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public List<Address> getAddress() {
        return addresses;
    }

    public void addAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("WRONG: address can not be null ");
        }
        if (!addresses.contains(address))
            addresses.add(address);

    }

    public void setAddress(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<String> getCuisineCategories() {
        return cuisineCategories;
    }

    public void addCuisineCategories(String cuisine) {
        if (cuisine == null || cuisine.isBlank()) {
            throw new IllegalArgumentException("WRONG: cuisine can not be empty ");
        }
        cuisineCategories.add(cuisine);

    }

    public void setCuisineCategories(Set<String> cuisineCategories) {
        this.cuisineCategories = cuisineCategories;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {

        if (averageRating > 5 || averageRating < 0) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        this.averageRating = averageRating;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "\nRestaurant" +
                "   \nrestaurantId: " + restaurantId +
                "   \nRestaurant Name: " + restaurantName +
                "   \nAddresses: " + addresses +
                "   \nCuisine Categories: " + cuisineCategories +
                "   \nAverage Rating: " + averageRating +
                "   \nIs Open: " + isOpen +
                "   \nMenu: " + menu + '\n';
    }
}
