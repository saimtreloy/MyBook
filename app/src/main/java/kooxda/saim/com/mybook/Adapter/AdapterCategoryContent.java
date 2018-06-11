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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kooxda.saim.com.mybook.Activity.VIdeoPlayer;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;

/**
 * Created by NREL on 6/11/18.
 */

public class AdapterCategoryContent extends RecyclerView.Adapter<AdapterCategoryContent.AdapterCategoryContentHolder> {

    ArrayList<ModelContent> adapterList = new ArrayList<>();
    Context mContext;

    public AdapterCategoryContent(ArrayList<ModelContent> adapterList) {
        this.adapterList = adapterList;
    }

    public AdapterCategoryContent(ArrayList<ModelContent> adapterList, Context mContext) {
        this.adapterList = adapterList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterCategoryContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_content, parent, false);
        AdapterCategoryContentHolder adapterCategoryContentHolder = new AdapterCategoryContentHolder(view);
        return adapterCategoryContentHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategoryContentHolder holder, int position) {
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
        }

        @Override
        public void onClick(View v) {
            if (adapterList.get(getAdapterPosition()).getType().equals("Video")) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), VIdeoPlayer.class);
                intent.putExtra("URL", adapterList.get(getAdapterPosition()).getLocation());
                v.getContext().startActivity(intent);
            } else if (adapterList.get(getAdapterPosition()).getType().equals("Audio")) {
                Toast.makeText(v.getContext().getApplicationContext(), "Audio Content", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext().getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
