package rh.flashcards.feature.cardlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rh.flashcards.R;
import rh.flashcards.data.CardRepository;
import rh.flashcards.data.database.DatabaseCardRepository;
import rh.flashcards.entity.Deck;
import rh.flashcards.feature.cardeditor.CardEditorActivity;

public class CardListActivity extends AppCompatActivity {

    private static final int REQUEST_CARD_EDITOR = 1;
    private static final String EXTRA_DECK = "deck";

    @BindView(R.id.recycler_cards)
    RecyclerView recyclerCards;

    private Deck deck;

    private CardRepository cardRepository;

    public static Intent createIntent(Context context, Deck deck) {
        Intent intent = new Intent(context, CardListActivity.class);
        intent.putExtra(EXTRA_DECK, deck);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!getIntent().hasExtra(EXTRA_DECK)) {
            throw new IllegalStateException("CardListActivity must be supplied a Deck via Intent");
        }

        deck = (Deck) getIntent().getSerializableExtra(EXTRA_DECK);

        cardRepository = new DatabaseCardRepository(this);

        recyclerCards.setLayoutManager(new LinearLayoutManager(this));
        recyclerCards.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        loadCards();
    }

    private void loadCards() {
        CardAdapter cardAdapter = new CardAdapter(cardRepository.findForDeck(deck));
        recyclerCards.setAdapter(cardAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CARD_EDITOR && resultCode == CardEditorActivity.RESULT_CARD_SAVED) {
            loadCards();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        Intent intent = CardEditorActivity.createIntent(this, deck);
        startActivityForResult(intent, REQUEST_CARD_EDITOR);
    }
}
