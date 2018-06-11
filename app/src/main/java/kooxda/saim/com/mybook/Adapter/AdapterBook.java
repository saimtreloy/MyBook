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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kooxda.saim.com.mybook.Activity.CategoryContent;
import kooxda.saim.com.mybook.Model.ModelBook;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.SharedPrefDatabase;

/**
 * Created by NREL on 6/10/18.
 */

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.AdapterBookHolder>{


    ArrayList<ModelBook> adapterList = new ArrayList<>();
    Context mContext;

    public AdapterBook(ArrayList<ModelBook> adapterList) {
        this.adapterList = adapterList;
    }

    public AdapterBook(ArrayList<ModelBook> adapterList, Context mContext) {
        this.adapterList = adapterList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_book, parent, false);
        AdapterBookHolder adapterBookHolder = new AdapterBookHolder(view);
        return adapterBookHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBookHolder holder, int position) {
        holder.txtBookName.setText(adapterList.get(position).getCategory_name());
        Picasso.with(holder.imageCover.getContext()).
                load(adapterList.get(position).getCover()).
                placeholder(R.drawable.ic_logo).
                error(R.drawable.ic_logo).
                into(holder.imageCover);
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }


    public class AdapterBookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView card_view;
        ImageView imageCover;
        TextView txtBookName;

        public AdapterBookHolder(View itemView) {
            super(itemView);

            card_view = (CardView) itemView.findViewById(R.id.card_view);
            card_view.setPreventCornerOverlap(false);

            imageCover = (ImageView) itemView.findViewById(R.id.imageCover);
            txtBookName = (TextView) itemView.findViewById(R.id.txtBookName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext().getApplicationContext(), CategoryContent.class);
            intent.putExtra("CATEGORY_NAME", adapterList.get(getAdapterPosition()).getCategory_name());
            v.getContext().startActivity(intent);
        }
    }

}
