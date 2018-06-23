package kooxda.saim.com.mybook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kooxda.saim.com.mybook.Activity.VIdeoPlayer;
import kooxda.saim.com.mybook.Model.ModelBook;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;

/**
 * Created by NREL on 6/11/18.
 */

public class AdapterContentVideo extends RecyclerView.Adapter<AdapterContentVideo.AdapterContentVideoHolder>{

    ArrayList<ModelContent> adapterList = new ArrayList<>();
    Context mContext;

    public AdapterContentVideo(ArrayList<ModelContent> adapterList) {
        this.adapterList = adapterList;
    }

    public AdapterContentVideo(ArrayList<ModelContent> adapterList, Context mContext) {
        this.adapterList = adapterList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterContentVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_book, parent, false);
        AdapterContentVideoHolder adapterContentVideoHolder = new AdapterContentVideoHolder(view);
        return adapterContentVideoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContentVideoHolder holder, int position) {
        holder.txtBookName.setText(adapterList.get(position).getName());
        Picasso.with(holder.imageCover.getContext()).
                load(adapterList.get(position).getBanner()).
                placeholder(R.drawable.ic_logo).
                error(R.drawable.ic_logo).
                into(holder.imageCover);
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class AdapterContentVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView card_view;
        ImageView imageCover;
        TextView txtBookName;

        public AdapterContentVideoHolder(View itemView) {
            super(itemView);

            card_view = (CardView) itemView.findViewById(R.id.card_view);
            card_view.setPreventCornerOverlap(false);

            imageCover = (ImageView) itemView.findViewById(R.id.imageCover);
            txtBookName = (TextView) itemView.findViewById(R.id.txtBookName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (adapterList.get(getAdapterPosition()).getType().equals("Video")) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), VIdeoPlayer.class);
                intent.putExtra("TYPE", adapterList.get(getAdapterPosition()).getType());
                intent.putExtra("URL", adapterList.get(getAdapterPosition()).getLocation());
                intent.putExtra("TITLE", adapterList.get(getAdapterPosition()).getName());

                Gson gson = new Gson();
                String jsonAdapterList = gson.toJson(adapterList);

                intent.putExtra("LIST", jsonAdapterList);

                v.getContext().startActivity(intent);
            } else if (adapterList.get(getAdapterPosition()).getType().equals("Audio")) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), VIdeoPlayer.class);
                intent.putExtra("TYPE", adapterList.get(getAdapterPosition()).getType());
                intent.putExtra("URL", adapterList.get(getAdapterPosition()).getLocation());
                intent.putExtra("TITLE", adapterList.get(getAdapterPosition()).getName());
                intent.putExtra("COVER", adapterList.get(getAdapterPosition()).getBanner());

                Gson gson = new Gson();
                String jsonAdapterList = gson.toJson(adapterList);

                intent.putExtra("LIST", jsonAdapterList);

                v.getContext().startActivity(intent);
            } else {
                Toast.makeText(v.getContext().getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
