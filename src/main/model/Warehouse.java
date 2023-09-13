package model;

import java.util.ArrayList;

//a Warehouse is where user can manage stock/items and also set the warehouse as closed
public class Warehouse {
    private ArrayList<Item> items;
    private boolean isFull;

    /*
     * EFFECTS: creates a warehouse object which has a isFull marked as false, as it
     *          will be used to check the full status of warehouse. An empty ArrayList
     *          is created to store items.
     */
    public Warehouse() {
        isFull = false;
        items = new ArrayList<Item>();
    }

    /*
     * REQUIRES: isFill should be false(means warehouse not full) and item object
     *          should exist
     * MODIFIES: this
     * EFFECTS: it will add the item if it meets the required condition and return
     *          true or else will return false
     */
    public boolean addItem(Item item) {
        if (!isFull && item != null) {
            items.add(item);
            EventLog.getInstance().logEvent(new Event("New Item added to Warehouse."));
            return true;
        }
        return false;
    }

    /*
     * MODIFIES: this
     * EFFECTS: it will match the itemID with all the items and remove the matching
     *          then return true else returns false
     */
    public boolean removeItem(int itemID) {
        for (Item item : items) {
            if (item.getItemID() == itemID) {
                items.remove(item);
                EventLog.getInstance().logEvent(new Event("Item Removed from Warehouse."));
                return true;
            }
        }
        return false;
    }

    public ArrayList<Item> getAllItems() {
        return items;
    }

    public boolean getIsFull() {
        return isFull;
    }

    /*
     * MODIFIES: isFull
     * EFFECTS: this will flip the status of warehouse(true to false, false to true)
     *          should be used in pair with getIsFull
     */
    public void flipStatus() {
        isFull = !isFull;
        EventLog.getInstance().logEvent(new Event("Warehouse status changed."));
    }

}
