package rh.flashcards.feature.decklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rh.android.RecyclerViewAdapter;
import rh.flashcards.R;
import rh.flashcards.data.DeckRepository;
import rh.flashcards.data.database.DatabaseDeckRepository;
import rh.flashcards.entity.Deck;
import rh.flashcards.feature.cardlist.CardListActivity;

public class DeckListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_decks)
    RecyclerView recyclerDecks;

    private DeckRepository deckRepository;
    private ActionMode actionMode;
    private DeckAdapter deckAdapter;
    private Deck selectedDeck;
    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.deck_list_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit_deck:
                    showDeckDialog(selectedDeck);
                    break;
                case R.id.action_delete_deck:
                    removeSelectedDeck();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);
        ButterKnife.bind(this);

        deckRepository = new DatabaseDeckRepository(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        deckAdapter = new DeckAdapter(deckRepository.findAll());

        deckAdapter.setOnClickListener(new RecyclerViewAdapter.OnClickListener<Deck>() {
            @Override
            public void onItemClicked(Deck item) {
                Intent intent = new Intent(DeckListActivity.this, CardListActivity.class);
                startActivity(intent);
            }
        });

        deckAdapter.setOnLongClickListener(new RecyclerViewAdapter.OnLongClickListener<Deck>() {
            @Override
            public void onItemLongClicked(Deck deck) {
                if (actionMode == null) {
                    selectedDeck = deck;
                    startSupportActionMode(actionModeCallback);
                }
            }
        });

        recyclerDecks.setAdapter(deckAdapter);
        recyclerDecks.setLayoutManager(new LinearLayoutManager(this));
        recyclerDecks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deck_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        showDeckDialog(null);
    }

    private void showDeckDialog(@Nullable final Deck deck) {
        DeckDialog dialog = new DeckDialog();

        if (deck != null) {
            dialog.setArguments(DeckDialog.createArguments(deck.getName()));
        }

        dialog.setOnDialogAcceptedListener(new DeckDialog.OnDialogAcceptedListener() {
            @Override
            public void onDialogAccepted(String deckName) {
                if (deck == null) {
                    Deck deck = new Deck(deckName);
                    deckRepository.create(deck);
                    deckAdapter.addItem(deck);
                } else {
                    deck.setName(deckName);
                    deckRepository.update(deck);
                    deckAdapter.notifyItemChanged(deck);
                }
            }
        });

        dialog.show(getSupportFragmentManager(), "DeckDialog");
    }

    private void removeSelectedDeck() {
        deckRepository.delete(selectedDeck);
        deckAdapter.removeItem(selectedDeck);
    }
}
