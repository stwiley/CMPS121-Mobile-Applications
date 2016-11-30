package com.example.stw.tictactoe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //Various declarations

    //Whose turn it is
    boolean sswitch =false;
    //Counter for counting turns
    int counter;
    //An array containing all of the winning combinations
    int[][] winArr={{1,2,3},{1,4,7},{1,5,9},{4,5,6},{7,8,9},{3,5,7},{2,5,8},{3,6,9}};
    //An array to hold O's
    int[] oArray= new int[5];
    //An array to hold X's
    int[] xArray= new int[5];
    //A counter to count X's
    int xCounter;
    //A counter to count O's
    int oCounter;
    //Set a boolean to have a winner
    boolean winner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //This is for clearing the board and resetting
    public void clearBoard(View v) {
        //reset the contents of the board
        setContentView(R.layout.activity_main);
        //Reset our counters and boolean identifier
        counter=0;
        xCounter=0;
        oCounter=0;
        winner =false;
        //Fill the arrays up with nothing
        Arrays.fill(oArray, 0);
        Arrays.fill(xArray, 0);
        //Reset the X's and O's and set everything back to clickable
        ImageButton b = (ImageButton) findViewById(R.id.imageButton00);
        b.setImageResource(0);
        b.setClickable(true);
        ImageButton c = (ImageButton) findViewById(R.id.imageButton01);
        c.setImageResource(0);
        c.setClickable(true);
        ImageButton d = (ImageButton) findViewById(R.id.imageButton02);
        d.setImageResource(0);
        d.setClickable(true);
        ImageButton e = (ImageButton) findViewById(R.id.imageButton03);
        e.setImageResource(0);
        e.setClickable(true);
        ImageButton f = (ImageButton) findViewById(R.id.imageButton04);
        f.setImageResource(0);
        f.setClickable(true);
        ImageButton g = (ImageButton) findViewById(R.id.imageButton05);
        g.setImageResource(0);
        g.setClickable(true);
        ImageButton h = (ImageButton) findViewById(R.id.imageButton06);
        h.setImageResource(0);
        h.setClickable(true);
        ImageButton j = (ImageButton) findViewById(R.id.imageButton07);
        j.setImageResource(0);
        j.setClickable(true);
        ImageButton k = (ImageButton) findViewById(R.id.imageButton08);
        k.setImageResource(0);
        k.setClickable(true);
        //Set the turn back to circles
        sswitch=false;
    }


    //Process for whenever a player clicks a button
    public void clickBoard(View v) {
        //Set all of our buttons to the proper orders
        ImageButton vv = (ImageButton) v;
        ImageButton b = (ImageButton) findViewById(R.id.imageButton00);
        ImageButton c = (ImageButton) findViewById(R.id.imageButton01);
        ImageButton d = (ImageButton) findViewById(R.id.imageButton02);
        ImageButton e = (ImageButton) findViewById(R.id.imageButton03);
        ImageButton f = (ImageButton) findViewById(R.id.imageButton04);
        ImageButton g = (ImageButton) findViewById(R.id.imageButton05);
        ImageButton h = (ImageButton) findViewById(R.id.imageButton06);
        ImageButton j = (ImageButton) findViewById(R.id.imageButton07);
        ImageButton k = (ImageButton) findViewById(R.id.imageButton08);
        //Set up the text view
        TextView tWin = (TextView)findViewById(R.id.wonView);
        //A turn for the X's
        if (sswitch) {
            vv.setImageResource(R.drawable.cross);
            //Put the tag of the button clicked into our xArray
            xArray[xCounter]= Integer.parseInt((String)vv.getTag());
            //Set the switch so that circles go next
            sswitch = false;
            //This is actually how android chooses to display turns for some reason
            tWin.setText("O's turn!");
            //Make the symbol un-clickable
            vv.setClickable(false);
            //Up our counter
            counter++;
            //Up our xCounter
            xCounter++;
            //If our xCounter has reached 3 ticks, meaning the X player has made three moves
            if(xCounter >=3){
                //For all the elements in our Winner array
                for(int i=0; i<8; i++){
                    //If the values in our xArray match the values in our winner array
                    if(Arrays.toString(xArray).contains(String.valueOf(winArr[i][0]))&&Arrays.toString(xArray).contains(String.valueOf(winArr[i][1]))&&Arrays.toString(xArray).contains(String.valueOf(winArr[i][2]))){
                        //Set the winner's text
                        tWin.setText("X Wins!");
                        //Declare a winner
                        winner=true;
                        //Determine which blocks to highlight by which portion of our 2-D winning array matches the 3 in a row
                        if(i==0){
                            b.setBackgroundColor(Color.BLUE);
                            c.setBackgroundColor(Color.BLUE);
                            d.setBackgroundColor(Color.BLUE);
                        } else if(i==1){
                            b.setBackgroundColor(Color.BLUE);
                            e.setBackgroundColor(Color.BLUE);
                            h.setBackgroundColor(Color.BLUE);
                        }else if(i==2){
                            b.setBackgroundColor(Color.BLUE);
                            f.setBackgroundColor(Color.BLUE);
                            k.setBackgroundColor(Color.BLUE);
                        }else if(i==3){
                            e.setBackgroundColor(Color.BLUE);
                            f.setBackgroundColor(Color.BLUE);
                            g.setBackgroundColor(Color.BLUE);
                        }else if(i==4){
                            h.setBackgroundColor(Color.BLUE);
                            j.setBackgroundColor(Color.BLUE);
                            k.setBackgroundColor(Color.BLUE);
                        }else if(i==5){
                            d.setBackgroundColor(Color.BLUE);
                            f.setBackgroundColor(Color.BLUE);
                            h.setBackgroundColor(Color.BLUE);
                        }else if(i==6){
                            c.setBackgroundColor(Color.BLUE);
                            f.setBackgroundColor(Color.BLUE);
                            j.setBackgroundColor(Color.BLUE);
                        }else if(i==7){
                            d.setBackgroundColor(Color.BLUE);
                            g.setBackgroundColor(Color.BLUE);
                            k.setBackgroundColor(Color.BLUE);
                        }
                        //End the game and reset
                        endGame();
                        //And if there's nine moves with now winner, declare a draw
                    }else if(counter >= 9 && winner==false){
                        tWin.setText("It's a draw!");
                        endGame();
                    }
                }
            }
        }else{
            //Basically the same process as our X's but with the circles
            vv.setImageResource(R.drawable.circle);
            oArray[oCounter]= Integer.parseInt((String)vv.getTag());
            sswitch= true;
            vv.setClickable(false);
            tWin.setText("X's Turn!");
            counter++;
            oCounter++;
            if(oCounter >=3){
                for(int i=0;i<8;i++){
                    if(Arrays.toString(oArray).contains(String.valueOf(winArr[i][0]))&&Arrays.toString(oArray).contains(String.valueOf(winArr[i][1]))&&Arrays.toString(oArray).contains(String.valueOf(winArr[i][2]))){
                        tWin.setText("O wins!");
                        winner=true;
                        if(i==0){
                            b.setBackgroundColor(Color.BLUE);
                            c.setBackgroundColor(Color.BLUE);
                            d.setBackgroundColor(Color.BLUE);
                        } else if(i==1){
                            b.setBackgroundColor(Color.BLUE);
                            e.setBackgroundColor(Color.BLUE);
                            h.setBackgroundColor(Color.BLUE);
                        }else if(i==2){
                            b.setBackgroundColor(Color.BLUE);
                            f.setBackgroundColor(Color.BLUE);
                            k.setBackgroundColor(Color.BLUE);
                        }else if(i==3){
                            e.setBackgroundColor(Color.BLUE);
                            f.setBackgroundColor(Color.BLUE);
                            g.setBackgroundColor(Color.BLUE);
                        }else if(i==4){
                            h.setBackgroundColor(Color.BLUE);
                            j.setBackgroundColor(Color.BLUE);
                            k.setBackgroundColor(Color.BLUE);
                        }else if(i==5){
                            d.setBackgroundColor(Color.BLUE);
                            f.setBackgroundColor(Color.BLUE);
                            h.setBackgroundColor(Color.BLUE);
                        }else if(i==6){
                            c.setBackgroundColor(Color.BLUE);
                            f.setBackgroundColor(Color.BLUE);
                            j.setBackgroundColor(Color.BLUE);
                        }else if(i==7){
                            d.setBackgroundColor(Color.BLUE);
                            g.setBackgroundColor(Color.BLUE);
                            k.setBackgroundColor(Color.BLUE);
                        }
                        endGame();
                    }else if(counter >= 9 && winner==false){
                        tWin.setText("It's a draw!");
                        endGame();
                    }
                }
            }
        }

    }

    //Our endgame process. Basically make everything unclickable and declare a winner
    public void endGame(){
        ImageButton b = (ImageButton) findViewById(R.id.imageButton00);
        b.setClickable(false);
        ImageButton c = (ImageButton) findViewById(R.id.imageButton01);
        c.setClickable(false);
        ImageButton d = (ImageButton) findViewById(R.id.imageButton02);
        d.setClickable(false);
        ImageButton e = (ImageButton) findViewById(R.id.imageButton03);
        e.setClickable(false);
        ImageButton f = (ImageButton) findViewById(R.id.imageButton04);
        f.setClickable(false);
        ImageButton g = (ImageButton) findViewById(R.id.imageButton05);
        g.setClickable(false);
        ImageButton h = (ImageButton) findViewById(R.id.imageButton06);
        h.setClickable(false);
        ImageButton j = (ImageButton) findViewById(R.id.imageButton07);
        j.setClickable(false);
        ImageButton k = (ImageButton) findViewById(R.id.imageButton08);
        k.setClickable(false);

    }


}
