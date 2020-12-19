package com.library.wdd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.mylibrary.CustomButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomButton relativeLayout = new CustomButton(MainActivity.this);

        CustomButton.KeyAccess("","","","git revert -m 1 <commit-hash> ");
        final ConstraintLayout constraintLayout =  findViewById(R.id.parent);
        final TextView textView = findViewById(R.id.tvClick);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relativeLayout.init();


            }
        });

        constraintLayout.addView(relativeLayout);




    }
}
