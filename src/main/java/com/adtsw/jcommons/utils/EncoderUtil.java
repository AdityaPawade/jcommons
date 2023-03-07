package com.adtsw.jcommons.utils;

import com.adtsw.jcommons.models.EncodingFormat;

import java.io.IOException;

public class EncoderUtil {

    public static String encode(EncodingFormat storageFormat, String payload) {
        String encodedPayload = null;
        switch (storageFormat) {
            case STRING:
                encodedPayload = payload;
                break;
            case GZIP_WITH_BASE64:
                try {
                    encodedPayload = CompressionUtil.compress(payload);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
                break;
        }
        return encodedPayload;
    }

    public static String decode(EncodingFormat storageFormat, String storedPayload) {
        String decodedPayload = null;
        switch (storageFormat) {
            case STRING:
                decodedPayload = storedPayload;
                break;
            case GZIP_WITH_BASE64:
                try {
                    decodedPayload = CompressionUtil.decompress(storedPayload);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
                break;
        }
        return decodedPayload;
    }
}
