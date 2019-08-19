package com.mobiquityinc.packer;

import com.mobiquityinc.pojo.Case;
import com.mobiquityinc.pojo.Item;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class RecursivePacker {

    static List<Item> getPackedItems(Case caseInst) {
        List<Item> items = caseInst.getItems();
        Package pack = findRecursively(caseInst.getWeightLimit(), items, 0, -1);
        return pack.items;
    }

    private static Package findRecursively(int weightLimit, List<Item> items, float totalWeight, int index) {
        Item curItem = index < 0 ? null: items.get(index);
        int curItCost = curItem == null ? 0 : curItem.getCost();
        float curItWeight = curItem == null ? 0 : curItem.getWeight();
        Package bestChildPack = null;

        for (int i = index + 1; i < items.size(); i++) {
            float nextWeight = totalWeight + items.get(i).getWeight();

            if (nextWeight <= weightLimit) {
                Package childPack = findRecursively(weightLimit, items, nextWeight, i);

                if (bestChildPack == null || childPack.cost > bestChildPack.cost) {
                    bestChildPack = childPack;
                } else {
                    if (childPack.cost == bestChildPack.cost) {
                        if (childPack.weight < bestChildPack.weight) {
                            bestChildPack = childPack;
                        }
                    }
                }
            }
        }

        return getItemsPack(bestChildPack, curItWeight, curItCost, curItem);
    }

    private static Package getItemsPack(Package bestChildPack, float curItemWeight, int curItemCost, Item curItem) {
        List<Item> items;

        if (bestChildPack != null) {
            curItemWeight += bestChildPack.weight;
            curItemCost += bestChildPack.cost;
            items = bestChildPack.items;
            if (curItem != null) {
                items.add(0, curItem);
            }
        }
        else {
            if (curItem != null) {
                items = new LinkedList<>();
                items.add(curItem);
            }
            else items = Collections.emptyList();
        }
        return new Package(curItemWeight, curItemCost, items);
    }

    private static class Package {
        private float weight;
        private int cost;
        private List<Item> items;

        private Package(float weight, int cost, List<Item> items) {
            this.weight = weight;
            this.cost = cost;
            this.items = items;
        }
    }
}
