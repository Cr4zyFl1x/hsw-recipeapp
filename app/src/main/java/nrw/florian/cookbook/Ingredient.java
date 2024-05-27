package nrw.florian.cookbook;

public class Ingredient {
    private String name;
    private double quantity;
    private String unit;


    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getter-Methoden
    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return quantity + " " + unit + " " + name;
    }
}
