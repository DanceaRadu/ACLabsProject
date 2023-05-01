package com.aclabs.twitter.auxiliaries;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

record Pair<T, K>(T first, K second) { }
public class PasswordHasher {
    //method that hashes a password and return a pair of Strings containing the hashed password and the salt
    public static Pair<String, String> hashPassword(String password) {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f;
        byte[] hash = new byte[0];
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Error hashing password");
            System.exit(-1);
        }

        Base64.Encoder enc = Base64.getEncoder();
        return new Pair<>("'" + enc.encodeToString(hash) + "'", "'" + enc.encodeToString(salt) + "'");
    }

    //method that hashes the String given as the first argument using the salt, and compares it to the hash
    public static boolean checkPassword(String password, String hash, String salt) {
        byte[] saltBytes;
        saltBytes = Base64.getDecoder().decode(salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 128);
        SecretKeyFactory f;
        byte[] hashByte = new byte[0];
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hashByte = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Error checking password");
        }

        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(hashByte).equals(hash);
    }
}
