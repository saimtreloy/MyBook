package kooxda.saim.com.mybook.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import kooxda.saim.com.mybook.Adapter.AdapterCategoryContent;
import kooxda.saim.com.mybook.Adapter.AdapterSaveVideo;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.DBHelper;

public class SaveContent extends AppCompatActivity {

    ProgressDialog progressDialog;
    DBHelper dbHelper = new DBHelper(this);

    ArrayList<ModelContent> modelContents = new ArrayList<>();
    RecyclerView recyclerViewSaveContent;
    RecyclerView.LayoutManager layoutManagerCategoryContent;
    RecyclerView.Adapter categoryContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_content);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle("Save Content");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerViewSaveContent = (RecyclerView) findViewById(R.id.recyclerViewSaveContent);
        recyclerViewSaveContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSaveContent.setHasFixedSize(true);

        modelContents = dbHelper.getAllContents();
        categoryContentAdapter = new AdapterSaveVideo(modelContents);
        recyclerViewSaveContent.setAdapter(categoryContentAdapter);
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
