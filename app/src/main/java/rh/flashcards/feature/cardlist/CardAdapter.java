package rh.flashcards.feature.cardlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rh.android.RecyclerViewAdapter;
import rh.flashcards.R;
import rh.flashcards.entity.Card;

class CardAdapter extends RecyclerViewAdapter<Card, CardAdapter.ViewHolder> {

    public CardAdapter(List<Card> items) {
        super(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Card card = getItem(position);
        holder.textCardFront.setText(card.getFront());
        holder.textCardBack.setText(card.getBack());

        String cardScore = card.getFrontScore() + " / " + card.getBackScore();
        holder.textCardScore.setText(cardScore);

        String reviewedDates = card.getFrontReviewed() != null ? card.getFrontReviewed().plusDays(card.getFrontScore()).toString() : "today";
        reviewedDates += " / ";
        reviewedDates += card.getBackReviewed() != null ? card.getBackReviewed().plusDays(card.getBackScore()).toString() : "today";

        holder.textCardReviewedDates.setText(reviewedDates);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_card_front)
        TextView textCardFront;

        @BindView(R.id.text_card_back)
        TextView textCardBack;

        @BindView(R.id.text_card_score)
        TextView textCardScore;

        @BindView(R.id.text_card_reviewed_dates)
        TextView textCardReviewedDates;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
