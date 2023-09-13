package persistence;

import org.json.*;

import model.Item;
import model.Warehouse;

import java.io.*;
import java.util.ArrayList;

//This gives allows the database to be written
public class JsonWriter {

    private JSONObject jsonWriter;

    /*
     * EFFECTS: initialises json writer
     */
    public JsonWriter() {
        jsonWriter = new JSONObject();
    }

    /*
     * REQUIRES: warehouse to be not null
     * MODIFIES: this
     * EFFECTS: writes all the fields to JSON class
     */
    public void writeDatabase(Warehouse warehouse) throws IOException {
        ArrayList<Item> items = warehouse.getAllItems();

        jsonWriter.put("FinalID", Item.getFinalID());

        JSONArray itemsArray = new JSONArray();
        for (Item item : items) {
            JSONArray value = new JSONArray();
            value.put(item.getItemID());
            value.put(item.getName());
            value.put(item.getQuantity());
            itemsArray.put(value);
        }

        jsonWriter.put("Items", itemsArray);
        jsonWriter.put("Status", warehouse.getIsFull());

        writeFile();
    }

    /*
     * REQUIRES:
     * MODIFIES: this
     * EFFECTS: writes the json file
     */
    private void writeFile() throws IOException {
        String destination = "./data/database.json";
        FileWriter fileWriter = new FileWriter(destination);
        fileWriter.write(jsonWriter.toString(4));
        fileWriter.close();
    }

}
