package entity;

import service.GenerateIdService;

import java.util.Objects;

public abstract class MenuItem {

    private final String MenuItemId;

    private String name;

    private double price;

    private String category;

    private double preparationTime;

    private boolean isAvailable;

    private GenerateIdService generateId = new GenerateIdService();

    public MenuItem(String name, double price, String category,
                    int preparationTime, boolean isAvailable) {

        MenuItemId = generateId.generateId(name);
        this.name = name;
        this.price = price;
        this.category = category;
        this.preparationTime = preparationTime;
        this.isAvailable = isAvailable;
    }

    public String getMenuItemId() {
        return MenuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("WRONG: price can not be negative");

        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(double preparationTime) {
        if (preparationTime < 0) throw new IllegalArgumentException("WRONG: preparation time can not be negative");
        this.preparationTime = preparationTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public abstract double countPrice(double quantity);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(MenuItemId, menuItem.MenuItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MenuItemId);
    }

    @Override
    public String toString() {
        return
                "   \nMenu Item Id: " + MenuItemId +
                "   \nName: " + name +
                "   \nPrice: " + price +
                "   \nCategory: " + category +
                "   \nPreparation Time: " + preparationTime +
                "   \nIs Available: " + isAvailable + '\n';
    }
}
