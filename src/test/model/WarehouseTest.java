package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    private Warehouse warehouse;
    private Item itemA;
    private Item itemB;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        itemA = new Item("ItemA", 55);
        itemB = new Item("ItemB", 200);
        warehouse.addItem(itemA);
        warehouse.addItem(itemB);
    }

    @Test
    public void testAddItemNullItem() {
        assertFalse(warehouse.addItem(null));
    }

    @Test
    public void testAddItemWarehouseFull() {
        Item dummyItem = new Item("dummy", 300);
        warehouse.flipStatus(); //sets warehouse as full
        assertFalse(warehouse.addItem(dummyItem));
    }

    @Test
    public void testRemoveItemExistence() {
        assertTrue(warehouse.removeItem(itemB.getItemID()));
    }

    @Test
    public void testRemoveItemNoExistence() {
        assertFalse(warehouse.removeItem(9999));
    }

    @Test
    public void testRemoveItemEmptyList() {
        Warehouse warehouse1 = new Warehouse();
        assertFalse(warehouse1.removeItem(333));
    }

    @Test
    public void testGetAllItemsRegular() {
        ArrayList<Item> items = warehouse.getAllItems();
        assertTrue(items.contains(itemA));
        assertTrue(items.contains(itemB));
    }


    @Test
    void testFlipStatusRegular() {
        warehouse.flipStatus();
        assertTrue(warehouse.getIsFull());
        warehouse.flipStatus();
        assertFalse(warehouse.getIsFull());
    }

    @Test
    public void testEventLog() {
        EventLog.getInstance().clear();
        Warehouse localWarehouse = new Warehouse();
        localWarehouse.addItem(itemA);
        localWarehouse.addItem(itemB);

        localWarehouse.removeItem(warehouse.getAllItems().get(0).getItemID()); //since ItemID can change
        localWarehouse.flipStatus();

        /* Two objects were added, one removed and then status change
        * This should reflect in the log
        * */

        ArrayList<Event> eventLog= new ArrayList<>();

        for (Event event : EventLog.getInstance()) {
            eventLog.add(event);
        }

        assertEquals(eventLog.size(), 5);
        assertEquals(eventLog.get(0).getDescription(), "Event log cleared.");
        assertEquals(eventLog.get(1).getDescription(), "New Item added to Warehouse.");
        assertEquals(eventLog.get(2).getDescription(), "New Item added to Warehouse.");
        assertEquals(eventLog.get(3).getDescription(), "Item Removed from Warehouse.");
        assertEquals(eventLog.get(4).getDescription(), "Warehouse status changed.");

    }
}