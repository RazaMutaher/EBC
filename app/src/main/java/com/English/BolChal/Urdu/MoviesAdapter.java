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


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    Context context;

    private List<Movie> dataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listing, urdu;

        public MyViewHolder(View view) {
            super(view);
            listing = (TextView) view.findViewById(R.id.title);
            urdu = (TextView) view.findViewById(R.id.genre);
        }
    }

    public MoviesAdapter(Context context, ArrayList<Movie> list){
        this.context = context;
        this.dataList = list;
    }

    public MoviesAdapter(List<Movie> moviesList) {
        this.dataList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = dataList.get(position);
        holder.listing.setText(movie.getListing());
        holder.urdu.setText(movie.getUrdu());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}