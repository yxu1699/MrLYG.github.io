package usc.yuangang.es;

import org.json.JSONException;
import org.json.JSONObject;

public interface Callback {
    void onSuccess(JSONObject result) throws JSONException;

    void onFailure(Exception e);
}
