package service;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {
    public static Map<String, String> getProperties(String filePath) {
        Map<String, String> result = new HashMap<>();
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(filePath)) {
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            result.put((String) entry.getKey(), (String) entry.getValue());
        }
        return result;
    }
}
