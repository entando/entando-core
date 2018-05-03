/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.aps.util;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author paddeo
 */
public class BlowfishApsEncrypter implements IApsEncrypter {

    public static String decrypt(String source) {
        try {
            Key key = getKey();
            Cipher bfCipher = Cipher.getInstance(TRIPLE_BLOWFISH);
            byte[] dec = Base64.decodeBase64(source.getBytes());
            bfCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = bfCipher.doFinal(dec);
            // Return the clear text
            return new String(cleartext);
        } catch (Throwable t) {
            throw new RuntimeException("Error decrypting string", t);
        }
    }

    public static String decrypt(String keyString, String source) {
        try {
            Key key = getKey(keyString);
            Cipher bfCipher = Cipher.getInstance(TRIPLE_BLOWFISH);
            byte[] dec = Base64.decodeBase64(source.getBytes());
            bfCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = bfCipher.doFinal(dec);
            // Return the clear text
            return new String(cleartext);
        } catch (Throwable t) {
            throw new RuntimeException("Error decrypting string", t);
        }
    }

    public static String encryptString(String plainText) throws ApsSystemException {
        String encryptedString = null;
        try {
            Key key = getKey();
            Cipher bfCipher = Cipher.getInstance(TRIPLE_BLOWFISH);
            bfCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = plainText.getBytes();
            byte[] ciphertext = bfCipher.doFinal(cleartext);
            encryptedString = new String(Base64.encodeBase64(ciphertext));
        } catch (Throwable t) {
            throw new ApsSystemException("Error detcted while encoding a string", t);
        }
        return encryptedString;
    }

    public static String encryptString(String keyString, String plainText) throws ApsSystemException {
        String encryptedString = null;
        try {
            Key key = getKey(keyString);
            Cipher bfCipher = Cipher.getInstance(TRIPLE_BLOWFISH);
            bfCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = plainText.getBytes();
            byte[] ciphertext = bfCipher.doFinal(cleartext);
            encryptedString = new String(Base64.encodeBase64(ciphertext));
        } catch (Throwable t) {
            throw new ApsSystemException("Error detcted while encoding a string", t);
        }
        return encryptedString;
    }

    public static byte[] encryptBytes(String plainText) throws ApsSystemException {
        byte[] ciphertext = null;
        try {
            Key key = getKey();
            Cipher bfCipher = Cipher.getInstance(TRIPLE_BLOWFISH);
            bfCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = plainText.getBytes();
            ciphertext = bfCipher.doFinal(cleartext);
        } catch (Throwable t) {
            throw new ApsSystemException("Error detcted while encoding a string", t);
        }
        return ciphertext;
    }

    @Override
    public String encrypt(String text) throws ApsSystemException {
        return BlowfishApsEncrypter.encryptString(text);
    }

    public static Key getKey() {
        try {
            // hack for JCE Unlimited Strength
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, false);

            byte[] bytes = getKeyString().getBytes();
            SecretKey s = new SecretKeySpec(bytes, TRIPLE_BLOWFISH_KEY_SPEC);
            return s;
        } catch (Throwable t) {
            throw new RuntimeException("Error creating key", t);
        }
    }

    public static Key getKey(String key) {
        try {
            // hack for JCE Unlimited Strength
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, false);

            byte[] bytes = key.getBytes();
            SecretKey s = new SecretKeySpec(bytes, TRIPLE_BLOWFISH_KEY_SPEC);
            return s;
        } catch (Throwable t) {
            throw new RuntimeException("Error creating key", t);
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

    public static void main(String[] args) throws ApsSystemException {
        String example = "example";
        String encrypted = encryptString(example);
        System.out.println("encrypted pwd: " + encrypted);
//        String decrypted = decrypt(encrypted);
//        System.out.println("decrypted pwd: " + decrypted);
        byte[] encoding = encryptBytes(example);
        System.out.println("Base64:\t " + DatatypeConverter.printBase64Binary(encoding));
        System.out.println("HEX:\t " + bytesToHex(encoding));
    }

    public static String getKeyString() {
        if (StringUtils.isBlank(_keyString)) {
            _keyString = System.getProperties().getProperty("key.string.encryption");
        }
        return _keyString;
    }

    public void setKeyString(String _keyString) {
        this._keyString = _keyString;
    }

    public static final String TRIPLE_BLOWFISH_KEY_SPEC = "Blowfish";
    public static final String TRIPLE_BLOWFISH = "Blowfish/ECB/PKCS5Padding";
    private static String _keyString;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

}
