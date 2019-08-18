package com.mobiquityinc.pojo;

public class Item {
    private int index;
    private float weight;
    private int cost;

    public Item(int index, float weight, int cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    public int getIndex() {
        return index;
    }

    public float getWeight() {
        return weight;
    }

    public int getCost() {
        return cost;
    }
}
