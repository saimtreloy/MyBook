package kooxda.saim.com.mybook.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import kooxda.saim.com.mybook.Adapter.AdapterBook;
import kooxda.saim.com.mybook.Model.ModelBook;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.MainApiLink;
import kooxda.saim.com.mybook.Utility.MySingleton;
import kooxda.saim.com.mybook.Utility.SharedPrefDatabase;

public class AllCategory extends AppCompatActivity {

    ProgressDialog progressDialog;

    ArrayList<ModelBook> modelBooks = new ArrayList<>();
    RecyclerView recyclerViewAllBook;
    RecyclerView.LayoutManager layoutManagerAllBook;
    RecyclerView.Adapter allBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        init();
    }

    private void init() {
        getSupportActionBar().setTitle("All Books");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait data loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        recyclerViewAllBook = (RecyclerView) findViewById(R.id.recyclerViewAllBook);
        recyclerViewAllBook.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewAllBook.setHasFixedSize(true);

        LoadCategory();
    }

    private void LoadCategory() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
