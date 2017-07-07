package rh.flashcards.feature.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import rh.android.Activity;
import rh.flashcards.R;
import rh.flashcards.data.CardRepository;
import rh.flashcards.data.database.DatabaseCardRepository;
import rh.flashcards.entity.Card;

public class StudyActivity extends Activity {

    private static final String EXTRA_CARDS = "cards";

    private static final int SCORE_LIMIT = 3;

    private final List<Question> questions = new ArrayList<>();

    @BindView(R.id.text_question) TextView textQuestion;

    @BindView(R.id.text_answer) TextView textAnswer;

    @BindView(R.id.button_show_answer) Button buttonShowAnswer;

    @BindView(R.id.layout_correct_and_wrong_buttons) View layoutCorrectAndWrongButtons;

    @BindView(R.id.edit_answer) EditText editAnswer;

    @BindView(R.id.text_user_answer) TextView textUserAnswer;

    private Question currentQuestion;

    private CardRepository cardRepository;

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

        cardRepository = new DatabaseCardRepository(this);

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

        editAnswer.setVisibility(View.GONE);

        if (currentQuestion.isWrittenAnswer()) {
            String userAnswer = getString(R.string.your_answer) + " " + editAnswer.getText();
            textUserAnswer.setText(userAnswer);
            textUserAnswer.setVisibility(View.VISIBLE);
            evaluateAnswer();
        } else {
            textUserAnswer.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.button_answer_correct)
    public void onAnswerCorrectClicked() {
        Card card = currentQuestion.getCard();
        if (currentQuestion.testingFront && card.getFrontScore() != SCORE_LIMIT) {
            card.setFrontScore(card.getFrontScore() + 1);
            card.setFrontReviewed(LocalDate.now());
        } else if (card.getBackScore() != SCORE_LIMIT) {
            card.setBackScore(card.getBackScore() + 1);
            card.setBackReviewed(LocalDate.now());
        }

        cardRepository.update(card);

        showNextQuestion();
    }

    @OnClick(R.id.button_answer_wrong)
    public void onAnswerWrongClicked() {
        Card card = currentQuestion.getCard();
        if (currentQuestion.testingFront && card.getFrontScore() != 0) {
            card.setFrontScore(card.getFrontScore() - 1);
            card.setFrontReviewed(LocalDate.now());
        } else if (card.getBackScore() != 0) {
            card.setBackScore(card.getBackScore() - 1);
            card.setBackReviewed(LocalDate.now());
        }

        cardRepository.update(card);

        showNextQuestion();
    }

    @OnEditorAction(R.id.edit_answer)
    public boolean onEditAnswerSubmitted(EditText editText, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard(editText);
            onShowAnswerClicked();
            return true;
        }

        return false;
    }

    private void prepareQuestions() {
        if (!getIntent().hasExtra(EXTRA_CARDS)) {
            throw new IllegalStateException("List of Cards must be supplied via Intent");
        }

        // TODO: pick cards by score and last review date

        ArrayList<Card> cards = (ArrayList<Card>) getIntent().getSerializableExtra(EXTRA_CARDS);

        for (Card card : cards) {
            questions.add(Question.forFront(card));
            questions.add(Question.forBack(card));
        }

        Collections.shuffle(questions);
    }

    private void showNextQuestion() {
        if (!questions.isEmpty()) {
            buttonShowAnswer.setVisibility(View.VISIBLE);
            layoutCorrectAndWrongButtons.setVisibility(View.GONE);
            textAnswer.setVisibility(View.INVISIBLE);

            currentQuestion = questions.remove(0);
            editAnswer.setVisibility(currentQuestion.isWrittenAnswer() ? View.VISIBLE : View.INVISIBLE);
            editAnswer.setText("");
            textQuestion.setText(currentQuestion.getQuestion());
            textAnswer.setText(currentQuestion.getAnswer());
            textUserAnswer.setVisibility(View.GONE);

            if (currentQuestion.isWrittenAnswer()) {
                showKeyboard(editAnswer); // TODO: doesn't work for first question after onCreate
            }
        } else {
            textQuestion.setVisibility(View.VISIBLE);
            textQuestion.setText(R.string.study_finished);

            textAnswer.setVisibility(View.GONE);
            textUserAnswer.setVisibility(View.GONE);
            editAnswer.setVisibility(View.GONE);
            buttonShowAnswer.setVisibility(View.GONE);
            layoutCorrectAndWrongButtons.setVisibility(View.GONE);
        }
    }

    private void evaluateAnswer() {
        String answer = editAnswer.getText().toString();
        int answerColor = answer.equals(currentQuestion.getAnswer()) ? R.color.correct_answer : R.color.wrong_answer;
        textUserAnswer.setTextColor(ContextCompat.getColor(this, answerColor));
    }

    private static class Question {
        private Card card;

        private boolean testingFront;

        private Question(Card card) {
            this.card = card;
        }

        public static Question forFront(Card card) {
            Question question = new Question(card);
            question.testingFront = true;

            return question;
        }

        public static Question forBack(Card card) {
            Question question = new Question(card);
            question.testingFront = false;

            return question;
        }

        public String getQuestion() {
            return testingFront ? card.getFront() : card.getBack();
        }

        public String getAnswer() {
            return testingFront ? card.getBack() : card.getFront();
        }

        public boolean isWrittenAnswer() {
            return !testingFront;
        }

        public Card getCard() {
            return card;
        }
    }
}
