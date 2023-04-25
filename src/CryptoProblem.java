import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CryptoProblem {
    static final String M = "I Love Data Security Class So Much. It is so great :)";
    static byte[] msgBytes;
    static ArrayList<ArrayList<Integer>> res;

    public static void main(String[] args) {
        msgBytes = M.getBytes(StandardCharsets.UTF_8);
        int[] kArr = {2, 4, 6, 8};

        res = new ArrayList<>(kArr.length);
        for (int j : kArr) {
            runProgram(j);
        }

        printResults(kArr);
    }

    private static void printResults(int[] kArr) {
        System.out.println();
        for (int i = 0; i < kArr.length; i++) {
            System.out.println("Statistics for k = " + kArr[i]);
            ArrayList<Integer> list = res.get(i);

//            for (int val : list) {
//                System.out.print(val + " ");
//            }
            System.out.println();
            System.out.println("Average no. of tries = " + getAverage(list));
            System.out.println("Standard Deviation of tries = " + getStandardDeviation(list));
            System.out.println();
        }
    }

    static void runProgram(int k) {
        ArrayList<Integer> numTriesList = new ArrayList<>();
        //Run program 10 times
        for (int i = 0; i < 10; i++) {
//            System.out.println("********************************************************");
//            System.out.println("Iteration " + (i + 1) + " for k =" + k);
            work(k, numTriesList);
        }
        res.add(numTriesList);
    }

    static void work(int k, ArrayList<Integer> numTriesList) {
        int noOfTries = 0;
        boolean flag = true;
        while (flag) {
            noOfTries++;
            //System.out.println("Try No.  " + noOfTries);
            byte[] nonce = generateNonce();
            byte[] concatenatedByteArray = concatenate(msgBytes, nonce);
            byte[] hash = sha256(concatenatedByteArray);
            boolean res = checkFirstKBits(hash, k);
           // System.out.println("Result " + res);
            if (res) {
                flag = false;
                numTriesList.add(noOfTries);
                printResults(hash,nonce,noOfTries);
            }
        }

    }

    private static void printResults(byte[] hash, byte[] nonce, int noOfTries) {
        toHexString(hash);
        System.out.print(",");
        toHexString(nonce);
        System.out.print(","+noOfTries);
        System.out.println();
    }

    public static void toHexString(byte[] byteArray) {
        System.out.print(new BigInteger(1, byteArray).toString(16));
    }


    public static byte[] concatenate(byte[]... arrays) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (arrays != null) {
            Arrays.stream(arrays).filter(Objects::nonNull)
                    .forEach(array -> out.write(array, 0, array.length));
        }
        return out.toByteArray();
    }

    static byte[] sha256(byte[] array) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(array);
            return hash;
            //return String.format("%064x", new BigInteger(1, hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
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
        return nonce;
    }

    static boolean checkFirstKBits(byte[] hash, int k) {
        String binaryMsgHash = convertByteArrayToBinaryString(hash);
        //System.out.println("1-> " + binaryMsgHash);

        for (int i = 0; i < k; i++) {
            if (binaryMsgHash.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    static String convertByteArrayToBinaryString(byte[] barr) {
        StringBuilder binary = new StringBuilder();
        for (byte b : barr) {
            binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return binary.toString();
    }

    public static float getAverage(ArrayList<Integer> list) {
        float sum = 0;
        for (int ele : list) {
            sum += ele;
        }
        return sum / 10;
    }

    public static float getStandardDeviation(ArrayList<Integer> list) {
        float avg = getAverage(list);
        float sd = 0;
        for (float num : list) {
            sd += Math.pow(num - avg, 2);
        }
        return (float) Math.sqrt(sd / 10);
    }
}