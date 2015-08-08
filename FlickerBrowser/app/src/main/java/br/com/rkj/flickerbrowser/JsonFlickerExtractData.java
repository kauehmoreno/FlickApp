package br.com.rkj.flickerbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 28/07/2015.
 */
public class JsonFlickerExtractData  extends ExtractRawData{

    private String LOG_TAG = JsonFlickerExtractData.class.getName();
    private List<Photo> photos;
    private Uri destinationUrl;

    public JsonFlickerExtractData(String searchCriteria, boolean matchAll){

        super(null);

        creatAndUpdateUrl(searchCriteria, matchAll);
        photos = new ArrayList<Photo>();
    }

    public void execute(){
        super.setnRawUrl(destinationUrl.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built URI = " + destinationUrl.toString());
        downloadJsonData.execute(destinationUrl.toString());
    }

    public boolean creatAndUpdateUrl(String searchCriteria, boolean matchAll){
        final String FLICK_API_BASE_URL= "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM="tags";
        final String TAGMODE_PARAM= "tagmode";
        final String FORMAT_PARAM ="format";
        final String NO_JSON_CALLBACK_PARAM="nojsoncallback";

        destinationUrl = Uri.parse(FLICK_API_BASE_URL).buildUpon()
                .appendQueryParameter(TAGS_PARAM,searchCriteria)
                .appendQueryParameter(TAGMODE_PARAM, matchAll ? "All" : "ANY")
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(NO_JSON_CALLBACK_PARAM,"1")
                .build();

        // retornando destination se o mesmo nao for null
        return destinationUrl != null;



    }

    public List<Photo> getMPhotos(){


        return photos;
    }
    public  void processResult(){
        if(getnDownloadStatus() != DownloadStatus.OK){
            Log.e(LOG_TAG,"Erroe downloading raw file");
            return;
        }

        final String FLICK_ITEMS = "items";
        final String FLICK_TITLE = "title";
        final String FLICK_MEDIA = "media";
        final String FLICK_PHOTO_URL = "m";
        final String FLICK_AUTHOR = "author";
        final String FLICK_AUTHOR_ID = "author_id";
        final String FLICK_LINKS = "link";
        final String FLICK_TAGS = "tags";

        try{
            JSONObject jsonData = new JSONObject(getnData());
            JSONArray itemsArray = jsonData.getJSONArray(FLICK_ITEMS);
            // percorrendo pelo json a partir do items que e um array com variosa atributos

            for(int i=0; i<itemsArray.length(); i++){

                JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                /*
                    navegando pelos atributos dentro de item
                 */
                String title = jsonPhoto.getString(FLICK_TITLE);
                String author = jsonPhoto.getString(FLICK_AUTHOR);
                String authorId = jsonPhoto.getString(FLICK_AUTHOR_ID);
                String link = jsonPhoto.getString(FLICK_LINKS);
                String tags = jsonPhoto.getString(FLICK_TAGS);

                JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICK_MEDIA);
                String photoUrl = jsonMedia.getString(FLICK_PHOTO_URL);

                /*
                 criando um objeto com as informacoes pegas via json
                 */
                Photo photoObject = new Photo(title,author,authorId,link
                ,tags,photoUrl);

                this.photos.add(photoObject);
            }

            for(Photo singlePhoto : photos){
                Log.v(LOG_TAG,singlePhoto.toString());
            }

        }catch (JSONException json){
            json.printStackTrace();
            Log.e(LOG_TAG,"Error processing Json data");

        }

    }
    public class DownloadJsonData extends DownloadRawData{

        protected  void onPostExecute(String webData){
            super.onPostExecute(webData);
            processResult();
        }

        /*
         to avoid crash, cuz instead of parm now we have a url, so we now using "par"
         */
        protected  String doInBackground(String... params){
            String[] par = {destinationUrl.toString()};
            return super.doInBackground(par);
        }
    }

}
