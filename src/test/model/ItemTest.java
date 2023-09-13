package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item itemA;
    private int quantityA = 33;
    private Item itemB;

    @BeforeEach
    public void setUp() {
        itemA = new Item("ItemA", quantityA);
        itemB = new Item("ItemB", 200);
    }

    @Test
    public void testGetNameRegular() {
        assertEquals(itemA.getName(), "ItemA");
        assertNotEquals(itemB.getName(), "abc");
    }

    @Test
    public void testGetQuantityRegular() {
        assertEquals(itemA.getQuantity(), quantityA);
    }

}