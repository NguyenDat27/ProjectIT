package ITproject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Bits extends DES{
	
    public static int[] toBits(int dec, int minLen) {
        ArrayList<Integer> resultList = new ArrayList<>();
        while (dec > 0) {
            resultList.add(dec % 2);
            dec /= 2;
        }
        return finalizedArray(resultList, minLen, true);
    }

    public static int[] toBits(String hex, int minLen) {
        ArrayList<Integer> resultList = new ArrayList<>();
        BigInteger temp = new BigInteger(hex.replace(" ", "").trim(), 16);

        while (temp.compareTo(new BigInteger("0", 10)) > 0) {
            resultList.add(temp.mod(new BigInteger("2", 10)).intValue());
            temp = temp.divide(new BigInteger("2", 10));
        }
        return finalizedArray(resultList, minLen, true);
    }

    private static int[] finalizedArray(ArrayList<Integer> resultList, int minLen, boolean reverse) {
        int[] resultArray;
        while (resultList.size() < minLen || (minLen <= 0 && resultList.size() % 8 != 0)) {
            if (reverse) resultList.add(0);
            else resultList.add(0, 0);
        }
        
        if (reverse) Collections.reverse(resultList);

        resultArray = new int[resultList.size()];
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = resultList.get(i);
        }
        return resultArray;
    }

    public static int toDecimal(final int[] bits) throws IllegalArgumentException {
        int result = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            if (bits[i] != 0 && bits[i] != 1)
                throw new IllegalArgumentException("Bit array may not contain any other values than 0 or 1!");
            result += bits[i] * ((int) Math.pow(2, (bits.length - 1) - i));
        }
        return result;
    }

    public static BigInteger toBigInt(final int[] bits) throws IllegalArgumentException {
        BigInteger result = new BigInteger("0", 10);
        for (int i = bits.length - 1; i >= 0; i--) {
            if (bits[i] != 0 && bits[i] != 1)
                throw new IllegalArgumentException("Bit array may not contain any other values than 0 or 1!");
            BigInteger b1 = new BigInteger("2").pow((bits.length - 1) - i);
            BigInteger b2 = new BigInteger("" + bits[i]).multiply(b1);
            result = result.add(b2);
        }
        return result;
    }

    public static String toHex(final int[] bits) {
        return toBigInt(bits).toString(16).toUpperCase();
    }

    public static String stringToHex(String str) {
        ArrayList<Integer> resultList = new ArrayList<>();

        for (char c : str.toCharArray()) {
            for (int i : toBits(c, 8)) {
                resultList.add(i);
            }
        }
        return toHex(finalizedArray(resultList, 0, false));
    }

    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        int[] temp = new int[8];
        int[] bits = Bits.toBits(hex, 0);

        for (int i = 0; i < bits.length / 8; i++) {
            System.arraycopy(bits, i * 8, temp, 0, 8);
            sb.append((char) toDecimal(temp));
            if (sb.indexOf("" + (char) 0) > -1) sb.deleteCharAt(sb.indexOf("" + (char) 0));
        }
        return sb.toString();
    }

    public static int[] expand(int[] bits, int newLen) {
        if (newLen <= bits.length) return bits;

        int[] temp = new int[newLen];

        System.arraycopy(bits, 0, temp, newLen - bits.length, bits.length);
        return temp;
    }

    public static int[] xor(final int[] bits, final int[] otherBits) throws IllegalArgumentException {
        if (bits.length != otherBits.length) throw new IllegalArgumentException("Bit arrays must be of the same size!");

        int[] retVal = new int[bits.length];

        for (int i = 0; i < bits.length; i++) {
            retVal[i] = (bits[i] + otherBits[i]) % 2;
        }
        return retVal;
    }

}
