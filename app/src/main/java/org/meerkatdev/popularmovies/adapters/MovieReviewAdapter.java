package org.meerkatdev.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.meerkatdev.popularmovies.R;
import org.meerkatdev.popularmovies.data.models.MovieReview;
import org.meerkatdev.popularmovies.utils.DetailsListItemClickListener;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    final private DetailsListItemClickListener mOnClickListener;
    public MovieReview[] availableElements;
    private int noElements;

    public MovieReviewAdapter(int numberOfItems, DetailsListItemClickListener listener) {
        noElements = numberOfItems;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        int itemLayout = R.layout.movie_review_element;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(itemLayout, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder holder, int position) {
        holder.author.setText(availableElements[position].author);
        holder.content.setText(availableElements[position].content);
       // holder.url.setText(availableElements[position].url);
    }

    @Override
    public int getItemCount() {
        return noElements;
    }

    public void setData(MovieReview[] elements) {
        availableElements = elements;
        noElements = elements.length;
        notifyDataSetChanged();
    }

    protected class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView author;
        final TextView content;

        MovieReviewViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.tv_review_author);
            content = itemView.findViewById(R.id.tv_review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onReviewClick(clickedPosition);
        }
    }
}
