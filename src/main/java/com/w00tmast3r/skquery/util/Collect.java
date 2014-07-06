package com.w00tmast3r.skquery.util;

import java.io.InputStream;
import java.util.Scanner;

public class Collect {
    public static String[] toStringArray(Object[] array) {
        String[] strings = new String[array.length];
        for (int i = 0; i < strings.length; i++) strings[i] = array[i].toString();
        return strings;
    }

    public static String[] toFriendlyStringArray(Object[] array) {
        String[] strings = new String[array.length];
        for (int i = 0; i < strings.length; i++) strings[i] = array[i].toString().toLowerCase().replace("_", " ");
        return strings;
    }

    public static <T> T[] asArray(T... objects) {
        return objects;
    }

    public static String[] asSkriptProperty(String property, String fromType) {
        return asArray("[the] " + property + " of %" + fromType + "%", "%" + fromType + "%'[s] " + property);
    }

    public static <T> String toString(T[] array) {
        return toString(array, ',');
    }

    public static <T> String toString(T[] array, char separator) {
        return toString(array, separator, true);
    }


    public static <T> String toString(T[] array, char separator, boolean spaces) {
        String SEPARATOR = spaces ? " " : "";
        if (array == null)
            return "null";
        int max = array.length - 1;
        if (max == -1)
            return "";
        String b = "";
        for (int i = 0; ; i++) {
            b += String.valueOf(array[i]);
            if (i == max)
                return b;
            b += separator + SEPARATOR;
        }
    }

    public static String textPart(InputStream is) {
        if (is == null) return "";
        Scanner s = new Scanner(is).useDelimiter("\\A");
        try {
            return s.hasNext() ? s.next() : "";
        } finally {
            s.close();
        }
    }
}
