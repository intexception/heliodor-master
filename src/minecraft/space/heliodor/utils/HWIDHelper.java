package space.heliodor.utils;

import org.lwjgl.opengl.GL11;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWIDHelper {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static byte[] generateHWID() {
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-1");
            String s = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name");
            return hash.digest(s.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Algorithm wasn't found.", e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
