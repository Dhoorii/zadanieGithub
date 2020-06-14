package com.ryer.zadaniegithub;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryer.zadaniegithub.api.Repo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;


    private List<Repo> data;
    private LayoutInflater inflater;
    private int selectedPos = RecyclerView.NO_POSITION;

    public RecyclerViewAdapter(Context context, List<Repo> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.itemView.setSelected(selectedPos == position);
        holder.bind(data.get(position));

        Repo repo = data.get(position);
        holder.textViewAuthor.setText(repo.getCommit().getAuthor().getName());
        holder.textViewMessage.setText(repo.getCommit().getMessage());
        holder.textViewSHA.setText(repo.getSha());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        final Repo item = data.get(position);
        return item.isActive() ? TYPE_ACTIVE : TYPE_INACTIVE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewAuthor,textViewMessage,textViewSHA;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewSHA = itemView.findViewById(R.id.textViewSHA);
        }

        void bind(final Repo reposi)
        {
            textViewSHA.setText(reposi.getSha());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reposi.setActive(!reposi.isActive());
                    if (reposi.isActive()) {
                        textViewAuthor.setTextColor(Color.RED);
                        textViewMessage.setTextColor(Color.RED);
                        textViewSHA.setTextColor(Color.RED);
                    }
                    else {
                        textViewAuthor.setTextColor(Color.BLACK);
                        textViewMessage.setTextColor(Color.BLACK);
                        textViewSHA.setTextColor(Color.BLACK);
                    }
                    System.out.println("dziala cos" + reposi.getSha());
                }
            });
        }
    }
    public List<Repo> getAll(int id) {
        return data;
    }
    public List<Repo> getSelected() {
        ArrayList<Repo> selected = new ArrayList<Repo>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isActive()) {
                selected.add(data.get(i));
            }
        }
        return selected;
    }
}
