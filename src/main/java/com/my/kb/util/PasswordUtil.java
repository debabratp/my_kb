package com.my.kb.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    //Hash a password
    public static String hashPassword(final String plainPassword) {
        int log_rounds = 12;
        var hashPwd = BCrypt.hashpw(plainPassword, BCrypt.gensalt(log_rounds));
        return (hashPwd);
    }

    // Check that an unencrypted password matches one that has previously been hashed
    public static Boolean checkPassword(final String plainPwd, final String hashedPwd) {
        return BCrypt.checkpw(plainPwd, hashedPwd);
    }
}
