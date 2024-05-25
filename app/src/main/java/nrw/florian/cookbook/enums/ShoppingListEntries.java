package nrw.florian.cookbook.enums;

public enum ShoppingListEntries {
    ÄPFEL("Äpfel"),
    BANANEN("Bananen"),
    BROT("Brot"),
    KUCHEN("Kuchen"),
    MILCH("Milch");

    private String name;

    private ShoppingListEntries(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
