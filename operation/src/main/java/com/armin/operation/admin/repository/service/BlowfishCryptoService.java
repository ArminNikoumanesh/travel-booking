package com.armin.operation.admin.repository.service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class BlowfishCryptoService {
    private static final String KEY = "75C8nwD1xpNDI53zDeqTyJpwG8IbC0ITUskI";
    private static final String BLOWFISH = "Blowfish";

    public static String encrypt(String text) throws
            NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), BLOWFISH);
        Cipher cipher = Cipher.getInstance(BLOWFISH);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
    }

    public static String decrypt(String text) throws
            NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), BLOWFISH);
        Cipher cipher = Cipher.getInstance(BLOWFISH);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
    }
}