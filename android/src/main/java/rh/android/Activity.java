package rh.android;

import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class Activity extends AppCompatActivity {

    protected void showKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
