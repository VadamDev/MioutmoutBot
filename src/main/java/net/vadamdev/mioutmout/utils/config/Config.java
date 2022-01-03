package net.vadamdev.mioutmout.utils.config;

import net.vadamdev.mioutmout.utils.config.json.JSONReader;
import net.vadamdev.mioutmout.utils.config.json.JSONWriter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Config {
    private File file;
    private JSONObject jsonObject;

    public Config(File dataFolder, String path) throws IOException {
        file = new File(dataFolder, path);

        if(!file.exists()) jsonObject = new JSONObject();
        else jsonObject = new JSONReader(file).toJSONObject();
    }

    public String getString(String key) {
        return jsonObject.getString(key);
    }

    public int getInt(String key) {
        if(!jsonObject.has(key)) jsonObject.put(key, 0);
        return jsonObject.getInt(key);
    }

    public void set(String key, Object value) {
        jsonObject.put(key, value);
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void save() {
        try {
            JSONWriter jsonWriter = new JSONWriter(file);
            jsonWriter.write(jsonObject);
            jsonWriter.flush();
        }catch(IOException e) { e.printStackTrace(); }
    }
}
