package rh.flashcards.feature.cardlist;

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
import rh.flashcards.data.mock.MockCardRepository;
import rh.flashcards.feature.cardeditor.CardEditorActivity;

public class CardListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_cards)
    RecyclerView recyclerCards;

    private CardRepository cardRepository = new MockCardRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardAdapter cardAdapter = new CardAdapter(cardRepository.findForDeck(null));

        recyclerCards.setAdapter(cardAdapter);
        recyclerCards.setLayoutManager(new LinearLayoutManager(this));
        recyclerCards.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        Intent intent = new Intent(this, CardEditorActivity.class);
        startActivity(intent);
    }
}
