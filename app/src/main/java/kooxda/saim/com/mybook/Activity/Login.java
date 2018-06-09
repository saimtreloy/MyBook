package kooxda.saim.com.mybook.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.MainApiLink;
import kooxda.saim.com.mybook.Utility.MySingleton;
import kooxda.saim.com.mybook.Utility.SharedPrefDatabase;

public class Login extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText inputEmail, inputPassword;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeLogin);
        setContentView(R.layout.activity_login);

        init();
    }

    public void init() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                String email = inputEmail.getText().toString().trim();
                String pass = inputPassword.getText().toString().trim();

                RequestForLogin(email, pass);

            }
        });
    }

    private void RequestForLogin(final String email, final String pass ) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainApiLink.login,
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
                                    String user_fullname = jsonObjectUser.getString("user_fullname");
                                    String user_email = jsonObjectUser.getString("user_email");
                                    String user_mobile = jsonObjectUser.getString("user_mobile");
                                    String user_pass = jsonObjectUser.getString("user_pass");
                                    String user_age = jsonObjectUser.getString("user_age");
                                    String user_photo = jsonObjectUser.getString("user_photo");
                                    String user_address = jsonObjectUser.getString("user_address");

                                    new SharedPrefDatabase(getApplicationContext()).StoreID(id);
                                    new SharedPrefDatabase(getApplicationContext()).StoreName(user_fullname);
                                    new SharedPrefDatabase(getApplicationContext()).StoreEmail(user_email);
                                    new SharedPrefDatabase(getApplicationContext()).StoreMobile(user_mobile);
                                    new SharedPrefDatabase(getApplicationContext()).StorePass(user_pass);
                                    new SharedPrefDatabase(getApplicationContext()).StoreAge(user_age);
                                    new SharedPrefDatabase(getApplicationContext()).StorePhoto(user_photo);
                                    new SharedPrefDatabase(getApplicationContext()).StoreAddress(user_address);
                                }

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();

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
                params.put("user_email", email);
                params.put("user_pass", pass);

                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
