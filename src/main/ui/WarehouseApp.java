package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Event;
import model.EventLog;
import org.json.JSONException;

import model.Item;
import model.Warehouse;
import persistence.JsonReader;
import persistence.JsonWriter;

//This is the user interface for the warehouse and item
public class WarehouseApp {

    private boolean quit = false;
    private Scanner scanner = new Scanner(System.in);
    private Warehouse warehouse;

    /*
     * EFFECTS: initializes the warehouse object
     */
    public WarehouseApp() {
        warehouse = new Warehouse();
    }

    /*
     * MODIFIES:this.warehouse
     * EFFECTS:it creates a demo item and add it to the list
     * then displays the menu that take commands
     * if quit is pressed the program ends
     */
    public void runDemo() {
        Item newItem = new Item("demo Item", 300);
        warehouse.addItem(newItem);
        while (!quit) {
            displayMenu();
        }
    }

    /*
     * EFFECTS: display menu options to the user.
     */
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Create and add new Item to warehouse");
        System.out.println("\tr -> Remove an Item");
        System.out.println("\td -> Display all items");
        System.out.println("\ts -> Check/Change Warehouse status");
        System.out.println("\tp -> Save Database");
        System.out.println("\tl -> Load Database");
        System.out.println("\tq -> Quit");

        String command = scanner.next();
        processCommand(command);
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            doCreateItem();
        } else if (command.equals("r")) {
            doRemove();
        } else if (command.equals("d")) {
            doDisplayAll();
        } else if (command.equals("s")) {
            warehouseStatus();
        } else if (command.equals("p")) {
            saveDB();
        } else if (command.equals("l")) {
            loadDB();
        } else if (command.equals("q")) {
            quit();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: saves the DB to the Json file
     */
    private void saveDB() {
        JsonWriter writeDB = new JsonWriter();
        try {
            writeDB.writeDatabase(this.warehouse);
        } catch (IOException e) {
            System.out.println("try again later.");
        }
        System.out.println("database saved.");
    }

    /*
     * MODIFIES: this
     * EFFECTS: loads the existing database
     */
    private void loadDB() {
        JsonReader readDB = new JsonReader();
        try {
            Warehouse newWarehouse = readDB.readDatabase();
            warehouse = newWarehouse;
            System.out.println("New warehouse loaded");
        } catch (IOException e) {
            System.out.println("The file is not correct. no database loaded.");
        } catch (JSONException e) {
            System.out.println("The JSON file is corrupted.");
        }
    }

    /*
     * REQUIRES: Name and Quantity to be non-empty and quantity
     * to be integer
     * MODIFIES: this
     * EFFECTS: prompts the user for name of the product and
     * quantity then creates and adds the item to
     * the warehouse. prints the feedback.
     */
    private void doCreateItem() {
        System.out.print("Enter Name: ");
        scanner.nextLine(); // wasnt warking because of empty space and just needed flushing
        String name = scanner.nextLine();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        Item newItem = new Item(name, quantity);
        if (warehouse.addItem(newItem)) {
            System.out.printf("Item added. ID is %d%n", newItem.getItemID());
        } else {
            System.out.println("Warehouse full. Item not added");
        }
    }

    /*
     * REQUIRES: itemID to be integer
     * MODIFIES: this.warehouse
     * EFFECTS: this will remove the item from the warehouse
     * and give a feedback
     */
    private void doRemove() {
        doDisplayAll();

        System.out.println("Enter ItemID to remove: ");
        int itemID = scanner.nextInt();

        if (warehouse.removeItem(itemID)) {
            System.out.println("Item removed.");
        } else {
            System.out.println("No item removed.");
        }
    }

    /*
     * EFFECTS: prints all the items in the warehouse
     */
    private void doDisplayAll() {
        ArrayList<Item> items = warehouse.getAllItems();

        System.out.println("ItemNo\tName\tQuantity");
        for (Item item : items) {
            System.out.println(item.getItemID() + "\t" + item.getName() + "\t " + item.getQuantity());
            System.out.println();
        }
    }

    /*
     * MODIFIES: this.warehouse
     * EFFECTS: this will get the warehouse status and ask the user if they
     * want to change it.
     */
    private void warehouseStatus() {
        if (warehouse.getIsFull()) {
            System.out.println("Warehouse is full.");
        } else {
            System.out.println("Warehouse is not full.");
        }

        System.out.print("Change Status (y/n)? ");
        String choice = scanner.next();

        if (choice.equals("y")) {
            warehouse.flipStatus();
            System.out.println("Status changed.");
        } else {
            System.out.println("Status not changed.");
        }
    }

    /*
     * MODIFIES:this
     * EFFECTS: sets the quit to be false and closes the scanner
     */
    private void quit() {
        quit = true;
        scanner.close();
        for (Event event : EventLog.getInstance()) {
            System.out.println(event + "\n");
        }
    }

}
