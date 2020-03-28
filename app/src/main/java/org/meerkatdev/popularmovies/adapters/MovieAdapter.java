package org.meerkatdev.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.popularmovies.R;
import org.meerkatdev.popularmovies.data.models.Movie;
import org.meerkatdev.popularmovies.utils.ListItemClickListener;
import org.meerkatdev.popularmovies.utils.NetworkUtils;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    final private ListItemClickListener mOnClickListener;
    public List<Movie> availableMovies;
    private int noMovies;

    public MovieAdapter(int numberOfItems, ListItemClickListener listener) {
        noMovies = numberOfItems;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int gridItemLayout = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(gridItemLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder viewHolder, int position) {
        NetworkUtils.loadMoviePoster(availableMovies.get(position).posterPath).into(viewHolder.movieView);
    }

    @Override
    public int getItemCount() {
        return noMovies;
    }

    public void setMovieData(List<Movie> movies) {
        availableMovies = movies;
        noMovies = movies.size();
        notifyDataSetChanged();
    }

    protected class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView movieView;

        MovieViewHolder(View itemView) {
            super(itemView);
            movieView = itemView.findViewById(R.id.iv_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
