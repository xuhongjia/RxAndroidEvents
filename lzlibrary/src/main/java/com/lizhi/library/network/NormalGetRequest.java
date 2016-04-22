package com.lizhi.library.network;

import com.external.volley.Response;
import com.external.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class NormalGetRequest extends JsonObjectRequest {

    public NormalGetRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url,"", listener, errorListener);
    }

}