package com.voguepay.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

/**
 * @author hp
 */
public final class PublicKeyUtil {
    
    private PublicKeyUtil() {}

    public static byte [] fromString(String text) throws IOException{
        return toDecodedBytes(text);
    }
    
    public static byte [] from(InputStream in) throws IOException{
        return toDecodedBytes(getInputStreamContentAndCloseStream(in));
    }
    
    public static byte [] fromFile(String filepath) throws IOException{
        return toDecodedBytes(getPublicKeyFileContent(filepath));
    }

    private static byte[] toDecodedBytes(String key) {
        key = key.replace("-", "");
        key = key.replace("BEGIN", "");
        key = key.replace("END", "");
        key = key.replace("PUBLIC", "");
        key = key.replace("KEY", "");
        key = key.replace("\n", "");
        key = key.trim();
        return Base64.getDecoder().decode(key);
    }
    
    private static String getPublicKeyFileContent(String location) throws IOException{
        if(hasProtocol(location)) {
            return getFileContent(location);
        }else{
            return getResourceContent(location);
        }
    }
    
    private static boolean hasProtocol(String location) {
        int n = location.indexOf('/');
        if(n == -1) {
            n = location.indexOf('\\');
        }
        return n == -1 ? false : location.indexOf(':', n + 1) != -1;
    }
    
    private static String getFileContent(String filePath) throws IOException{
        byte[] encoded = Files.readAllBytes(new File(filePath).toPath());
        return new String(encoded);
    }
    
    private static String getResourceContent(String resourcePath) throws IOException{
        return getInputStreamContentAndCloseStream(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resourcePath));
    }

    private static String getInputStreamContentAndCloseStream(InputStream inputStream) throws IOException{
        StringBuilder buffer = new StringBuilder();
        try(InputStream in = inputStream) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.US_ASCII));
            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line);
            }
        }
        return buffer.toString();
    }
}
