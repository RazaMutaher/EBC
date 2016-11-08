package com.English.BolChal.Urdu;

/**
 * Created by Owais Nizami on 3/8/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MoviesAdapter2 extends RecyclerView.Adapter<MoviesAdapter2.MyViewHolder> {

    private List<Movie2> dataList;
    Context context;

     public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listingB , transliteration, urduB;

        public MyViewHolder(View view) {
            super(view);
            listingB = (TextView) view.findViewById(R.id.engText);
            transliteration = (TextView) view.findViewById(R.id.transText);
            urduB = (TextView) view.findViewById(R.id.urduText);
        }
    }

    public MoviesAdapter2(Context context, ArrayList<Movie2> list){
        this.context = context;
        this.dataList = list;
    }


    public MoviesAdapter2(List<Movie2> list) {
        this.dataList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie2 movie = dataList.get(position);
        holder.listingB.setText(movie.getListingB());
        holder.transliteration.setText(movie.getTransliteration());
        holder.urduB.setText(movie.getUrduB());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}