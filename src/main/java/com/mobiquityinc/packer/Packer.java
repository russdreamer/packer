package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.pojo.Case;
import com.mobiquityinc.pojo.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Packer {

    private Packer() {
    }

    /**
     * Pack income items based on maximal package weight and valuable
     * @param filePath text file with item and package weight
     * @return comma separated packaged items's id. Each case outputs as a separate line
     * @throws APIException if incorrect parameters are being passed
     */
    public static String pack(String filePath) throws APIException {
        List<String> lines = readLines(filePath);
        List<Case> cases = new LinkedList<>();
        for (String line : lines) {
            cases.add(parseCase(line));
        }

        if (! isCasesValid(cases)) {
            throw new APIException("The file's line contains incorrect parameters");
        }

        return cases.stream()
                .map(Packer::getSuitableItems)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private static boolean isCasesValid(List<Case> cases) {
        for (Case caseInst: cases) {
            if (caseInst.getWeightLimit() > 100 || caseInst.getItems().size() > 15) {
                return false;
            }
            for (Item item: caseInst.getItems()) {
                if (item.getWeight() > 100) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * parse text file into lines
     * @param filePath text file to parse
     * @return text lines
     * @throws APIException if incorrect parameters are being passed
     */
    private static List<String> readLines(String filePath) throws APIException {
        if (filePath == null) {
            throw new APIException("Invalid file path");
        }

        File file = new File(filePath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.lines().collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new APIException("Invalid file path", e);
        }
    }

    /**
     * parse text line case
     * @param line text line with case attributes
     * @return parsed case as object
     * @throws APIException if incorrect parameters are being passed
     */
    static Case parseCase(String line) throws APIException {
        Pattern pattern = Pattern.compile("^\\d+\\s*:(\\s*(\\([^()]*\\))*\\s*)*$");
        if (!pattern.matcher(line).matches()) {
            throw new APIException("The file's line contains incorrect parameters");
        }

        String[] parts = line.split(":");
        int weightLimit = Integer.valueOf(parts[0].trim());
        List<Item> items = parts.length < 2 || parts[1].trim().isEmpty()? Collections.emptyList(): parseItems(parts[1]);
        return new Case(weightLimit, items);
    }

    /**
     * parse text line of items list
     * @param line set of items with attributes
     * @return parsed items as list of objects
     * @throws APIException if incorrect parameters are being passed
     */
    private static List<Item> parseItems(String line) throws APIException {
        String groupRegex = "(?:\\((\\d+),(\\d+(?:[.]\\d+)*),€(\\d+)\\))";
        String validateRegex = "(\\s" + groupRegex + "\\s*)*";

        Matcher validateMatcher = Pattern.compile(validateRegex).matcher(line);
        if (!validateMatcher.matches()){
            throw new APIException("The file's line contains incorrect parameters");
        }

        Matcher groupMatcher = Pattern.compile(groupRegex).matcher(line);
        List<Item> items = new ArrayList<>();
        while (groupMatcher.find()) {
            items.add(new Item(
                    Integer.valueOf(groupMatcher.group(1)),
                    Float.valueOf(groupMatcher.group(2)),
                    Integer.valueOf(groupMatcher.group(3))
            ));
        }
        return items;
    }

    /**
     * get suitable items that fit into package optimal way
     * @param caseInst case with maximal weight of package and items to pack
     * @return comma separated packaged items' id. Or hyphen sign if no item fits
     */
    private static String getSuitableItems(Case caseInst) {
        /* remove all items that larger than weight limit */
        caseInst.getItems().removeIf(item -> item.getWeight() > caseInst.getWeightLimit());

        List<Item> items;
        if (caseInst.getItems().isEmpty() || (items = RecursivePacker.getPackedItems(caseInst)) == null) {
            return "-";
        }

        return items.stream()
                .map(Item::getIndex)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
