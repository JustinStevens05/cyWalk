package com.example.androidexample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * The users view that shows them their profile
 * */
public class Profile extends AppCompatActivity {

    private static String key;
    private static String URL_JSON_OBJECT = null;
    private String username;
    TextView txt_username;
    ShapeableImageView img_profile_avatar;
    private Button btn_logout;
    private Button btn_edit_avatar;
    public String URL_IMAGE = null;
    private String userType;

    // ActivityResultLauncher for opening the gallery
    ActivityResultLauncher<Intent> openGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Get the URI of the selected image
                    Uri selectedImageUri = result.getData().getData();

                    // Convert URI to Bitmap and set it as profile image
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        img_profile_avatar.setImageBitmap(bitmap);  // Set the selected image to img_profile_avatar
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    /**
     * creates the users profile page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        userType = extras.getString("userType");
        //txt_response.setText("Key: " + key);
        URL_JSON_OBJECT = "http://coms-3090-072.class.las.iastate.edu:8080/users/"+key;
        txt_username = findViewById(R.id.profile_txt_username);
        btn_logout = findViewById(R.id.profile_btn_logout);
        btn_edit_avatar = findViewById(R.id.profile_btn_edit_avatar);
        img_profile_avatar = findViewById(R.id.profile_img_avatar);

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        botnav.setSelectedItemId(R.id.nav_profile);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_dashboard) {
                intent = new Intent(Profile.this, Dashboard.class);
                intent.putExtra("key", key);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_goals) {
                intent = new Intent(Profile.this, Goals.class);
                intent.putExtra("key", key);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_social) {
                intent = new Intent(Profile.this, Social.class);
                intent.putExtra("key", key);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_profile) {
                intent = new Intent(Profile.this, Profile.class);
                intent.putExtra("key", key);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                return false;
            }
        });
        makeUsernameReq();

        URL_IMAGE = "http://coms-3090-072.class.las.iastate.edu:8080/users/image/"+username;

        btn_logout.setOnClickListener(new View.OnClickListener() {
            Intent intent = null;
            @Override
            public void onClick(View v) {
                intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
            }
        });

        btn_edit_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ImageUploadActivity
                Intent intent = new Intent(Profile.this, ImageUploadActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("userType", userType);
                startActivity(intent);
                makeImageRequest();
            }
        });


    }

    /**
     * gets the current users username base off of their session key and then sets the required text
     * on the screen to that retrieved username
     */
    private void makeUsernameReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL_JSON_OBJECT, null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            username = response.getString("username");
                            txt_username.setText(username);

                            URL_IMAGE = "http://coms-3090-072.class.las.iastate.edu:8080/users/image/" + username;
                            makeImageRequest();

                            //img_profile_avatar.setBackgroundResource(R.drawable.bronze_border);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    /**
     * Making image request
     * */
    private void makeImageRequest() {

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        img_profile_avatar.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }
}
