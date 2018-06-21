package kooxda.saim.com.mybook.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.SharedPrefDatabase;

public class Profile extends AppCompatActivity {

    CircleImageView profile_image;
    EditText inputFullName, inputEmail, inputMobile, inputAge, inputAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    private void init() {
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        inputFullName = (EditText) findViewById(R.id.inputFullName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMobile = (EditText) findViewById(R.id.inputMobile);
        inputAge = (EditText) findViewById(R.id.inputAge);
        inputAddress = (EditText) findViewById(R.id.inputAddress);

        inputFullName.setText(new SharedPrefDatabase(getApplicationContext()).RetriveName());
        inputEmail.setText(new SharedPrefDatabase(getApplicationContext()).RetriveEmail());
        inputMobile.setText(new SharedPrefDatabase(getApplicationContext()).RetriveMobile());
        inputAge.setText(new SharedPrefDatabase(getApplicationContext()).RetriveAge());
        inputAddress.setText(new SharedPrefDatabase(getApplicationContext()).RetriveAddress());

        Picasso.with(getApplicationContext()).
                load(new SharedPrefDatabase(getApplicationContext()).RetrivePhoto()).
                placeholder(R.drawable.ic_logo).
                error(R.drawable.ic_logo).
                into(profile_image);
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
