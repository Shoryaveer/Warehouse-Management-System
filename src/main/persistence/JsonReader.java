package persistence;

import org.json.*;

import model.Item;
import model.Warehouse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

//allows the reading/ loading of database
public class JsonReader {

    private String fileName = "database";
    private JSONObject jsonObject;

    public JsonReader() {
        // pass
    }

    /*
     * REQUIRES: JSON to have all the correct details
     * MODIFIES: this
     * EFFECTS: parses the JSON file and sends it to makeWarehouse
     */
    public Warehouse readDatabase() throws IOException, JSONException {
        String fileData = new String(Files.readAllBytes(Paths.get("./data/" + fileName + ".json")),
                StandardCharsets.UTF_8);

        jsonObject = new JSONObject(fileData);
        Item.setFinalID(jsonObject.getInt("FinalID"));
        JSONArray itemArray = jsonObject.getJSONArray("Items");

        Warehouse warehouse = makeWarehouse(itemArray);
        if (jsonObject.getBoolean("Status")) {
            warehouse.flipStatus();
        }
        return warehouse;
    }

    /*
     * REQUIRES: item array to be not null, correct storage of JSON file
     * MODIFIES: this
     * EFFECTS: makes a new warehouse from the file and returns it.
     */
    private Warehouse makeWarehouse(JSONArray itemArray) throws IOException {
        Warehouse warehouse = new Warehouse();

        try {
            for (Object temp : itemArray) {
                JSONArray arrObj = (JSONArray) temp;
                warehouse.addItem(new Item((int) arrObj.get(0), (String) arrObj.get(1), (int) arrObj.get(2)));
            }
        } catch (Exception e) {
            throw new IOException();
        }

        return warehouse;
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets the database name
     */
    public void overrideFileName(String name) {
        fileName = name;
    }
}
