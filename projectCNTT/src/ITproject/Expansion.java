package ITproject;

public class Expansion extends DES{
	
	private final static int[] expansion = new int[]{
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    public static int[] expand(final int[] bits) throws IllegalArgumentException {
        if (bits.length != 32) throw new IllegalArgumentException("Bit array has to be exactly 32 indices long!");

        int[] retVal = new int[48];

        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = bits[expansion[i] - 1];
        }

        return retVal;
    }

}
