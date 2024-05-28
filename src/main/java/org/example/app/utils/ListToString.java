package org.example.app.utils;

import java.util.List;

public class ListToString {
    public static String listToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s).append("\n");
        }
        return result.toString().trim();
    }
}
