package com.example.aitagstorage.tools;

import org.springframework.util.DigestUtils;

public class MD5 {
    public static String Encryption(String text, String salt) {
        return DigestUtils.md5DigestAsHex((text + salt).getBytes());
    }
}
