package ui;

//doggo pic credit: 
//https://www.countryliving.com
// /life/kids-pets/a28423224/dog-instagram-captions/
import javax.swing.*;

import model.Event;
import model.EventLog;
import org.json.JSONException;

import model.Item;
import model.Warehouse;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;

import java.util.ArrayList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;

//This class creates the GUI for warehouse management
public class WarehouseUI {

    private Warehouse warehouse;

    private JFrame mainFrame;
    private JPanel controlPanel;
    private JPanel listPanel;

    private JScrollPane scrollPane;

    private JList<String> itemList;

    private JLabel nameLabel;
    private JLabel quantityLabel;

    private JTextField nameField;
    private JTextField quantityField;

    private JButton addButton;
    private JButton displayAllButton;
    private JButton statusButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton removeButton;
    private JButton calmButton;

    /*
     * REQUIRES: warehouse to be not null
     * MODIFIES: this
     * EFFECTS: sets the warehouse for rest of the functions
     */
    public WarehouseUI(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialsies the UI components and starts a chain reaction for all
     * other listeners and adding components
     */
    public void initialize() {
        mainFrame = new JFrame("Warehouse Management");
        controlPanel = new JPanel();
        listPanel = new JPanel();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controlPanel.setLayout(new GridLayout(0, 2));
        mainFrame.setLayout(new BorderLayout());

        setComp();
        addComp();
        setListPanel();

        mainFrame.add(controlPanel, BorderLayout.NORTH);
        mainFrame.add(new JSeparator(), BorderLayout.CENTER);
        mainFrame.add(listPanel, BorderLayout.SOUTH);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /*
     * MODIFIES: this
     * EFFECTS: creates the lower half of the screen, specifically the list panel to
     * display the warehouse items
     */
    private void setListPanel() {
        JList<Item> itemList = new JList<>();
        listPanel.setPreferredSize(new Dimension(200, 200));
        scrollPane = new JScrollPane(itemList);
        listPanel.add(scrollPane);
    }

    /*
     * MODIFIES: all the JComponents
     * EFFECTS: This makes all the components and the calls set listeners to define
     * event functions
     */
    private void setComp() {
        nameLabel = new JLabel("Name");
        quantityLabel = new JLabel("Quantity");

        nameField = new JTextField();
        quantityField = new JTextField();

        addButton = new JButton("Add Item");
        displayAllButton = new JButton("Display All");
        statusButton = new JButton("Change Status");
        loadButton = new JButton("Load DB");
        saveButton = new JButton("Save DB");
        removeButton = new JButton("Remove Item");
        calmButton = new JButton("Calm me pls");

        setListeners();
    }

    /*
     * REQUIRES: set comp to be called first
     * MODIFIES: all JComponents
     * EFFECTS: This calls all the functions that set the behaviour of
     * components
     */
    private void setListeners() {
        addFunc();
        displayFunc();
        removeFunc();
        calmFunc();
        statusFunc();
        loadFunc();
        saveFunc();
        endSequence();
    }

    /*
     * MODIFIES: mainFrame
     * EFFECTS: sets a window listener with a window adapter so that when
     * the window closes it initiates the printing of events
     */
    private void endSequence() {
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event + "\n");
                }
                System.exit(0);
            }
        });
    }

    /*
     * MODIFIES: JSON file
     * EFFECTS: Writes the current state of warehouse to designated JSON file
     */
    private void saveFunc() {
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JsonWriter writeDB = new JsonWriter();
                try {
                    writeDB.writeDatabase(warehouse);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "try again later.");
                }
                JOptionPane.showMessageDialog(mainFrame, "database saved.");
            }
        });
    }

    /*
     * MODIFIES: warehouse
     * EFFECTS: loads the warehouse from the designated file and replaces the
     * current warehouse
     */
    private void loadFunc() {
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JsonReader readDB = new JsonReader();
                try {
                    Warehouse newWarehouse = readDB.readDatabase();
                    warehouse = newWarehouse;
                    JOptionPane.showMessageDialog(mainFrame, "New warehouse loaded");
                    displayAllButton.doClick();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "The file is not correct. no database loaded.");
                } catch (JSONException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "The JSON file is corrupted.");
                }
            }
        });
    }

    /*
     * MODIFIES: warehouse
     * EFFECTS: this flips the warehouse status (is full or not)
     */
    private void statusFunc() {
        statusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                warehouse.flipStatus();
                JOptionPane.showMessageDialog(
                        mainFrame, "Warehouse full ->" + warehouse.getIsFull());
            }
        });
    }

    /*
     * EFFECTS: opens a new window and shows a cute doggo pic to calm the user if he
     * has too much stress
     */
    private void calmFunc() {
        calmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame calmFrame = new JFrame();
                calmFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JLabel doggoPic = new JLabel(new ImageIcon("./data/calmDoggo.jpg"));

                calmFrame.add(doggoPic);

                calmFrame.pack();
                calmFrame.setVisible(true);
            }
        });

    }

    /*
     * REQUIRES: selected JList item
     * MODIFIES: warehouse
     * EFFECTS: this removes the selected Jlist item/warehouse item and refreshes
     * the list
     */
    private void removeFunc() {
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = itemList.getSelectedIndex();
                if (index != -1) {
                    warehouse.removeItem(warehouse.getAllItems().get(index).getItemID());
                }
                displayAllButton.doClick();
            }
        });
    }

    /*
     * REQUIRES: the name and quantity field to be non empty and the quantity field
     * should be parsable to integer
     * MODIFIES: warehouse
     * EFFECTS: this gets the name and quantity field then adds the item to the
     * warehouse if they are valid
     */
    private void addFunc() {
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String quantity = quantityField.getText();

                Item item = new Item(name, Integer.parseInt(quantity));

                warehouse.addItem(item);
                displayAllButton.doClick();
            }
        });
    }

    /*
     * EFFECTS: this gets a fresh list of warehouse items, creates a list model and
     * then calls the setScrollPane to refresh the pane
     */
    private void displayFunc() {
        displayAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Item> items = warehouse.getAllItems();
                DefaultListModel<String> listModel = new DefaultListModel<>();

                for (Item item : items) {
                    listModel.addElement(item.getItemID() + "      " + item.getName() + "      " + item.getQuantity());
                }

                itemList = new JList<>(listModel);
                itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setScrollPane();
            }
        });
    }

    /*
     * MODIFIES: scrollPane, listPane
     * EFFECTS: this refreshes the scroll pane with new items.. basicall updates the
     * all item display
     */
    private void setScrollPane() {
        listPanel.remove(scrollPane);
        listPanel.revalidate();
        listPanel.repaint();

        scrollPane = new JScrollPane(itemList);
        listPanel.add(scrollPane);
        listPanel.revalidate();
        listPanel.repaint();
    }

    /*
     * REQUIRES: setComp to be called
     * MODIFIES: controlPane
     * EFFECTS: this add all the JComponents to the controlPane
     */
    private void addComp() {
        JComponent[] componentArray = { nameLabel, nameField, quantityLabel, quantityField, addButton, loadButton,
                displayAllButton, saveButton, statusButton, calmButton, removeButton };

        for (JComponent comp : componentArray) {
            controlPanel.add(comp);
        }
    }

    /*
     * EFFECTS: For Demo purposes only.. this creates a demo warehouse with two item
     * and calls the UI
     */
    public static void runDemo() {
        Warehouse warehouse = new Warehouse();
        Item newItem = new Item("demo Item", 300);
        Item newItem2 = new Item("demo Item2", 400);
        warehouse.addItem(newItem);
        warehouse.addItem(newItem2);
        new WarehouseUI(warehouse).initialize();
    }
}
