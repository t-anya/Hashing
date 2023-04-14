
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Main {

    static final String SHA_256 = "SHA-256";
    public static void main(String[] args) {

        String M = "I Love Data Security Class So Much. It is so great :)";
        int[]k = {2,4,6,8};
        runProgram(M,k);

    }

    static void runProgram(String M, int[]k) {
        for (int i = 0; i < 10; i++) {
            System.out.println("Iteration " + i + "for k =" + k[0] + "");
            work(M,k[0]);
        }

    }

    static void work(String M, int k) {
        byte[] hashedMsg = hashMessage(M);
        System.out.println("Hashed Msg " + hashedMsg.toString());
        if(hashedMsg != null) {
            int noOfTries = 0;
            boolean flag = true;

            while(flag && noOfTries < 5) {
                noOfTries++;
                System.out.println("Try No.  " + noOfTries);
                byte[] nonce =  generateNonce();
                System.out.println("Nonce " + nonce.toString());
                boolean result = checkFirstKBits(hashedMsg,nonce,k);
                System.out.println("Result " + result);
                if (result) {
                    flag = false;
                    printResults(noOfTries,nonce,M);
                } 
            }
        } else {
            System.out.println("Hashed Value is null.");
        }
    }

    private static void printResults(int noOfTries, byte[] nonce, String M) {
        String nonceStr = convertByteArrayToString(nonce);
        String newMsg = M + nonceStr;
        byte[] newHash = hashMessage(newMsg);

        //todo null check
        BigInteger bigIntegerNewHash = new BigInteger(newHash);
        String hexaHash = bigIntegerNewHash.toString(16);

        BigInteger bigIntegerNewNonce = new BigInteger(nonce);
        String hexaNonce = bigIntegerNewNonce.toString(16);

        System.out.println(hexaHash + "," + hexaNonce + noOfTries);
    }

    static String convertByteArrayToString(byte[] barr) {
       return new String(barr, StandardCharsets.UTF_8);
    }

    static byte[] hashMessage(String msg){
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256);
            return md.digest(msg.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    static byte[] generateNonce() {
        //64 bit nonce -> 8 bytes
        byte[] nonce = new byte[8];
        SecureRandom prng = null;
        try {
            prng = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        prng.nextBytes(nonce);
        return  nonce;
    }

    static boolean checkFirstKBits(byte[] msgHash, byte[] nonce, int k) {
        BigInteger bigIntegerMsgHash = new BigInteger(msgHash);
        String binaryMsgHash = bigIntegerMsgHash.toString(2);
        System.out.println("1-> " + binaryMsgHash);

        BigInteger bigIntegerNonce = new BigInteger(nonce);
        String binaryNonce = bigIntegerNonce.toString(2);
        System.out.println("2-> " + binaryMsgHash);


        for (int i = 0; i < k; i++) {
            if (binaryMsgHash.charAt(i) != '0' && binaryNonce.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    static void printByteArray(byte[] bytes){
        for (byte b1 : bytes){
            String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
            s1 += " " + Integer.toHexString(b1);
            s1 += " " + b1;
            System.out.println(s1);
        }
    }

    public static void foo( String s) {
        try {

            // taking input string
            String charsetName = "UTF-16";
            byte[] byteArray = s.getBytes(charsetName);

            System.out.println(Arrays.toString(byteArray));

            System.out.println("Length of String"
                    + " " + s.length() + " "
                    + "Length of byte Array"
                    + " " + byteArray.length);
        } catch (Exception e) {
            System.out.println("Unsupported charset :" + e);
        }
    }

}