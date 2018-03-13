package com.jlc.buried.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩工具
 *
 * @author deliang.xie
 * @data 2016/5/17 0017
 * @time 下午 4:37
 */
public class GZipUtils {
    //0x78,0x9c为zlib数据头，或者0x0,0x0,0xFF,0xFF也可以实现正常解压缩
    //第一个字节0x1f(31),第二个字节为0x8b(139),标识为GZIP格式

    //gzip解压时的临时缓冲区大小
    private static final int KGZipUncompressBufferSize = 1024;
    //gzip流数据的首字节
    private static final byte KGZipHeaderFirstByte = (byte) 0x1f;
    //gzip流数据的第二个字节
    private static final byte KGZipHeaderSecondByte = (byte) 0x8b;

    private static final byte[] KGZipUncompressBuffer = new byte[KGZipUncompressBufferSize];

    private static final byte[] KEmptyByteArray = new byte[0];


    private GZipUtils() {

    }

    //判断一个数据流是否是GZip
    public static boolean isGZipData(final byte[] data) {

        return data.length > 2
                && data[0] == KGZipHeaderFirstByte
                && data[1] == KGZipHeaderSecondByte;

    }

    //gzip压缩
    public static byte[] gzipCompress(final byte[] data) {

        if (0 == data.length) {
            return KEmptyByteArray;
        }

        byte[] pBuf = KEmptyByteArray;

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(byteArrayOutputStream);
            gzip.write(data);
            gzip.close();
            pBuf = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pBuf;
    }


    public static byte[] gzipUncompress(final byte[] data) {

        byte[] pBuf = KEmptyByteArray;
        if (data.length > 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPInputStream gzipInputStream = null;
            try {

                gzipInputStream = new GZIPInputStream(byteArrayInputStream);

                int byteCount = 0;

                while (byteCount >= 0) {
                    byteCount = gzipInputStream.read(KGZipUncompressBuffer);
                    if (byteCount > 0) {
                        byteArrayOutputStream.write(KGZipUncompressBuffer, 0, byteCount);
                    }

                }

                pBuf = byteArrayOutputStream.toByteArray();

                byteArrayInputStream.close();
                gzipInputStream.close();
                byteArrayOutputStream.close();

            } catch (IOException e) {
                Log.w("gzip uncompressed fail", e.getMessage());
            } finally {

                try {
                    if (null != byteArrayInputStream) {
                        byteArrayInputStream.close();
                    }

                    if (null != byteArrayOutputStream) {
                        byteArrayOutputStream.close();
                    }

                    if (null != gzipInputStream) {
                        gzipInputStream.close();
                    }
                } catch (IOException e) {
                    Log.w("gzip", e.getMessage());
                }
            }
        }

        return pBuf;
    }

    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param srcfile  压缩包的名字
     * @param destFile 指定的路径
     * @throws Exception
     */
    public static void unZipFolder(File srcfile, File destFile) throws Exception {
        unZipFolder(new java.io.FileInputStream(srcfile), destFile);
    }//end of func


    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param input    压缩包的数据流
     * @param destFile 指定的路径
     * @throws Exception
     */
    public static void unZipFolder(InputStream input, File destFile) throws Exception {
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(input);
        java.util.zip.ZipEntry zipEntry = null;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                java.io.File folder = new java.io.File(destFile, szName);
                folder.mkdirs();
            } else {
                java.io.File file = new java.io.File(destFile, szName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                // get the output stream of the file
                java.io.FileOutputStream out = new java.io.FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }//end of while
        inZip.close();
    }

    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception
     */
    public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
        unZipFolder(new java.io.FileInputStream(zipFileString), new File(outPathString));
    }//end of func
}