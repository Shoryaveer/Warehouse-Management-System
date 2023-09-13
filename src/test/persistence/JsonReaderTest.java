package persistence;

import model.Item;
import model.Warehouse;
import org.json.JSONException;
import persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    private Warehouse warehouse;
    private JsonWriter writer;
    private JsonReader reader;
    private Item itemA;
    private Item itemB;

    @BeforeEach
    public void setUp() throws IOException {
        warehouse = new Warehouse();
        Item.setFinalID(0);
        itemA = new Item("ItemA", 55);
        itemB = new Item("ItemB", 200);
        warehouse.addItem(itemA);
        warehouse.addItem(itemB);
        warehouse.flipStatus();
        writer = new JsonWriter();
        writer.writeDatabase(warehouse); // tested and is correct so no worries
        reader = new JsonReader();
    }

    private boolean equate(Item itemA, Item itemB){
        return(itemA.getItemID() == itemB.getItemID() &&
                itemA.getName().equals(itemB.getName())&&
                itemA.getQuantity() == itemB.getQuantity());
    }

    @Test
    public void testReadDatabaseRegular() {
        reader.overrideFileName("TestJsonReaderDatabaseRegular");

        try {
            Warehouse newWarehouse = reader.readDatabase();
            assertEquals(warehouse.getIsFull(), newWarehouse.getIsFull());
            assertEquals(warehouse.getAllItems().size(), newWarehouse.getAllItems().size());
            for(Item itemA : warehouse.getAllItems()){
                boolean flag = false;
                for(Item itemB : newWarehouse.getAllItems()){
                    if(equate(itemA, itemB)){
                        flag = true;
                        break;
                    }
                }
                assertTrue(flag);
            }
        } catch (IOException e) {
            fail("regular read: should not run");
        }
        reader.overrideFileName("TestJsonReaderDatabaseRegular1");

        try {
            assertFalse(reader.readDatabase().getIsFull());
        } catch (IOException e) {
            fail("not run");
        }
    }

    @Test
    public void testReadDatabaseNoFile() {
        reader.overrideFileName("not exist");

        try {
            reader.readDatabase();
            fail("should have given error");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    public void testReadDatabaseEmpty() {
        reader.overrideFileName("TestJsonReaderEmptyDatabase");
        try {
            reader.readDatabase();
            fail("should fail");
        } catch (JSONException e) {
            //pass
        } catch (IOException e) {
            fail("shouldn't throw since file exist");
        }
    }

    @Test
    public void testReadDatabaseFaulty() {
        reader.overrideFileName("TestJsonReaderDatabaseFaulty");
        try {
            reader.readDatabase();
            fail("read faulty db");
        } catch (IOException e) {
            //pass
        }
    }

}