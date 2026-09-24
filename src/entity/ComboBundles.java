package entity;

import java.util.HashMap;
import java.util.Map;

public class ComboBundles extends MenuItem {

    private Map<MenuItem, Double> items;
    private double discount;

    public ComboBundles(String name, double price, String category, int preparationTime,
                        boolean isAvailable, double discount) {
        super(name, price, category, preparationTime, isAvailable);
        this.items = new HashMap<>();

        if (discount < 0 || discount >= 1) {
            throw new IllegalArgumentException("WRONG: discount should be between 0 and 1");
        }

        this.discount = discount;
    }

    private double makeDiscount(double total) {

        return total * (1 - discount);
    }

    public void addItem(MenuItem menuItem, double quantity) {
        if (menuItem == null) throw new IllegalArgumentException("WRONG: Item can not be null");

        if (quantity <= 0) throw new IllegalArgumentException("WRONG: quantity can not be negative or 0");

        items.put(menuItem, quantity);
    }

    @Override
    public double countPrice(double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "WRONG: quantity should be positive number");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("WRONG: there is no combo yet!");
        }

        double total = items.entrySet().stream().mapToDouble(
                entry -> entry.getKey().countPrice(entry.getValue())
        ).sum();

        return makeDiscount(total) * quantity;
    }

    @Override
    public String toString() {
        return "\nCombo Bundles" +
                super.toString() +
                "   Items: " + items +
                "   \nDiscount: " + discount + '\n';
    }
}
