import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordCracker {

    private static final String TARGET_HASH = "2a2375e1171723a0e04a3c49adccb4ec6db86b2f7527db45e0bb84d8d76a9b9d3536d39e01b92d303fc966b36aa73475f9aea541d63f5ad894a50dda63b68a1c";

    public static void main(String[] args) {
        char[] chars = {'a', 'b', 'c', 'd', 'e'};
        for (char c1 : chars) {
            for (char c2 : chars) {
                for (char c3 : chars) {
                    for (char c4 : chars) {
                        for (char c5 : chars) {
                            String password = String.valueOf(new char[]{c1, c2, c3, c4, c5});
                            String hash = hashPassword(password);
                            if (TARGET_HASH.equals(hash)) {
                                System.out.println("Пароль найден: " + password);
                                return;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Пароль не найден.");
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-512 алгоритм не найден: " + e.getMessage());
            return null;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
