package com.jlc.buried.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Base64
 *
 * @author deliang.xie
 * @data 2016/5/17 0017
 * @time 下午 4:37
 */
public class Base64Util {
    private static final char[] BASE64_ENCODECHARS = new char[]{'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] BASE64_DECODECHARS = new byte[]{-1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
            58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1,
            -1, -1, -1, -1};

    public static String encode(String str) {
        return new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
    }

    public static String decode(String str) {
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }

    public static byte[] decBase64(byte[] data) {
        int len = data.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
        int i = 0;
        int b1, b2, b3, b4;

        while (i < len) {

			/* b1 */
            do {
                b1 = BASE64_DECODECHARS[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) {
                break;
            }

			/* b2 */
            do {
                b2 = BASE64_DECODECHARS[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) {
                break;
            }
            buf.write((b1 << 2) | ((b2 & 0x30) >>> 4));

			/* b3 */
            do {
                b3 = data[i++];
                if (b3 == 61) {
                    return buf.toByteArray();
                }
                b3 = BASE64_DECODECHARS[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) {
                break;
            }
            buf.write(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2));

			/* b4 */
            do {
                b4 = data[i++];
                if (b4 == 61) {
                    return buf.toByteArray();
                }
                b4 = BASE64_DECODECHARS[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) {
                break;
            }
            buf.write(((b3 & 0x03) << 6) | b4);
        }
        return buf.toByteArray();
    }

    /**
     * Base64-encode the given data and return a newly allocated String with the
     * result.
     *
     * @param data the data to encode
     */
    public static String encBase64(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(BASE64_ENCODECHARS[b1 >>> 2]);
                sb.append(BASE64_ENCODECHARS[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(BASE64_ENCODECHARS[b1 >>> 2]);
                sb.append(BASE64_ENCODECHARS[((b1 & 0x03) << 4)
                        | ((b2 & 0xf0) >>> 4)]);
                sb.append(BASE64_ENCODECHARS[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(BASE64_ENCODECHARS[b1 >>> 2]);
            sb.append(BASE64_ENCODECHARS[((b1 & 0x03) << 4)
                    | ((b2 & 0xf0) >>> 4)]);
            sb.append(BASE64_ENCODECHARS[((b2 & 0x0f) << 2)
                    | ((b3 & 0xc0) >>> 6)]);
            sb.append(BASE64_ENCODECHARS[b3 & 0x3f]);
        }
        return sb.toString();
    }

}
