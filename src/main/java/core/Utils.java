package core;

import java.io.FileInputStream;
import java.util.Properties;

public class Utils {
    public static Properties getCredentialProperties(String path) {
        Properties credProperties = new Properties();
        try (FileInputStream in = new FileInputStream("src/test/resources/tokens.properties")) {
            credProperties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return credProperties;
    }
}
