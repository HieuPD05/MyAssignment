package util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {
    private static final int COST = 10; // đủ mạnh, không chậm

    public static String hash(String raw) {
        if (raw == null) return null;
        return BCrypt.hashpw(raw, BCrypt.gensalt(COST));
    }

    public static boolean verify(String raw, String hash) {
        if (raw == null || hash == null) return false;
        return BCrypt.checkpw(raw, hash);
    }
}
