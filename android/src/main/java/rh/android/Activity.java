package rh.android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Activity extends AppCompatActivity {

    /**
     * Shows soft input. NOTE: for this to work, target EditText properties 'focusable' and 'focusableInTouchMode'
     * must be set to true.
     *
     * @param editText
     */
    protected void showKeyboard(EditText editText) {
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Hides soft input. NOTE: for this to work, target EditText properties 'focusable' and 'focusableInTouchMode'
     * must be set to true.
     *
     * @param editText
     */
    protected void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
