package ITproject;


import ITproject.*;


import java.util.Arrays;

public class Main {
	
	 public static void main(String[] args) {

	        String hex = Bits.stringToHex("Dai hoc Su pham ky thuat thanh pho Ho Chi Minh");
	        //hex = 48 65 6C 6C 6F 20 57 6F 72 6C 64 21
	        Message msg = new Message(hex);
	        System.out.println(msg.toString());

                    Key key = new Key("6D62B676B5F7B932");

	        DES des = new DES();
	        Message enc = des.encrypt(msg, key);
	        System.out.println(enc.toString());	        

	        Message dec = des.decrypt(enc, key);        
	        System.out.println(dec.toString());
	    }

}
