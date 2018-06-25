package kooxda.saim.com.mybook.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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

import kooxda.saim.com.mybook.Adapter.AdapterBook;
import kooxda.saim.com.mybook.Adapter.AdapterCategoryContent;
import kooxda.saim.com.mybook.Model.ModelBook;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.MainApiLink;
import kooxda.saim.com.mybook.Utility.MySingleton;
import kooxda.saim.com.mybook.Utility.SharedPrefDatabase;

public class SearchActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    ArrayList<ModelBook> modelBooks = new ArrayList<>();
    RecyclerView recyclerViewAllBook;
    RecyclerView.LayoutManager layoutManagerAllBook;
    RecyclerView.Adapter allBookAdapter;

    ArrayList<ModelContent> modelContents = new ArrayList<>();
    RecyclerView recyclerViewAllContentAudioVideo;
    RecyclerView.LayoutManager layoutManagerCategoryContent;
    RecyclerView.Adapter categoryContentAdapter;

    RadioButton radioBook, radioContent;
    EditText inputSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
    }

    private void init() {
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        recyclerViewAllBook = (RecyclerView) findViewById(R.id.recyclerViewSearchAllBook);
        recyclerViewAllBook.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewAllBook.setHasFixedSize(true);

        recyclerViewAllContentAudioVideo = (RecyclerView) findViewById(R.id.recyclerViewSearchAllContent);
        recyclerViewAllContentAudioVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewAllContentAudioVideo.setHasFixedSize(true);

        radioBook = (RadioButton) findViewById(R.id.radioBook);
        radioContent = (RadioButton) findViewById(R.id.radioContent);

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (radioBook.isChecked()) {
                    filterBook(editable.toString());
                } else {
                    filterContent(editable.toString());
                }

            }
        });
        RadioButtonChange();

    }

    public void RadioButtonChange() {
        radioBook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    recyclerViewAllBook.setVisibility(View.VISIBLE);
                    recyclerViewAllContentAudioVideo.setVisibility(View.GONE);
                    LoadAllCategory();
                }
            }
        });
        radioContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    recyclerViewAllBook.setVisibility(View.GONE);
                    recyclerViewAllContentAudioVideo.setVisibility(View.VISIBLE);
                    LoadCategoryContent("All");
                }
            }
        });
    }

    private void LoadAllCategory() {
        progressDialog.show();
        modelBooks.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainApiLink.getCategory,
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
                                    String category_name = jsonObjectUser.getString("category_name");
                                    String cover = jsonObjectUser.getString("cover");

                                    ModelBook modelBook = new ModelBook(id, category_name, cover);
                                    modelBooks.add(modelBook);
                                }

                                allBookAdapter = new AdapterBook(modelBooks);
                                recyclerViewAllBook.setAdapter(allBookAdapter);

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
                params.put("user_fullname", new SharedPrefDatabase(getApplicationContext()).RetriveName());

                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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

    void filterBook(String text){
        ArrayList<ModelBook> temp = new ArrayList();
        for(ModelBook d: modelBooks){
            if(d.getCategory_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        allBookAdapter = new AdapterBook(temp);
        recyclerViewAllBook.setAdapter(allBookAdapter);
        allBookAdapter.notifyDataSetChanged();
    }


    void filterContent(String text){
        ArrayList<ModelContent> temp = new ArrayList();
        for(ModelContent d: modelContents){
            if(d.getName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        categoryContentAdapter = new AdapterCategoryContent(temp);
        recyclerViewAllContentAudioVideo.setAdapter(categoryContentAdapter);
        categoryContentAdapter.notifyDataSetChanged();
    }
}
