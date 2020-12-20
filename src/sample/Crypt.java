package sample;

import java.util.*;

public class Crypt {
    public static String crypt(String text) {
        indexesToString(asciiToIndex(text));
        return indexesToString(asciiToIndex(text));
    }

    public static String decrypt(String text) {
        StringTokenizer stringTokenizer = new StringTokenizer(text, " ");
        List<Integer> indexes = new ArrayList<>();
        while (stringTokenizer.hasMoreTokens()) {
            String elem = numbersSwap(stringTokenizer.nextToken());
            indexes.add(binaryToDecimal(elem));
        }

        return indexesToAscii(indexes);
    }

    private static int[] asciiToIndex(String text) {
        char[] textArr = text.toCharArray();
        int[] indexArr = new int[textArr.length];

        for (int i = 0; i < textArr.length; i++) {
            indexArr[i] = textArr[i];
        }

        return indexArr;
    }

    private static String indexesToAscii(List<Integer> indexes) {
        char[] chars = new char[indexes.size()];
        int[] indexesArr = new int[indexes.size()];

        for (int i = 0; i < indexes.size(); i++) {
            indexesArr[i] = indexes.get(i);
        }

        for (int i = 0; i < indexesArr.length; i++) {
            chars[i] = (char) indexesArr[i];
        }

        return String.valueOf(chars);
    }

    private static String indexesToString(int[] decimals) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int el : decimals) {
            String decToBin = numbersSwap(decimalToBinary(el));
            for (int i = 0; i < decToBin.length(); i++) {
                stringBuilder.append(decToBin.charAt(i));
            }
            stringBuilder.append(' ');
        }

        return stringBuilder.toString();
    }

    private static String numbersSwap(String text) {
        text = text.replaceAll("0", "2")
                .replaceAll("1", "0")
                .replaceAll("2", "1");
        return text;
    }

    private static String decimalToBinary(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    private static int binaryToDecimal(String binary) {
        return Integer.parseInt(binary, 2);
    }
}
