package com.iljaproject.shortify.util;

import com.iljaproject.shortify.constants.EncoderConstants;

public final class Base62Encoder {
    private Base62Encoder(){}

    public static String encode(Long id) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.insert(0, EncoderConstants.Base62Characters.charAt((int) (id % 62)));
            id /= 62;
        } while (id > 0);
        return sb.toString();
    }
}
