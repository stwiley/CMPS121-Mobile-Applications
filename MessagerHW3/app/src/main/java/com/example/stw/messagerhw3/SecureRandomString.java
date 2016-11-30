package com.example.stw.messagerhw3;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureRandomString {
    private SecureRandom random = new SecureRandom();

    public String nextString() {

        return new BigInteger(150, random).toString(32);
    }
}
