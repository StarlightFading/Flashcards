package rh.flashcards.feature.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rh.android.Activity;
import rh.flashcards.R;
import rh.flashcards.entity.Card;

public class StudyActivity extends Activity {

    private static final String EXTRA_CARDS = "cards";
    private final List<Question> questions = new ArrayList<>();
    @BindView(R.id.text_question)
    TextView textQuestion;
    @BindView(R.id.text_answer)
    TextView textAnswer;
    @BindView(R.id.button_show_answer)
    Button buttonShowAnswer;
    @BindView(R.id.layout_correct_and_wrong_buttons)
    View layoutCorrectAndWrongButtons;
    @BindView(R.id.edit_answer)
    EditText editAnswer;

    public static Intent createIntent(Context context, ArrayList<Card> cards) {
        Intent intent = new Intent(context, StudyActivity.class);
        intent.putExtra(EXTRA_CARDS, cards);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        ButterKnife.bind(this);

        prepareQuestions();
        showNextQuestion();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return false;
        }

        return true;
    }

    @OnClick(R.id.button_show_answer)
    public void onShowAnswerClicked() {
        buttonShowAnswer.setVisibility(View.GONE);
        layoutCorrectAndWrongButtons.setVisibility(View.VISIBLE);
        textAnswer.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_answer_correct)
    public void onAnswerCorrectClicked() {
        showNextQuestion();
    }

    @OnClick(R.id.button_answer_wrong)
    public void onAnswerWrongClicked() {
        showNextQuestion();
    }

    private void prepareQuestions() {
        if (!getIntent().hasExtra(EXTRA_CARDS)) {
            throw new IllegalStateException("List of Cards must be supplied via Intent");
        }

        ArrayList<Card> cards = (ArrayList<Card>) getIntent().getSerializableExtra(EXTRA_CARDS);

        for (Card card : cards) {
            questions.add(new Question(card.getFront(), card.getBack(), false));
            questions.add(new Question(card.getBack(), card.getFront(), true));
        }

        Collections.shuffle(questions);
    }

    private void showNextQuestion() {
        if (!questions.isEmpty()) {
            buttonShowAnswer.setVisibility(View.VISIBLE);
            layoutCorrectAndWrongButtons.setVisibility(View.GONE);
            textAnswer.setVisibility(View.INVISIBLE);

            Question question = questions.remove(0);
            editAnswer.setVisibility(question.isWrittenAnswer() ? View.VISIBLE : View.INVISIBLE);
            editAnswer.setText("");
            textQuestion.setText(question.getQuestion());
            textAnswer.setText(question.getAnswer());

            if (question.isWrittenAnswer()) {
                editAnswer.requestFocus();
                showKeyboard(); // TODO: this doesn't work on repeated call
            }
        } else {
            buttonShowAnswer.setVisibility(View.GONE);
            layoutCorrectAndWrongButtons.setVisibility(View.GONE);
            textAnswer.setVisibility(View.GONE);
            editAnswer.setVisibility(View.GONE);

            textQuestion.setText(R.string.study_finished);
        }
    }

    private static class Question {
        private String question;

        private String answer;

        private boolean writtenAnswer;

        public Question(String question, String answer, boolean writtenAnswer) {
            this.question = question;
            this.answer = answer;
            this.writtenAnswer = writtenAnswer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        public boolean isWrittenAnswer() {
            return writtenAnswer;
        }
    }
}
