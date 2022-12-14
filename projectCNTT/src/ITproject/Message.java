package ITproject;

public class Message {
	
	    private int[] data;
	    private int[][] dataSplit;
	    private int blockLength;

	    public Message(String hex) {
	        this(hex, 64);
	    }

	    public Message(String hex, int blockLength) {
	        this(Bits.toBits(hex, blockLength), blockLength);
	    }

	    public Message(int[] data) {
	        this(data, 64);
	    }

	    public Message(int[] data, int blockLength) {
	        this.blockLength = blockLength;
	        this.data = data;
	        this.data = Bits.expand(data, this.blockLength * getBlockCount());
	        this.dataSplit = splitData();
	    }

	    private int[][] splitData() {
	        int[][] retVal;

	        if (data.length <= blockLength) {
	            retVal = new int[1][blockLength];
	            System.arraycopy(data, 0, retVal[0], 0, data.length);
	        } else {
	            int blockCount = getBlockCount();

	            retVal = new int[blockCount][blockLength];

	            for (int i = 0; i < blockCount; i++) {
	                System.arraycopy(data, i * blockLength, retVal[i], Math.max(0, blockLength - (data.length - (i * blockLength))), Math.min(blockLength, data.length - (i * blockLength)));
	            }
	        }
	        return retVal;
	    }

	    public int[] getData() {
	        return data.clone();
	    }

	    public int[] getBlock(int pos) throws IndexOutOfBoundsException {
	        if (pos < 0 || pos >= getBlockCount())
	            throw new IndexOutOfBoundsException("Block " + pos + " does not exist!");
	        return dataSplit[pos].clone();
	    }

	    public int getBlockCount() {
	        int blockCount = data.length / blockLength;
	        if (data.length % blockLength != 0) blockCount++;
	        return blockCount;
	    }

	    public int getBlockLength() {
	        return this.blockLength;
	    }

	    public String toString() {
	        String hex = Bits.toHex(data);	        
	        return Bits.hexToString(hex);
	    }

}
