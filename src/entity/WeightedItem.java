package entity;

public class WeightedItem extends MenuItem {

    public WeightedItem(String name, double price, String category,
                        int preparationTime, boolean isAvailable) {
        super(name, price, category, preparationTime, isAvailable);
    }

    @Override
    public double countPrice(double quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "WRONG: quantity should be positive number in KG");
        }

        return quantity * getPrice();
    }

    @Override
    public String toString() {
        return "\nWeighted Item: " + super.toString();
    }
}
