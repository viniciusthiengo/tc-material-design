package br.com.thiengo.tcmaterialdesign.network;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.thiengo.tcmaterialdesign.domain.WrapObjToNetwork;

/**
 * Created by viniciusthiengo on 7/26/15.
 */
public class NetworkConnection {
    private static NetworkConnection instance;
    private Context mContext;
    private RequestQueue mRequestQueue;


    public NetworkConnection(Context c){
        mContext = c;
        mRequestQueue = getRequestQueue();
    }


    public static NetworkConnection getInstance( Context c ){
        if( instance == null ){
            instance = new NetworkConnection( c.getApplicationContext() );
        }
        return( instance );
    }


    public RequestQueue getRequestQueue(){
        if( mRequestQueue == null ){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return(mRequestQueue);
    }


    public <T> void addRequestQueue( Request<T> request ){
        getRequestQueue().add(request);
    }


    public void execute( final Transaction transaction, String tag ){
        WrapObjToNetwork obj = transaction.doBefore();
        Gson gson = new Gson();

        if( obj == null ){
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("jsonObject", gson.toJson(obj));

        CustomRequest request = new CustomRequest(Request.Method.POST,
            "http://192.168.25.221:8888/TCMaterialDesign/package/ctrl/CtrlCar.php",
            params,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    transaction.doAfter(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("LOG", "onErrorResponse(): "+error.getMessage());
                    transaction.doAfter(null);
                }
            });

        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequestQueue(request);
    }
}
