package ITproject;

public class DES {
	
    private BLOCK_MODE mode;

    public enum BLOCK_MODE {
        EBC, CBC
    }

    public DES() {
        this(BLOCK_MODE.EBC);
    }

    public DES(BLOCK_MODE mode) {
        this.mode = mode;
    }
    
    public Message encrypt(Message msg, Key key) {
        key.generateSubkeys(16);

        int[] encrypted = new int[msg.getBlockCount() * 
                                  msg.getBlockLength()];

        for (int i = 0; i < msg.getBlockCount(); i++) {
            int[] temp = cipher(msg.getBlock(i), key, true);
            System.arraycopy(temp, 0, encrypted, i * 
            		msg.getBlockLength(), temp.length);
        }
        return new Message(encrypted);
    }

    public Message decrypt(Message msg, Key key) {
        key.generateSubkeys(16);

        int[] decrypted = new int[msg.getBlockCount() * 
                                  msg.getBlockLength()];

        for (int i = 0; i < msg.getBlockCount(); i++) {
            int[] temp = cipher(msg.getBlock(i), key, false);
            System.arraycopy(temp, 0, decrypted, i *
            		msg.getBlockLength(), temp.length);
        }
        return new Message(decrypted);
    }

    private int[] cipher(int[] block, Key key, boolean encrypt) {
        int[][] parts = new int[2][32]; 

        block = Permutation.IP(block);

        System.arraycopy(block, 0, parts[0], 0, 32); //left
        System.arraycopy(block, 32, parts[1], 0, 32); //right

        int[] rightOld, retVal;

        for (int i = 0; i < 16; i++) {
            rightOld = parts[1];

            if (encrypt)
                parts[1] = F(key.getSubkey(i + 1), parts[1]);
            else
                parts[1] = F(key.getSubkey(16 - i), parts[1]);

            parts[1] = Bits.xor(parts[1], parts[0]);

            parts[0] = rightOld;
        }
        retVal = new int[64];
        System.arraycopy(parts[1], 0, retVal, 0, 32);
        System.arraycopy(parts[0], 0, retVal, 32, 32);

        return Permutation.IP_Inversed(retVal);
    }

    private int[] F(int[] subkey, int[] part) {
	    
        int[] retVal;

        retVal = Expansion.expand(part);
        retVal = Bits.xor(retVal, subkey);
        retVal = SBoxes.encrypt(retVal);
        retVal = Permutation.P(retVal);

        return retVal;
    }

    public BLOCK_MODE getMode() {
        return mode;
    }

}
