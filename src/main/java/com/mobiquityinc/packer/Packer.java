package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {
        File file = new File(filePath);
        List<String> lines = readLines(file);

        List<Case> cases = new LinkedList<>();
        for (String line : lines) {
            cases.add(parseCase(line));
        }
        return cases.stream().map(Packer::getSuitableItems).collect(Collectors.joining(System.lineSeparator()));
    }

    private static List<String> readLines(File file) {
        return null;
    }

    public static Case parseCase(String line) {
        return null;
    }

    private static String getSuitableItems(Case caseInst) {
        return "-";
    }
}
