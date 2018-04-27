package com.github.holyloop.secure.shiro.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;

import com.github.holyloop.util.TwoTuple;

public class CredentialEncrypter {

    private static RandomNumberGenerator rng = new SecureRandomNumberGenerator();

    /**
     * hash + salt
     * 
     * @param plainTextPassword
     *            明文密码
     * @return
     */
    public static TwoTuple<String, String> saltedHash(String plainTextPassword) {
        if (StringUtils.isEmpty(plainTextPassword)) {
            throw new IllegalArgumentException("plainTextPassword must not be null");
        }

        Object salt = rng.nextBytes();
        String hash = new Sha256Hash(plainTextPassword, salt, 1024).toBase64();
        return new TwoTuple<>(hash, salt.toString());
    }

}
