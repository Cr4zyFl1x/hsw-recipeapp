package nrw.florian.cookbook;

import org.json.JSONObject;

public class JSONHelper {
    private final JSONObject jsonObject;

    public JSONHelper(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean hasJsonObject(String object) {
        return this.jsonObject.has(object);
    }
}
