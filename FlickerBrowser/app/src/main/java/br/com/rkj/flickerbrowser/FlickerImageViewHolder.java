package br.com.rkj.flickerbrowser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * Created by Home on 08/08/2015.
 */
public class FlickerImageViewHolder  extends RecyclerView.ViewHolder{
    protected ImageView thumbail;
    protected TextView title;

    public FlickerImageViewHolder(View view) {
        super(view);
        this.thumbail = (ImageView) view.findViewById(R.id.thumbail);
        this.title = (TextView) view.findViewById(R.id.title);
    }
}
