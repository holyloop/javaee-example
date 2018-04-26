package com.github.holyloop.secure.shiro.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;

public class CredentialEncrypter {

    private static RandomNumberGenerator rng = new SecureRandomNumberGenerator();

    /**
     * salt + hash
     * 
     * @param plainTextPassword
     *            明文密码
     * @return
     */
    public String saltedHash(String plainTextPassword) {
        if (StringUtils.isEmpty(plainTextPassword)) {
            throw new IllegalArgumentException("plainTextPassword must not be null");
        }

        Object salt = rng.nextBytes();

        /*
         * TODO: save user and salt
         * User user = new User(username, hashedPasswordBase64);
         * user.setPasswordSalt(salt.toString()); userDAO.create(user);
         */
        return new Sha256Hash(plainTextPassword, salt, 1024).toBase64();
    }

}
