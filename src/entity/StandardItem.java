package entity;

public class StandardItem extends MenuItem {

    public StandardItem(String name, double price, String category,
                        int preparationTime, boolean isAvailable) {
        super(name, price, category, preparationTime, isAvailable);
    }

    @Override
    public double countPrice(double quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "WRONG: quantity should be positive number");
        }

        return quantity * getPrice();
    }

    @Override
    public String toString() {
        return "\nStandard Item: " + super.toString();
    }
}
