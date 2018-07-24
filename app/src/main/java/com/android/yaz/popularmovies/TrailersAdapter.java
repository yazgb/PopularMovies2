package com.android.yaz.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private String[] mTrailersData;
    private final TrailersOnClickListener mDetailsClickListener;

    public interface TrailersOnClickListener{
        void trailerOnClick(String trailerKey);
    }

    public TrailersAdapter(TrailersOnClickListener onClickListener) {
        mDetailsClickListener = onClickListener;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.trailer_tv) TextView mTrailerTextView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            String key = mTrailersData[itemPosition];
            mDetailsClickListener.trailerOnClick(key);
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.trailers_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder trailerViewHolder, int position) {
        trailerViewHolder.mTrailerTextView.setText("Trailer " + (position+1));
    }

    @Override
    public int getItemCount() {
        if(mTrailersData == null) {
            return 0;
        } else {
            return mTrailersData.length;
        }
    }

    public void setTrailersData(String[] trailersData) {
        mTrailersData = trailersData;
        notifyDataSetChanged();
    }
}
