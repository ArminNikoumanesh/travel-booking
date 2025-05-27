package com.armin.utility.bl;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * The Hash Service Class,
 * Containing Methods about Hashing and Base64 Encoding
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

@Service
public class HashService {

    /**
     * Secret Key For Encrypt With Aes-128 Algorithm
     * Length Can Be 16 , 24 , 32 Byte
     */
    private static final String KEY = "hfgdshaiufkhnrlns43w3d1se4rfcds2";
    private static final SecretKeySpec sKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
    /**
     * Init Vector For Encrypt With Aes-128 Algorithm
     * Length Must Be 16 Byte
     */
    private static final String INIT_VECTOR = "74irhgfkjxsfdkjn";
    private static final IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));

    /**
     * Get Secure Hashed Version of Input Value
     *
     * @param value A {@link String} Instance Representing Input Value
     * @return A {@link String} Instance Representing Hashed Value of Input Value
     * @throws SystemException A Customized {@link RuntimeException} with type of {@link SystemError#HASH_FUNCTION_FAILED} when Hash Algorithm does not Support
     */
    public String hash(String value) throws SystemException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(value.getBytes());
            return this.encode(messageDigest.digest());
        } catch (NoSuchAlgorithmException | RuntimeException e) {
            throw new SystemException(SystemError.HASH_FUNCTION_FAILED, "SHA-256", 1071);
        }
    }

    /**
     * Encode Input Value with Base64 Algorithm
     *
     * @param value A {@link byte[]} Instance Representing Input Value
     * @return A {@link String} Instance Representing Base64 Encoded Value of Input Value
     */
    public String encode(byte[] value) {
        return Base64.getEncoder().withoutPadding().encodeToString(value);
    }

    /**
     * Decode Input Value with Base64 Algorithm
     *
     * @param value A {@link String} Instance Representing Input Value
     * @return A {@link byte[]} Instance Representing Base64 Decoded Value of Input Value
     */
    public byte[] decode(String value) {
        return Base64.getDecoder().decode(value);
    }

    /**
     * Encrypt Input Value With AES-128 Algorithm
     *
     * @param value A {@link String} Instance Representing Input Value
     * @return A {@link String} Instance Representing AES-128 Encrypted Value of Input Value
     */
    public String encrypt(String value) throws SystemException {

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return this.encode(encrypted);
        } catch (Exception ex) {
            throw new SystemException(SystemError.HASH_FUNCTION_FAILED, "AES-128", 1072);
        }
    }

    /**
     * Decrypt Input Value With AES-128 Algorithm
     *
     * @param value A {@link String} Instance Representing Input Value
     * @return A {@link String} Instance Representing AES-128 Decrypted Value of Input Value
     */
    public String decrypt(String value) throws SystemException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
            byte[] original = cipher.doFinal(this.decode(value));
            return new String(original);
        } catch (Exception ex) {
            throw new SystemException(SystemError.HASH_FUNCTION_FAILED, "AES-128", 1073);
        }
    }

}
