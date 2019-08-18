package com.mobiquityinc.pojo;

import java.util.List;

public class Case {
    private int weightLimit;
    private List<Item> items;

    public Case(int weightLimit, List<Item> items) {
        this.weightLimit = weightLimit;
        this.items = items;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public List<Item> getItems() {
        return items;
    }
}
