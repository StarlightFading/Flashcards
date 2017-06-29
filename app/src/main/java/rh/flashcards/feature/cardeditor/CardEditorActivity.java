package rh.flashcards.feature.cardeditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import rh.flashcards.R;
import rh.flashcards.data.CardRepository;
import rh.flashcards.data.database.DatabaseCardRepository;
import rh.flashcards.entity.Card;
import rh.flashcards.entity.Deck;

public class CardEditorActivity extends AppCompatActivity {

    public static final int RESULT_CARD_SAVED = 1;
    private static final String ARG_DECK = "deck";

    @BindView(R.id.edit_card_front)
    EditText editCardFront;

    @BindView(R.id.edit_card_back)
    EditText editCardBack;

    private Deck deck;

    private CardRepository cardRepository;

    public static Intent createIntent(Context context, Deck deck) {
        Intent intent = new Intent(context, CardEditorActivity.class);
        intent.putExtra(ARG_DECK, deck);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_editor);
        ButterKnife.bind(this);

        cardRepository = new DatabaseCardRepository(this);

        Intent intent = getIntent();
        if (!intent.hasExtra(ARG_DECK)) {
            throw new IllegalStateException("CardEditorActivity must be supplied Deck via Intent");
        }

        deck = (Deck) intent.getSerializableExtra(ARG_DECK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            Card card = new Card();
            card.setFront(editCardFront.getText().toString());
            card.setBack(editCardBack.getText().toString());

            cardRepository.create(card, deck);

            setResult(RESULT_CARD_SAVED);
            finish();

            return true;
        }

        return false;
    }
}
