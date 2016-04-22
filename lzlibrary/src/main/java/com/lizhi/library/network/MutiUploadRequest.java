package com.lizhi.library.network;

import android.util.Log;

import com.external.volley.AuthFailureError;
import com.external.volley.NetworkResponse;
import com.external.volley.ParseError;
import com.external.volley.Request;
import com.external.volley.Response;
import com.external.volley.http.LoadControler;
import com.external.volley.http.RequestManager;
import com.external.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by jacob on 15/6/17.
 */
public class MutiUploadRequest extends Request<JSONObject> {


    private Map<String, String> mMap;
    private Response.Listener<JSONObject> mListener;
    private MultipartEntity mMultiPartEntity;

    public MutiUploadRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,MultipartEntity multipartEntity) {
        super(Request.Method.POST, url, errorListener);

        mListener = listener;

    }

    //mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    public MultipartEntity getMultiPartEntity() {
        return mMultiPartEntity;
    }

    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // multipart body
            mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
