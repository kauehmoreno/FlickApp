package br.com.rkj.flickerbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Home on 28/07/2015.
 * classe sera chamada pelo mainActivity
 */
enum DownloadStatus { IDLE,PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

public class ExtractRawData {

    private String LOG_TAG = ExtractRawData.class.getSimpleName();
    private String nRawUrl;
    private String nData;
    private DownloadStatus nDownloadStatus;

    public ExtractRawData(String nRawUrl) {

        this.setnRawUrl(nRawUrl);
        this.nDownloadStatus = DownloadStatus.IDLE;
    }

    public void reset(){
        this.nDownloadStatus = DownloadStatus.IDLE;
        this.nData = null;
        this.setnRawUrl(null);
    }

    public String getnData() {
        return nData;
    }

    public DownloadStatus getnDownloadStatus() {
        return nDownloadStatus;
    }

    public void setnRawUrl(String nRawUrl) {
        this.nRawUrl = nRawUrl;
    }

    public void execute(){
        this.nDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();

        downloadRawData.execute(nRawUrl);
    }



    public class DownloadRawData extends AsyncTask<String, Void,String>{

        protected void onPostExecute(String webData){

            nData = webData;
            Log.v(LOG_TAG,"Data returned was: " +nData);

            if(nData == null){
                if(nRawUrl == null){
                    nDownloadStatus = DownloadStatus.NOT_INITIALISED;
                }else{
                    nDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            }else{
                //sucess
                nDownloadStatus = DownloadStatus.OK;
            }
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if(params == null) {

                return null;
            }
                try{

                    URL url = new URL(params[0]);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    if(inputStream == null) {

                        return null;
                    }
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");
                    }

                    return buffer.toString();

                }catch(IOException e){

                    Log.e(LOG_TAG, "Error",e);
                    return null;
                }finally {
                    if(urlConnection != null) {

                        urlConnection.disconnect();
                    }
                    if(reader != null){
                        try {
                            reader.close();

                        }catch(final IOException e) {
                            Log.e(LOG_TAG,"Error closing stream",e);
                        }

                    }
                }
            }

        }
    }

