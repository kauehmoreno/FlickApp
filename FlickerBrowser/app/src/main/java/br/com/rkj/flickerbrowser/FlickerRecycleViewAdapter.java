package br.com.rkj.flickerbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Home on 08/08/2015.
 */
public class FlickerRecycleViewAdapter  extends RecyclerView.Adapter<FlickerImageViewHolder>{
    private List<Photo> nPhotoList;
    private Context nContext;

    /*
        Creating constructor
     */
    public FlickerRecycleViewAdapter(Context context, List<Photo> photoList){
        nContext = context;
        this.nPhotoList = photoList;
    }

    /*
        putting the data into the placeholder
     */
    @Override
    public FlickerImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browser,
                null);
        FlickerImageViewHolder flickerImageViewHolder = new FlickerImageViewHolder(view);

        return flickerImageViewHolder;
    }

    /*
        anytime there is a object on screen it needs to be update,only will
        download images on screen and not all of them. It will make less memory demage
     */
    @Override
    public void onBindViewHolder(FlickerImageViewHolder holder, int position) {
        Photo photoItem = nPhotoList.get(position);
        Picasso.with(nContext).load(photoItem.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbail);

        holder.title.setText(photoItem.getTitle());

    }

    /*
        if nPhotoList is null it will returns 0, otherwise it will returns the size of it.
     */
    @Override
    public int getItemCount() {

        return (null != nPhotoList ? nPhotoList.size() : 0);
    }
}
