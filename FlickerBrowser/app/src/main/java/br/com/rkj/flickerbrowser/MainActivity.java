package br.com.rkj.flickerbrowser;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static  final String LOG_TAG = "MainActivity";
    private List<Photo> nPhotoList = new ArrayList<Photo>();
    private RecyclerView nRecycleView;
    private FlickerRecycleViewAdapter flickerRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        nRecycleView.setLayoutManager(new LinearLayoutManager(this));

        ProcessPhotos processPhotos = new ProcessPhotos("android,lollipop",true);
        processPhotos.execute();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ProcessPhotos extends JsonFlickerExtractData{

        public ProcessPhotos(String searchCriteria, boolean matchAll) {
            super(searchCriteria, matchAll);
        }
        public void execute(){
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends  DownloadJsonData{

            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                flickerRecycleViewAdapter = new FlickerRecycleViewAdapter
                        (MainActivity.this,getMPhotos());
                nRecycleView.setAdapter(flickerRecycleViewAdapter);
            }
        }
    }
}
