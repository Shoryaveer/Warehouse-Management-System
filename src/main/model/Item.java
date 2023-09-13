package model;

// Represents an Item in a warehouse
public class Item {
    private String name;
    private int quantity;
    private final int itemID;
    private static int itemNo = 0;

    /*
     * REQUIRES: name.length() > 0; quantity >= 0;
     * MODIFIES: this
     * EFFECTS: sets fields of the stock.
     */
    public Item(String name, int quantity) {
        itemID = ++itemNo;
        this.name = name;
        this.quantity = quantity;
    }

    /*
     * REQUIRES: name.length() > 0; quantity >= 0; ID>=0
     * MODIFIES: this
     * EFFECTS: sets fields of the stock.
     */
    public Item(int customID, String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        itemID = customID;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    // as the itemID is static it will return unique but not random results.
    public int getItemID() {
        return itemID;
    }

    public static int getFinalID() {
        return itemNo;
    }

    public static void setFinalID(int finalId) {
        itemNo = finalId;
    }
}
