package org.meerkatdev.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.popularmovies.R;
import org.meerkatdev.popularmovies.data.models.MovieRelatedVideo;
import org.meerkatdev.popularmovies.utils.DetailsListItemClickListener;
import org.meerkatdev.popularmovies.utils.NetworkUtils;

public class MovieRelatedVideoAdapter extends RecyclerView.Adapter<MovieRelatedVideoAdapter.MovieRelatedVideoViewHolder> {

    final private DetailsListItemClickListener mOnClickListener;
    public MovieRelatedVideo[] availableElements;
    private int noElements;

    public MovieRelatedVideoAdapter(int numberOfItems, DetailsListItemClickListener listener) {
        noElements = numberOfItems;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MovieRelatedVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int itemLayout = R.layout.movie_related_video_element;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(itemLayout, parent, false);
        return new MovieRelatedVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRelatedVideoViewHolder holder, int position) {
        NetworkUtils.loadYoutubeThumbnail(availableElements[position].key).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return noElements;
    }

    public void setData(MovieRelatedVideo[] elements) {
        availableElements = elements;
        noElements = elements.length;
        notifyDataSetChanged();
    }

    protected class MovieRelatedVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;

        MovieRelatedVideoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_movie_related_video_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRelatedVideoClick(clickedPosition);
        }
    }
}
