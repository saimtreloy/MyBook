package kooxda.saim.com.mybook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kooxda.saim.com.mybook.Activity.VIdeoPlayer;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;

/**
 * Created by NREL on 6/23/18.
 */

public class AdapterPlayer extends RecyclerView.Adapter<AdapterPlayer.AdapterPlayerHolder> {

    ArrayList<ModelContent> adapterList = new ArrayList<>();
    Context mContext;

    public AdapterPlayer(ArrayList<ModelContent> adapterList) {
        this.adapterList = adapterList;
    }

    public AdapterPlayer(ArrayList<ModelContent> adapterList, Context mContext) {
        this.adapterList = adapterList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_content, parent, false);
        AdapterPlayer.AdapterPlayerHolder adapterPlayerHolder = new AdapterPlayer.AdapterPlayerHolder(view);
        return adapterPlayerHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlayerHolder holder, int position) {
        holder.txtContentName.setText(adapterList.get(position).getName());
        holder.txtContentCategory.setText("Book Name : " + adapterList.get(position).getCategory());
        holder.txtContentType.setText("Content Type : " + adapterList.get(position).getType());
        Picasso.with(holder.imageBanner.getContext()).
                load(adapterList.get(position).getBanner()).
                placeholder(R.drawable.ic_logo).
                error(R.drawable.ic_logo).
                into(holder.imageBanner);
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class AdapterPlayerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView card_view;
        ImageView imageBanner;
        TextView txtContentName, txtContentCategory, txtContentType;

        public AdapterPlayerHolder(View itemView) {
            super(itemView);

            card_view = (CardView) itemView.findViewById(R.id.card_view);
            card_view.setPreventCornerOverlap(false);

            imageBanner = (ImageView) itemView.findViewById(R.id.imageBanner);
            txtContentName = (TextView) itemView.findViewById(R.id.txtContentName);
            txtContentCategory = (TextView) itemView.findViewById(R.id.txtContentCategory);
            txtContentType = (TextView) itemView.findViewById(R.id.txtContentType);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("SAIM PLAYER", adapterList.get(getAdapterPosition()).getId() + "");
            Intent intent = new Intent().setAction("kooxda.saim.com.mybook.PlayContentReceiver");
            intent.putExtra("TYPE", adapterList.get(getAdapterPosition()).getType());
            intent.putExtra("URL", adapterList.get(getAdapterPosition()).getLocation());
            intent.putExtra("TITLE", adapterList.get(getAdapterPosition()).getName());
            intent.putExtra("COVER", adapterList.get(getAdapterPosition()).getBanner());
            v.getContext().sendBroadcast(intent);
        }
    }
}
