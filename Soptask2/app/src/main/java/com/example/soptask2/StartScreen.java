package com.example.soptask2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartScreen extends AppCompatActivity {

    private static final String TAG = "StartScreen";

    EditText name1, name2;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        name1 = findViewById(R.id.editText);
        name2 = findViewById(R.id.editText2);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p1, p2;
                p1 = String.valueOf(name1.getText());
                p2 = String.valueOf(name2.getText());

                Intent toGame = new Intent(StartScreen.this, MainActivity.class);
                toGame.putExtra("name1", p1);
                toGame.putExtra("name2", p2);
                Log.d(TAG, "onClick: name1 " + name1);
                Log.d(TAG, "onClick: name2 " + name2);

                if(p1.length() == 0 || p2.length() == 0) {
                    Toast.makeText(StartScreen.this, "Enter player name(s)", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(toGame);
                }
            }
        });

    }
}
