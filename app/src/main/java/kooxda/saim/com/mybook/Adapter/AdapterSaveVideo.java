package kooxda.saim.com.mybook.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.DBHelper;

/**
 * Created by NREL on 6/27/18.
 */

public class AdapterSaveVideo extends RecyclerView.Adapter<AdapterSaveVideo.AdapterCategoryContentHolder>{

    ArrayList<ModelContent> adapterList = new ArrayList<>();
    Context mContext;

    public AdapterSaveVideo(ArrayList<ModelContent> adapterList) {
        this.adapterList = adapterList;
    }

    public AdapterSaveVideo(ArrayList<ModelContent> adapterList, Context mContext) {
        this.adapterList = adapterList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterSaveVideo.AdapterCategoryContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_content, parent, false);
        AdapterSaveVideo.AdapterCategoryContentHolder adapterCategoryContentHolder = new AdapterSaveVideo.AdapterCategoryContentHolder(view);
        return adapterCategoryContentHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSaveVideo.AdapterCategoryContentHolder holder, int position) {
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

    public class AdapterCategoryContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView card_view;
        ImageView imageBanner;
        TextView txtContentName, txtContentCategory, txtContentType;

        public AdapterCategoryContentHolder(View itemView) {
            super(itemView);

            card_view = (CardView) itemView.findViewById(R.id.card_view);
            card_view.setPreventCornerOverlap(false);

            imageBanner = (ImageView) itemView.findViewById(R.id.imageBanner);
            txtContentName = (TextView) itemView.findViewById(R.id.txtContentName);
            txtContentCategory = (TextView) itemView.findViewById(R.id.txtContentCategory);
            txtContentType = (TextView) itemView.findViewById(R.id.txtContentType);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDelete(view, getAdapterPosition());
                    return false;
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (adapterList.get(getAdapterPosition()).getType().equals("Video")) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), VIdeoPlayer.class);
                intent.putExtra("TYPE", adapterList.get(getAdapterPosition()).getType());
                intent.putExtra("URL", adapterList.get(getAdapterPosition()).getLocation());
                intent.putExtra("TITLE", adapterList.get(getAdapterPosition()).getName());
                intent.putExtra("POSITION", getAdapterPosition());

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
                intent.putExtra("POSITION", getAdapterPosition());

                Gson gson = new Gson();
                String jsonAdapterList = gson.toJson(adapterList);

                intent.putExtra("LIST", jsonAdapterList);

                v.getContext().startActivity(intent);
            } else {
                Toast.makeText(v.getContext().getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void AlertDelete(final View v, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBHelper dbHelper = new DBHelper(v.getContext());
                        dbHelper.deleteContact(Integer.parseInt(adapterList.get(pos).getId()));
                        adapterList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
