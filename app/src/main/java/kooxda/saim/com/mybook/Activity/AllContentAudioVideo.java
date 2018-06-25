package kooxda.saim.com.mybook.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kooxda.saim.com.mybook.Adapter.AdapterCategoryContent;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.MainApiLink;
import kooxda.saim.com.mybook.Utility.MySingleton;

public class AllContentAudioVideo extends AppCompatActivity {

    ProgressDialog progressDialog;

    ArrayList<ModelContent> modelContents = new ArrayList<>();
    RecyclerView recyclerViewAllContentAudioVideo;
    RecyclerView.LayoutManager layoutManagerCategoryContent;
    RecyclerView.Adapter categoryContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_content_audio_video);

        init();
    }

    private void init() {
        String category_name = getIntent().getExtras().getString("CONTENT_TYPE");
        getSupportActionBar().setTitle(category_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait data loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        recyclerViewAllContentAudioVideo = (RecyclerView) findViewById(R.id.recyclerViewAllContentAudioVideo);
        recyclerViewAllContentAudioVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewAllContentAudioVideo.setHasFixedSize(true);

        LoadCategoryContent(category_name);
    }


    private void LoadCategoryContent(final String category_type) {
        progressDialog.show();
        modelContents.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainApiLink.getContentByType,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("API RESPONSE", response);

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");

                            if (code.equals("success")) {

                                JSONArray jsonArrayUser = jsonObject.getJSONArray("user");
                                for (int i=0; i<jsonArrayUser.length(); i++) {
                                    JSONObject jsonObjectUser = jsonArrayUser.getJSONObject(i);

                                    String id = jsonObjectUser.getString("id");
                                    String name = jsonObjectUser.getString("name");
                                    String banner = jsonObjectUser.getString("banner");
                                    String location = jsonObjectUser.getString("location");
                                    String type = jsonObjectUser.getString("type");
                                    String category = jsonObjectUser.getString("category");
                                    String date_time = jsonObjectUser.getString("date_time");

                                    ModelContent modelContentVideo = new ModelContent(id, name, banner, location, type, category, date_time);
                                    modelContents.add(modelContentVideo);
                                }

                                categoryContentAdapter = new AdapterCategoryContent(modelContents);
                                recyclerViewAllContentAudioVideo.setAdapter(categoryContentAdapter);


                            } else {
                                Toast.makeText(getApplicationContext(), "Something Wrong!!!", Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){
                            Log.d("HDHD ", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Request Error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("category_type", category_type);

                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.btbMenuSearch:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
