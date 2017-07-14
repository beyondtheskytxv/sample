package com.example.vuxt.timelearner;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vuxt.timelearner.model.Questions;
import com.example.vuxt.timelearner.model.ResponseJSON;
import com.example.vuxt.timelearner.services.RestClient;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    Questions[] currentQuestions;
    int score;
    int questionNo;
    String meridiem = "";
    String[] times;

    LinearLayout lin_layout_start, lin_layout_main, lin_layout_main_clock, lin_layout_result;
    com.rey.material.widget.ProgressView progressView;
    TextView tv_main_score, tv_main_number_question, tv_result_score;
    Button btn_main_answer_1, btn_main_answer_2, btn_main_answer_3, btn_result_play_again;
    com.example.vuxt.timelearner.custom.CustomAnalogClock customAnalogClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        onInitializeControls();
        onBindingEvent();
        onInitializeData();
    }

    void onInitializeControls() {

        lin_layout_start = (LinearLayout) findViewById(R.id.lin_layout_start);
        lin_layout_main = (LinearLayout) findViewById(R.id.lin_layout_main);
        lin_layout_main_clock = (LinearLayout) findViewById(R.id.lin_layout_main_clock);
        lin_layout_result = (LinearLayout) findViewById(R.id.lin_layout_result);

        progressView = (com.rey.material.widget.ProgressView) findViewById(R.id.progressView);

        tv_main_score = (TextView) findViewById(R.id.tv_main_score);
        tv_main_number_question = (TextView) findViewById(R.id.tv_main_number_question);
        tv_result_score = (TextView) findViewById(R.id.tv_result_score);

        btn_main_answer_1 = (Button) findViewById(R.id.btn_main_answer_1);
        btn_main_answer_2 = (Button) findViewById(R.id.btn_main_answer_2);
        btn_main_answer_3 = (Button) findViewById(R.id.btn_main_answer_3);
        btn_result_play_again = (Button) findViewById(R.id.btn_result_play_again);

        customAnalogClock = (com.example.vuxt.timelearner.custom.CustomAnalogClock) findViewById(R.id.customAnalogClock);
    }

    void onInitializeData() {

        currentQuestions = new Questions[10];
        new RequestQuestions().execute();
    }

    void onBindingEvent() {

        btn_main_answer_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Button btn = (Button) v;
                checkAnswer(btn.getText().toString());
            }
        });

        btn_main_answer_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Button btn = (Button) v;
                checkAnswer(btn.getText().toString());
            }
        });

        btn_main_answer_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Button btn = (Button) v;
                checkAnswer(btn.getText().toString());
            }
        });

        btn_result_play_again.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                playAgain();
            }
        });
    }

    void invalidate() {

        tv_main_score.setText(String.valueOf(score));
        tv_main_number_question.setText(String.valueOf(questionNo));

        if (currentQuestions.length == 0)
            return;

        btn_main_answer_1.setText(currentQuestions[questionNo - 1].options[0]);
        btn_main_answer_2.setText(currentQuestions[questionNo - 1].options[1]);
        btn_main_answer_3.setText(currentQuestions[questionNo - 1].options[2]);

        times = (currentQuestions[questionNo - 1].time_to_display.split(":"));

        if (Integer.parseInt(times[0]) >= 12)
            meridiem = "PM";
        else
            meridiem = "AM";


        customAnalogClock.setTime(meridiem, Integer.parseInt(times[0]), Integer.parseInt(times[1]));

    }

    void onStartRound() {

        score = 0;
        questionNo = 1;

        if (currentQuestions != null && currentQuestions.length >= 10)
            invalidate();
    }


    void checkAnswer(String answer) {

        AlertDialog dialog;

        if (answer.equals(currentQuestions[questionNo - 1].time_to_display)) {
            score++;
            dialog = new AlertDialog.Builder(this, R.style.CustomGreenAlertDialogStyle)
                    .setMessage("Correct")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create();
        }
        else
            dialog = new AlertDialog.Builder(this, R.style.CustomRedAlertDialogStyle)
                    .setMessage("Incorrect")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create();

        dialog.show();
        timerDelayRemoveDialog(1000, dialog);

    }

    void timerDelayRemoveDialog(long time, final Dialog d){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
                questionNo++;
                invalidate();
            }
        }, time);


        if (questionNo == 10) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    lin_layout_main.setVisibility(View.GONE);
                    tv_result_score.setText("You got " + String.valueOf(score) + " points");
                    lin_layout_result.setVisibility(View.VISIBLE);
                }
            }, time);

        }
    }

    void playAgain() {

        lin_layout_result.setVisibility(View.GONE);
        lin_layout_main.setVisibility(View.GONE);
        lin_layout_start.setVisibility(View.VISIBLE);
        new RequestQuestions().execute();
    }

    private class RequestQuestions extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {


            RestClient.syncGet("/questions", null, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    Gson gson = new Gson();
                    currentQuestions = gson.fromJson(responseString, ResponseJSON.class).questions;
                }

            });

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            lin_layout_start.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);

            lin_layout_main.setVisibility(View.VISIBLE);

            onStartRound();
        }

        @Override
        protected void onPreExecute() {

            lin_layout_start.setVisibility(View.VISIBLE);
            progressView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

    }
}