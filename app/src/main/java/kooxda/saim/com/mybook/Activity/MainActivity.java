package kooxda.saim.com.mybook.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kooxda.saim.com.mybook.Adapter.AdapterBook;
import kooxda.saim.com.mybook.Adapter.AdapterContentVideo;
import kooxda.saim.com.mybook.Model.ModelBook;
import kooxda.saim.com.mybook.Model.ModelCategoryBanner;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.MainApiLink;
import kooxda.saim.com.mybook.Utility.MySingleton;
import kooxda.saim.com.mybook.Utility.SharedPrefDatabase;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;

public class MainActivity extends AppCompatActivity {

    public static Toolbar toolbar;
    ProgressDialog progressDialog;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle;

    ArrayList<ModelCategoryBanner> modelCategoryBanners = new ArrayList<>();
    BannerSlider bannerSlider;
    List<Banner> banners;

    ArrayList<ModelBook> modelBooks = new ArrayList<>();
    RecyclerView recyclerViewAllBook;
    RecyclerView.LayoutManager layoutManagerAllBook;
    RecyclerView.Adapter allBookAdapter;

    //recyclerViewContentVideo
    ArrayList<ModelContent> modelContentsVideo = new ArrayList<>();
    RecyclerView recyclerViewContentAudio;
    RecyclerView.LayoutManager layoutManagerAudio;
    RecyclerView.Adapter audioAdapter;


    ArrayList<ModelContent> modelContentsAudio = new ArrayList<>();
    RecyclerView recyclerViewContentVideo;
    RecyclerView.LayoutManager layoutManagerVideo;
    RecyclerView.Adapter videoAdapter;

    TextView txtAllBookList;
    TextView txtAllVideo;
    TextView txtAllAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeMainActivity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait data loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        actionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        ImageView profile_image = (ImageView) headerView.findViewById(R.id.profile_image);
        TextView txtProfileName = (TextView) headerView.findViewById(R.id.txtProfileName);

        Picasso.with(getApplicationContext()).
                load(new SharedPrefDatabase(getApplicationContext()).RetrivePhoto()).
                placeholder(R.drawable.ic_logo).
                error(R.drawable.ic_logo).
                into(profile_image);

        txtProfileName.setText(new SharedPrefDatabase(getApplicationContext()).RetriveName());


        recyclerViewAllBook = (RecyclerView) findViewById(R.id.recyclerViewAllBook);
        recyclerViewAllBook.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAllBook.setHasFixedSize(true);

        recyclerViewContentVideo = (RecyclerView) findViewById(R.id.recyclerViewContentVideo);
        recyclerViewContentVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewContentVideo.setHasFixedSize(true);

        recyclerViewContentAudio = (RecyclerView) findViewById(R.id.recyclerViewContentAudio);
        recyclerViewContentAudio.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewContentAudio.setHasFixedSize(true);


        txtAllBookList = (TextView) findViewById(R.id.txtAllBookList);
        txtAllBookList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllCategory.class));
            }
        });
        txtAllVideo = (TextView) findViewById(R.id.txtAllVideo);
        txtAllVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllContentAudioVideo.class);
                intent.putExtra("CONTENT_TYPE", "Video");
                startActivity(intent);
            }
        });
        txtAllAudio = (TextView) findViewById(R.id.txtAllAudio);
        txtAllAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllContentAudioVideo.class);
                intent.putExtra("CONTENT_TYPE", "Audio");
                startActivity(intent);
            }
        });


        NavigationItemClicked();
        LoadCategoryBanner();
        BannerClicked();
        haveStoragePermission();
    }


    public void NavigationItemClicked() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btbMenuHome) {
                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.btbMenuProfile) {
                    drawerLayout.closeDrawers();
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                } else if (item.getItemId() == R.id.btbMenuCategory) {
                    drawerLayout.closeDrawers();
                    startActivity(new Intent(getApplicationContext(), AllCategory.class));
                } else if (item.getItemId() == R.id.btbMenuVideo) {
                    drawerLayout.closeDrawers();
                    Intent intent = new Intent(getApplicationContext(), AllContentAudioVideo.class);
                    intent.putExtra("CONTENT_TYPE", "Video");
                    startActivity(intent);
                } else if (item.getItemId() == R.id.btbMenuAudio) {
                    drawerLayout.closeDrawers();
                    Intent intent = new Intent(getApplicationContext(), AllContentAudioVideo.class);
                    intent.putExtra("CONTENT_TYPE", "Audio");
                    startActivity(intent);
                }else if (item.getItemId() == R.id.btbMenuExit) {
                    drawerLayout.closeDrawers();
                    AlertExit();
                }
                return false;
            }
        });
    }

    private void BannerClicked() {
        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getApplicationContext(), modelCategoryBanners.get(position).getCategory_name(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), CategoryContent.class).putExtra("CATEGORY_NAME", modelCategoryBanners.get(position).getCategory_name()));
            }
        });
    }

    private void LoadCategoryBanner() {
        bannerSlider = (BannerSlider) findViewById(R.id.bannerSlider);
        banners = new ArrayList<>();
        modelBooks.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainApiLink.getCategoryCover,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                    String category_id = jsonObjectUser.getString("category_id");
                                    String cover = jsonObjectUser.getString("cover");
                                    String category_name = jsonObjectUser.getString("category_name");

                                    ModelCategoryBanner modelCategoryBanner = new ModelCategoryBanner(id, category_id, cover, category_name);
                                    modelCategoryBanners.add(modelCategoryBanner);

                                    banners.add(new RemoteBanner(cover));
                                }

                                bannerSlider.setBanners(banners);
                                LoadCategory();

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


    private void LoadCategory() {
        modelBooks.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainApiLink.getCategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                                LoadVideo();

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


    private void LoadVideo() {
        modelContentsVideo.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainApiLink.getMainVideo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("API RESPONSE VIDEO", response);

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
                                    modelContentsVideo.add(modelContentVideo);
                                }

                                videoAdapter = new AdapterContentVideo(modelContentsVideo);
                                recyclerViewContentVideo.setAdapter(videoAdapter);


                                LoadAudio();
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


    private void LoadAudio() {
        modelContentsAudio.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainApiLink.getMainAudio,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("API RESPONSE AUDIO", response);

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

                                    ModelContent modelContentAudio = new ModelContent(id, name, banner, location, type, category, date_time);
                                    modelContentsAudio.add(modelContentAudio);
                                }

                                audioAdapter = new AdapterContentVideo(modelContentsAudio);
                                recyclerViewContentAudio.setAdapter(audioAdapter);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                AlertExit();
                break;

            case R.id.btbMenuSearch:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        AlertExit();
    }


    public void AlertExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }
}
