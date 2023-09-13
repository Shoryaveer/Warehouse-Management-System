package persistence;

import model.Item;
import model.Warehouse;
import persistence.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {

    private Warehouse warehouse;
    private JsonWriter writer;
    private Item itemA;
    private Item itemB;

    @BeforeEach
    public void setUp() {
        warehouse = new Warehouse();
        itemA = new Item("ItemA", 55);
        itemB = new Item("ItemB", 200);
        warehouse.addItem(itemA);
        warehouse.addItem(itemB);
        writer = new JsonWriter();
    }

    @Test
    public void testWriteDatabaseEmpty() {
        try {
            writer.writeDatabase(new Warehouse());
        } catch (IOException e) {
            fail("Empty warehouse: this should not be here");
        }
    }

    @Test
    public void testWriteDatabaseRegular() {
        try {
            writer.writeDatabase(warehouse);
        } catch (IOException e) {
            fail("regular run: this should not run");
        }
    }

}