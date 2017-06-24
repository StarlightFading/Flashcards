package rh.flashcards.feature.decklist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import rh.flashcards.R;

public class DeckDialog extends DialogFragment {

    private static final String ARG_DECK_NAME = "deck_name";

    @BindView(R.id.edit_deck_name)
    EditText editDeckName;

    private OnDialogAcceptedListener onDialogAcceptedListener;

    public static Bundle createArguments(String deckName) {
        Bundle args = new Bundle();
        args.putString(ARG_DECK_NAME, deckName);

        return args;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = View.inflate(getContext(), R.layout.dialog_deck, null);
        ButterKnife.bind(this, view);

        builder.setView(view);

        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onDialogAcceptedListener != null) {
                    onDialogAcceptedListener.onDialogAccepted(editDeckName.getText().toString().trim());
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_DECK_NAME)) {
            editDeckName.setText(args.getString(ARG_DECK_NAME));
        }

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        onDeckNameChanged(editDeckName.getText());

        //noinspection ConstantConditions
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @OnTextChanged(R.id.edit_deck_name)
    public void onDeckNameChanged(CharSequence deckName) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Button acceptButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
            acceptButton.setEnabled(!deckName.toString().trim().isEmpty());
        }
    }

    public void setOnDialogAcceptedListener(OnDialogAcceptedListener onDialogAcceptedListener) {
        this.onDialogAcceptedListener = onDialogAcceptedListener;
    }

    public interface OnDialogAcceptedListener {
        void onDialogAccepted(String deckName);
    }
}
