package rh.flashcards.feature.cardlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rh.android.RecyclerViewAdapter;
import rh.flashcards.R;
import rh.flashcards.data.CardRepository;
import rh.flashcards.data.database.DatabaseCardRepository;
import rh.flashcards.entity.Card;
import rh.flashcards.entity.Deck;
import rh.flashcards.feature.cardeditor.CardEditorActivity;
import rh.flashcards.feature.study.StudyActivity;

public class CardListActivity extends AppCompatActivity {

    private static final int REQUEST_CARD_EDITOR = 1;
    private static final String EXTRA_DECK = "deck";

    @BindView(R.id.recycler_cards)
    RecyclerView recyclerCards;

    private Deck deck;
    private List<Card> cards;
    private CardRepository cardRepository;
    private ActionMode actionMode;
    private Card selectedCard;
    private CardAdapter cardAdapter;
    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.card_list_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit_card:
                    editSelectedCard();
                    break;
                case R.id.action_delete_card:
                    deleteSelectedCard();
                    break;
                default:
                    return false;
            }

            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CARD_EDITOR && resultCode == CardEditorActivity.RESULT_CARD_SAVED) {
            loadCards();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_studying:
                startStudying();
                break;
            default:
                return false;
        }

        return true;
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        Intent intent = CardEditorActivity.createIntent(this, deck);
        startActivityForResult(intent, REQUEST_CARD_EDITOR);
    }

    private void loadCards() {
        cards = cardRepository.findForDeck(deck);
        cardAdapter = new CardAdapter(cards);

        // TODO: should not redo this every time cards are loaded
        cardAdapter.setOnLongClickListener(new RecyclerViewAdapter.OnLongClickListener<Card>() {
            @Override
            public void onItemLongClicked(Card card) {
                if (actionMode == null) {
                    selectedCard = card;
                    startSupportActionMode(actionModeCallback);
                }
            }
        });

        recyclerCards.setAdapter(cardAdapter);
    }

    private void editSelectedCard() {
        Intent intent = CardEditorActivity.createIntent(this, deck, selectedCard);
        startActivityForResult(intent, REQUEST_CARD_EDITOR);
    }

    private void deleteSelectedCard() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.message_delete_card_confirmation)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cardAdapter.removeItem(selectedCard);
                        cardRepository.delete(selectedCard);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void startStudying() {
        Intent intent = StudyActivity.createIntent(this, new ArrayList<>(cards));
        startActivity(intent);

        // TODO: reload card list when returning from study activity
    }
}
