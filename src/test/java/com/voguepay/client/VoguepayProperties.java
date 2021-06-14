package com.voguepay.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author hp
 */
public final class VoguepayProperties {
    
    private final String commandApiToken;
    
    private final String publicKeyPath;
    
    public VoguepayProperties(String propertiesFilePath) {
    
        try(InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(propertiesFilePath)) {
        
            Properties props = new Properties();
            props.load(in);

            this.commandApiToken = props.getProperty("commandApiToken");
            this.publicKeyPath = props.getProperty("publicKeyPath");
        }catch(IOException e) {
            throw new RuntimeException("", e);
        }
    }

    public final String getCommandApiToken() {
        return commandApiToken;
    }

    public final String getPublicKeyPath() {
        return publicKeyPath;
    }

    @Override
    public String toString() {
        return "VoguepayProperties{" + "commandApiToken has value=" + 
                (commandApiToken != null && ! commandApiToken.isEmpty()) + 
                ", publicKeyPath=" + publicKeyPath + '}';
    }
}
