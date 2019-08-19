package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.pojo.Case;
import com.mobiquityinc.pojo.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {
        List<String> lines = readLines(filePath);

        List<Case> cases = new LinkedList<>();
        for (String line : lines) {
            cases.add(parseCase(line));
        }
        return cases.stream().map(Packer::getSuitableItems).collect(Collectors.joining(System.lineSeparator()));
    }

    private static List<String> readLines(String filePath) throws APIException {
        if (filePath == null) {
            throw new APIException("Invalid file path");
        }

        File file = new File(filePath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.lines().collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new APIException("Invalid file path");
        }
    }

    private static Case parseCase(String line) throws APIException {
        Pattern pattern = Pattern.compile("^\\d+\\s*:(\\s*\\([^()]*\\)\\s*)*$");
        if (!pattern.matcher(line).matches()) {
            throw new APIException("The file's line contains incorrect parameters");
        }

        String[] parts = line.split(":");
        int weightLimit = Integer.valueOf(parts[0].trim());
        List<Item> items = parseItems(parts[1]);
        return new Case(weightLimit, items);
    }

    private static List<Item> parseItems(String line) throws APIException {
        String groupRegex = "(?:\\((\\d+),(\\d+(?:[.]\\d+)*),€(\\d+)\\))";
        String validateRegex = "(\\s" + groupRegex + "\\s*)*";

        Matcher validateMatcher = Pattern.compile(validateRegex).matcher(line);
        if (!validateMatcher.matches()){
            throw new APIException("The file's line contains incorrect parameters");
        }

        Matcher groupMatcher = Pattern.compile(groupRegex).matcher(line);
        List<Item> items = new LinkedList<>();
        while (groupMatcher.find()) {
            items.add(new Item(
                    Integer.valueOf(groupMatcher.group(1)),
                    Float.valueOf(groupMatcher.group(2)),
                    Integer.valueOf(groupMatcher.group(3))
            ));
        }
        return items;
    }

    private static String getSuitableItems(Case caseInst) {
        caseInst.getItems().removeIf(item -> item.getWeight() > caseInst.getWeightLimit());

        List<Item> items;
        if (caseInst.getItems().isEmpty() || (items = RecursivePacker.getPackedItems(caseInst)) == null) {
            return "-";
        }

        return items.stream().map(Item::getIndex).map(String::valueOf).collect(Collectors.joining(","));
    }
}
