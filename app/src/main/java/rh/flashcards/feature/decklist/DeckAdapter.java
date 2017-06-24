package rh.flashcards.feature.decklist;

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
import rh.flashcards.entity.Deck;

class DeckAdapter extends RecyclerViewAdapter<Deck, DeckAdapter.ViewHolder> {

    public DeckAdapter(List<Deck> items) {
        super(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deck, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Deck deck = getItem(position);
        holder.textDeckName.setText(deck.getName());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_deck_name)
        TextView textDeckName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
