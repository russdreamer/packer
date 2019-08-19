package com.mobiquityinc.packer;

import com.mobiquityinc.pojo.Case;
import com.mobiquityinc.pojo.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RecursivePackerTest {
    @Test
    void getPackedItemsTest() {
        Item[] items1 = {
                new Item(1, 53.38f, 45),
                new Item(2, 88.62f, 98),
                new Item(3, 78.48f, 3),
                new Item(4, 72.30f, 76),
                new Item(5, 30.18f, 9),
                new Item(6, 46.34f, 48)
        };
        Case c1 = new Case(81, Arrays.asList(items1));
        String res1 = RecursivePacker.getPackedItems(c1).stream().map(Item::getIndex).map(String::valueOf).collect(Collectors.joining(","));
        Assertions.assertEquals("4", res1);

        Item[] items2 = {new Item(1, 15.3f, 34)};
        Case c2 = new Case(8, Arrays.asList(items2));
        String res2 = RecursivePacker.getPackedItems(c2).stream().map(Item::getIndex).map(String::valueOf).collect(Collectors.joining(","));
        Assertions.assertEquals("-", res2);

        Item[] items3 = {
                new Item(1, 85.31f, 29),
                new Item(2, 14.55f, 74),
                new Item(3, 3.98f, 16),
                new Item(4, 26.24f, 55),
                new Item(5, 63.69f, 52),
                new Item(6, 76.25f, 75),
                new Item(7, 60.02f, 74),
                new Item(8, 93.18f, 35),
                new Item(9, 89.95f, 78)
        };
        Case c3 = new Case(75, Arrays.asList(items3));
        String res3 = RecursivePacker.getPackedItems(c3).stream().map(Item::getIndex).map(String::valueOf).collect(Collectors.joining(","));
        Assertions.assertEquals("2,7", res3);

        Item[] items4 = {
                new Item(1, 90.72f, 13),
                new Item(2, 33.80f, 40),
                new Item(3, 43.15f, 10),
                new Item(4, 37.97f, 16),
                new Item(5, 46.81f, 36),
                new Item(6, 48.77f, 79),
                new Item(7, 81.80f, 45),
                new Item(8, 19.36f, 79),
                new Item(9, 6.76f, 64)
        };
        Case c4 = new Case(56, Arrays.asList(items4));
        String res4 = RecursivePacker.getPackedItems(c4).stream().map(Item::getIndex).map(String::valueOf).collect(Collectors.joining(","));
        Assertions.assertEquals("8,9", res4);
    }
}
