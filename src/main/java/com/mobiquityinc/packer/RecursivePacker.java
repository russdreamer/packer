package com.mobiquityinc.packer;

import com.mobiquityinc.pojo.Case;
import com.mobiquityinc.pojo.Item;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Recursive method of package. Complexity is 2^n. Effectively in use for a small amount of n.
 * Please read in ReadMe for details
 */
class RecursivePacker {

    /**
     * get suitable items for the case
     * @param caseInst case with maximal weight of package and items to pack
     * @return items that fit given maximal weight and have maximal valuable. If different set of items has equal
     * weight it returns set with less weight
     */
    static List<Item> getPackedItems(Case caseInst) {
        List<Item> items = caseInst.getItems();
        Package pack = findRecursively(caseInst.getWeightLimit(), items, 0, -1);
        return pack.items;
    }

    /**
     * find recursively the best combination of items that fit given weight limit
     * @param weightLimit weight limit
     * @param items expectant items to be packed
     * @param totalWeight current weight of all packed items
     * @param index item index to be packed. Zero based numeration starts from zero. -1 for the first iteration
     * @return packed items as object with total weight and valuable
     */
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

    /**
     * get current packed items as package. It decides what list of item get back depending on if item was root or not,
     * if it has any child or top leveled
     * @param bestChildPack list of packed children items
     * @param curItemWeight current root item's weight
     * @param curItemCost current root item's cost
     * @param curItem current root item
     * @return packed items with total weight and valuable
     */
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

    /**
     * class to replace pair return in recursive function. It lets to return all necessary pack attributes
     */
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
