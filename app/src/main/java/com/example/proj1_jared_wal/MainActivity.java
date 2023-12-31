package com.example.proj1_jared_wal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;


public class MainActivity extends AppCompatActivity {


    Button btn_start;
    Button btn_reset;
    TextView txt_intruct;
    TextView txt_count_right;
    TextView txt_count_wrong;
    Button btn_red;
    Button btn_blue;
    Button btn_green;
    Button btn_pink;

    int[] color_lst = new int[2];
    Random rand = new Random();
    int game_step = 0; // 0 = setup -> 1 = 1st round -> 2 = 2nd round

    boolean failed_game = false; // keeps track if user lost the game
    boolean game_running = false; // checks if game is live/running
    boolean end_game_screen = false;

    int count_right;
    int count_wrong;

    private final int COLOR_RED = 0;
    private final int COLOR_BLUE = 1;
    private final int COLOR_GREEN = 2;
    private final int COLOR_PINK = 3;

    private final int COUNTER_STARTING_VALUE = 0;
    private final int GAME_STEP_STARTING_VALUE = 0;


    private static final String SAVED_GAME_RUNNING = "saved_game_running";
    private static final String SAVED_FAILED_GAME = "saved_failed_game";
    private static final String SAVED_GAME_STEP = "saved_game_step";
    private static final String SAVED_COLOR_LIST = "saved_color_list";
    private static final String SAVED_COUNT_RIGHT = "saved_count_right";
    private static final String SAVED_COUNT_WRONG = "saved_count_wrong";
    private static final String SAVED_END_GAME_SCREEN = "saved_end_game_screen";




    // save values : count_right, count_wrong, game_running, failed_game, game_step, color_list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("tag_1", "onCreate");

        if (savedInstanceState != null){
            Log.i("tag_1", savedInstanceState.toString());
            Log.d("tag_1", "SOMETHING IN INSTANCE STATE ");
            // TODO implement bundle saving
        }

        txt_intruct = findViewById(R.id.txt_instructions);
        btn_reset = findViewById(R.id.btn_reset);
        btn_start = findViewById(R.id.btn_start);

        btn_red = findViewById(R.id.button_red);
        btn_blue = findViewById(R.id.button_blue);
        btn_green = findViewById(R.id.button_green);
        btn_pink = findViewById(R.id.button_pink);

        txt_count_right = findViewById(R.id.count_right);
        txt_count_wrong = findViewById(R.id.count_wrong);


        btn_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // creates the random colors
                set_random_color_array();

                // sets the first color instruction
                String word = get_color_of_int(color_lst[0]);

                // reset game step
                game_step = GAME_STEP_STARTING_VALUE;

                // set booleans for game
                failed_game = false;
                game_running = true;
                end_game_screen = false;

                //display right/wrong
                count_right = COUNTER_STARTING_VALUE;
                count_wrong = COUNTER_STARTING_VALUE;
                String count_right_string = getString(R.string.number_right) + " " + String.valueOf(count_right);
                String count_wrong_string = getString(R.string.number_wrong) + " " + String.valueOf(count_wrong);

                // make right/wrong visiable and filled with text
                txt_count_right.setVisibility(View.VISIBLE);
                txt_count_wrong.setVisibility(View.VISIBLE);

                txt_count_right.setText(count_right_string);
                txt_count_wrong.setText(count_wrong_string);


                txt_intruct.setText(word);

                // set start btn attributes to greyed out
                btn_start.setBackgroundColor(getColor(R.color.grey));
                btn_start.setTextColor(getColor(R.color.dark_grey));
                btn_start.setEnabled(false);

                // set reset btn attribute to active
                btn_reset.setBackgroundColor(getColor(R.color.purple));
                btn_reset.setTextColor(getColor(R.color.white));
                btn_reset.setEnabled(true);
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // hide right/wrong
                txt_count_right.setVisibility(View.INVISIBLE);
                txt_count_wrong.setVisibility(View.INVISIBLE);

                //set game attributes
                game_running = false;
                end_game_screen = false;

                // say to press start to start game
                txt_intruct.setText(R.string.press_start_to_begin);

                // set start btn attributes to active
                btn_start.setBackgroundColor(getColor(R.color.purple));
                btn_start.setTextColor(getColor(R.color.white));
                btn_start.setEnabled(true);

