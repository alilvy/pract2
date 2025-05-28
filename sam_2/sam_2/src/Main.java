import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
class AESDecrypter {

    public static void main(String[] args) {
        String inputFile = "secret_text.bin";
        String outputFile = "decrypted_file.txt";

        try {
            byte[] encryptedData = Files.readAllBytes(Paths.get(inputFile));

            // Извлекаем IV и ключ из файла (проверяем размер)
            if (encryptedData.length < 32) {
                System.err.println("Файл слишком мал. Недостаточно данных для IV и ключа.");
                return;
            }

            byte[] iv = new byte[16];
            byte[] key = new byte[16];

            System.arraycopy(encryptedData, 0, iv, 0, 16);
            System.arraycopy(encryptedData, 16, key, 0, 16);

            // Остаток данных - зашифрованный текст с тегом аутентификации
            int ciphertextLength = encryptedData.length - 32;
            if (ciphertextLength <= 0) {
                System.err.println("Файл слишком мал. Недостаточно данных для зашифрованного текста.");
                return;
            }
            byte[] ciphertextWithTag = new byte[ciphertextLength];
            System.arraycopy(encryptedData, 32, ciphertextWithTag, 0, ciphertextLength);

            // Инициализация Cipher
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

            // Расшифровка
            byte[] decryptedData = cipher.doFinal(ciphertextWithTag);

            // Запись в файл
            Files.write(Paths.get(outputFile), decryptedData);

            System.out.println("Файл успешно расшифрован и сохранен в " + outputFile);

        } catch (Exception e) {
            System.err.println("Ошибка при расшифровке: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


