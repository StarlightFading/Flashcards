package rh.flashcards.feature.cardeditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
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

    private static final String EXTRA_DECK = "deck";
    private static final String EXTRA_CARD = "card";

    @BindView(R.id.edit_card_front)
    EditText editCardFront;

    @BindView(R.id.edit_card_back)
    EditText editCardBack;

    private Deck deck;

    private Card card;

    private CardRepository cardRepository;

    public static Intent createIntent(Context context, Deck deck) {
        Intent intent = new Intent(context, CardEditorActivity.class);
        intent.putExtra(EXTRA_DECK, deck);

        return intent;
    }

    public static Intent createIntent(Context context, Deck deck, Card card) {
        Intent intent = createIntent(context, deck);
        intent.putExtra(EXTRA_CARD, card);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_editor);
        ButterKnife.bind(this);

        cardRepository = new DatabaseCardRepository(this);

        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_DECK)) {
            throw new IllegalStateException("CardEditorActivity must be supplied Deck via Intent");
        }

        if (intent.hasExtra(EXTRA_CARD)) {
            card = (Card) intent.getSerializableExtra(EXTRA_CARD);

            editCardFront.setText(card.getFront());
            editCardBack.setText(card.getBack());
        } else {
            card = new Card();
        }

        deck = (Deck) intent.getSerializableExtra(EXTRA_DECK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveCardAndFinish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        editCardFront.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void saveCardAndFinish() {
        if (validateInput()) {
            card.setFront(editCardFront.getText().toString());
            card.setBack(editCardBack.getText().toString());

            if (card.getId() == null) {
                cardRepository.create(card, deck);
            } else {
                cardRepository.update(card);
            }

            setResult(RESULT_CARD_SAVED);
            finish();
        }
    }

    private boolean validateInput() {
        boolean valid = true;

        if (editCardFront.getText().toString().trim().isEmpty()) {
            editCardFront.setError(getString(R.string.error_input_empty));
            valid = false;
        }

        if (editCardBack.getText().toString().trim().isEmpty()) {
            editCardBack.setError(getString(R.string.error_input_empty));
            valid = false;
        }

        return valid;
    }
}