                // set reset btn attricutes to greyed out
                btn_reset.setBackgroundColor(getColor(R.color.grey));
                btn_reset.setTextColor(getColor(R.color.dark_grey));
                btn_reset.setEnabled(false);

            }
        });

        // Red button event handler
        btn_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(game_running){
                    game_step+=1;
                    check_button_result(COLOR_RED);
                }

            }
        });

        // Blue button event handler
        btn_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game_running){
                    game_step+=1;
                    check_button_result(COLOR_BLUE);
                }
            }
        });

        // Green button event handler
        btn_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game_running){
                    game_step+=1;
                    check_button_result(COLOR_GREEN);
                }
            }
        });

        // Pink button event handler
        btn_pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game_running){
                    game_step+=1;
                    check_button_result(COLOR_PINK);
                }
            }
        });

    } //onCreate()


    private void update_num_wrong_text(){
        String num_wrong_str = "Number Wrong = " + String.valueOf(count_wrong);
        txt_count_wrong.setText(num_wrong_str);
    }

    private void update_num_right_text(){
        String num_right_str = "Number Right = " + String.valueOf(count_right);
        txt_count_right.setText(num_right_str);
    }

    private void handle_endgame(){

        if(failed_game){
            txt_intruct.setText(R.string.pay_attention);
        }else{
            txt_intruct.setText(R.string.perfect_score);
        }

        end_game_screen = true;
        game_running = false;
    }

    private void check_button_result(int color_picked){
        // check what step:

        // 1st round
        if (game_step == 1){
            // compare to first round number
            if (color_picked == color_lst[0]){
                // correct
                count_right++;
            }else{
                // incorrect
                count_wrong++;
                failed_game = true;
            }

            // display second round instruction
            txt_intruct.setText(get_color_of_int(color_lst[1]));

        }
        // 2nd round
        else if (game_step == 2){
            // compare to second round number
            if (color_picked == color_lst[1]){
                // correct
                count_right++;
            }else{
                // incorrect
                count_wrong++;
                failed_game = true;
            }

            // handle end of game
            handle_endgame();
        }

        // update right/wrong texts
        update_num_right_text();
        update_num_wrong_text();

    }


    private String get_color_of_int(int num){
        switch (num){
            case COLOR_RED:
                return getString(R.string.press_red);

            case COLOR_BLUE:
                return getString(R.string.press_blue);

            case COLOR_GREEN:
                return getString(R.string.press_green);

            case COLOR_PINK:
                return getString(R.string.press_pink);
        }
        return null;
    }


    private void set_random_color_array(){
        color_lst[0] = rand.nextInt(4);
        color_lst[1] = rand.nextInt(4);
    }

    protected void onStop() {
        super.onStop();
        Log.i("tag_1", "Stopped");
    }
    protected void onPause() {
        super.onPause();
        Log.i("tag_1", "Paused");
    }
    protected void onRestart() {
        super.onRestart();
        Log.i("tag_1", "Restart");
    }
    protected void onResume() {
        super.onResume();
        Log.i("tag_1", "Resume");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i("tag_1", "Destroy");
    }
    protected void onStart() {
        super.onStart();
        Log.i("tag_1", "Start");
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("tag_1", "SAVED ");
        outState.putBoolean(SAVED_GAME_RUNNING, game_running);
        outState.putBoolean(SAVED_FAILED_GAME, failed_game);
        outState.putInt(SAVED_GAME_STEP, game_step);
        outState.putIntArray(SAVED_COLOR_LIST, color_lst);
        outState.putInt(SAVED_COUNT_RIGHT, count_right);
        outState.putInt(SAVED_COUNT_WRONG, count_wrong);
        outState.putBoolean(SAVED_END_GAME_SCREEN, end_game_screen);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("tag_1", "RESTORED ");
        Log.d("tag_1", "onRestoreInstanceState, i=" + savedInstanceState.getInt(SAVED_GAME_STEP));

        game_running = savedInstanceState.getBoolean(SAVED_GAME_RUNNING);
        failed_game = savedInstanceState.getBoolean(SAVED_FAILED_GAME);
        game_step = savedInstanceState.getInt(SAVED_GAME_STEP);
        color_lst = savedInstanceState.getIntArray(SAVED_COLOR_LIST);
        count_right = savedInstanceState.getInt(SAVED_COUNT_RIGHT);
        count_wrong = savedInstanceState.getInt(SAVED_COUNT_WRONG);
        end_game_screen = savedInstanceState.getBoolean(SAVED_END_GAME_SCREEN);


        if (game_running){
            // game is running

            // get the proper color to display:
            String word = "";
            Log.d("tag_1", Integer.toString(game_step));

            if (game_step == 0) {
                word = get_color_of_int(color_lst[0]);
            }else{
                word = get_color_of_int(color_lst[1]);
            }
            String count_right_string = getString(R.string.number_right) + " " + String.valueOf(count_right);
            String count_wrong_string = getString(R.string.number_wrong) + " " + String.valueOf(count_wrong);

            // make right/wrong visiable and filled with text

            txt_count_right.setVisibility(View.VISIBLE);
            txt_count_wrong.setVisibility(View.VISIBLE);

            txt_count_right.setText(count_right_string);
            txt_count_wrong.setText(count_wrong_string);


            txt_intruct.setText(word);

            // so make start button grey and disabled, and reset enabled and purple

            // set start btn attributes to greyed out
            btn_start.setBackgroundColor(getColor(R.color.grey));
            btn_start.setTextColor(getColor(R.color.dark_grey));
            btn_start.setEnabled(false);

            // set reset btn attribute to active
            btn_reset.setBackgroundColor(getColor(R.color.purple));
            btn_reset.setTextColor(getColor(R.color.white));
            btn_reset.setEnabled(true);

        }else{
            // game is not running, but it is on end_game_screen
            if (end_game_screen){
                Log.d("tag_1", "END GAME ");
                // so make start button grey and disabled, and reset enabled and purple

                // set start btn attributes to greyed out
                btn_start.setBackgroundColor(getColor(R.color.grey));
                btn_start.setTextColor(getColor(R.color.dark_grey));
                btn_start.setEnabled(false);

                // set reset btn attribute to active
                btn_reset.setBackgroundColor(getColor(R.color.purple));
                btn_reset.setTextColor(getColor(R.color.white));
                btn_reset.setEnabled(true);


                // display proper instruction text:
                if (count_right >= 2) {
                    txt_intruct.setText(R.string.perfect_score);
                }else{
                    txt_intruct.setText(R.string.pay_attention);
                }

                // display counts:
                String count_right_string = getString(R.string.number_right) + " " + String.valueOf(count_right);
                String count_wrong_string = getString(R.string.number_wrong) + " " + String.valueOf(count_wrong);


                txt_count_right.setVisibility(View.VISIBLE);
                txt_count_wrong.setVisibility(View.VISIBLE);

                txt_count_right.setText(count_right_string);
                txt_count_wrong.setText(count_wrong_string);

            }
        }


    }

}