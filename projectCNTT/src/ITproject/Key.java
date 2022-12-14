package ITproject;

import java.math.BigInteger;

public class Key extends DES {

	 private final static int[] permutedLeft_PC1 = new int[]{
	            57, 49, 41, 33, 25, 17, 9,
	            1, 58, 50, 42, 34, 26, 18,
	            10, 2, 59, 51, 43, 35, 27,
	            19, 11, 3, 60, 52, 44, 36
	    };

	    private final static int[] permutedRight_PC1 = new int[]{
	            63, 55, 47, 39, 31, 23, 15,
	            7, 62, 54, 46, 38, 30, 22,
	            14, 6, 61, 53, 45, 37, 29,
	            21, 13, 5, 28, 20, 12, 4
	    };

	    private final static int[][] permutedArray_PC1 = new int[][]{permutedLeft_PC1, permutedRight_PC1};

	    private final static int[] permuted_PC2 = new int[]{
	            14, 17, 11, 24, 1, 5, 3, 28,
	            15, 6, 21, 10, 23, 19, 12, 4,
	            26, 8, 16, 7, 27, 20, 13, 2,
	            41, 52, 31, 37, 47, 55, 30, 40,
	            51, 45, 33, 48, 44, 49, 39, 56,
	            34, 53, 46, 42, 50, 36, 29, 32
	    };

	    private final static int[][] shifts = new int[][]{
	            {1, 2, 9, 16}, /* 1 shift */
	            {3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15} /* 2 shifts */
	    };

	    private int currentRound, maxRound;
	    private int[] origin;
	    private int[][] bits;
	    private int[][] subkeys;

	    public Key(String hex) {
	        this(Bits.toBits(hex, 64));
	    }

	    public Key(final int[] key) throws IllegalArgumentException {
	        if (!verifyKey(key)) return;

	        origin = key.clone();
	        maxRound = -1;
	        currentRound = 0;
	    }
	    
	    public void generateSubkeys(int maxRound) {
	        if (maxRound <= 0 || this.maxRound == maxRound) return;

	        PC1();

	        currentRound = 0;
	        this.maxRound = maxRound;
	        subkeys = new int[this.maxRound][48];

	        for (int i = 0; i < this.maxRound; i++) {
	            subkeys[i] = getNextKey();
	        }
	    }

	    private int[] getNextKey() {
	        currentRound++;

	        for (int[] bit : bits)
	            circularLeftShift(bit, getShifts());

	        return PC2();
	    }

	    private void PC1() {
	    	
	        bits = new int[2][];
	        for (int i = 0; i < bits.length; i++) bits[i] = new int[28];

	        for (int i = 0; i < bits.length; i++) {
	            for (int j = 0; j < bits[i].length; j++) {
	                bits[i][j] = origin[permutedArray_PC1[i][j] - 1];
	            }
	        }
	    }

	    private int[] PC2() {
	        int[] retVal = new int[48];

	        for (int i = 0; i < retVal.length; i++) {
	            if (permuted_PC2[i] <= 28) retVal[i] = bits[0][permuted_PC2[i] - 1];
	            else if (permuted_PC2[i] > 28) retVal[i] = bits[1][(permuted_PC2[i] - 28) - 1];
	        }

	        return retVal;
	    }

	    private int getShifts() {
	        for (int i = 0; i < shifts.length; i++) {
	            for (int j = 0; j < shifts[i].length; j++) {
	                if (shifts[i][j] == currentRound) return (i + 1);
	            }
	        }
	        return 0;
	    }

	    private void circularLeftShift(int[] bits, int n) {
	        int temp = bits[0];
	        for (int i = 0; i < n; i++) {
	            System.arraycopy(bits, 1, bits, 0, bits.length - 1);
	            bits[bits.length - 1] = temp;
	            temp = bits[0];
	        }
	    }

	    private boolean verifyKey(final int[] key) throws IllegalArgumentException {
	        if (key.length != 64) throw new IllegalArgumentException("Key has to be exactly 64 indices long!");

	        for (int i = 1; i < 9; i++) {
	            int pos = (i * 8) - 1;
	            int sum = 0;

	            for (int j = pos - 1; j > pos - 8; j--)
	                sum += key[j];

	            if (key[pos] != (sum + 1) % 2)
	                throw new IllegalArgumentException(String.format("Wrong parity bit (%s) at array index %s (Byte %s) | Sum was %s | Expected parity bit is %s", key[pos], pos, i, sum, ((sum + 1) % 2)));
	        }
	        return true;
	    }

	    public int[] getSubkey(int round) throws IndexOutOfBoundsException {
	        if (round < 1 || round > maxRound)
	            throw new IndexOutOfBoundsException("Subkey round " + round + " does not exist!");
	        return subkeys[round - 1].clone();
	    }

	    public int[] getOrigin() {
	        return origin.clone();
	    }
}
