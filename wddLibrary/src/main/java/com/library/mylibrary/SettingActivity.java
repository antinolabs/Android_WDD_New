package com.library.mylibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.library.mylibrary.colorpicker.ColorPickerView;
import com.library.mylibrary.colorpicker.OnColorChangedListener;
import com.library.mylibrary.colorpicker.OnColorSelectedListener;
import com.library.mylibrary.colorpicker.builder.ColorPickerClickListener;
import com.library.mylibrary.colorpicker.builder.ColorPickerDialogBuilder;

public class SettingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private int currentBackgroundColor = 0xffffffff;
    private int sendcolor;
    private int position=0;
    private TextView tvSwitchPositionText, tvFacialMatch, tvId, tvInfo, tvSaveButton;
    private int facialProgress,idProgress,idInfo;
    private SeekBar sbFacialMatch, sbId, sbInfo;
    private SharedPreferences sharedPreferences;
    private ImageView backButton;
    private int selectedColor;
    private static Context context;

    private int tableText=0,formText=0,rawText=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        selectedColor = getIntent().getIntExtra(Constants.COLORTYPE,0);
        if(selectedColor==0)
        {
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable((getColor(android.R.color.holo_blue_bright))));
        }else {
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable((selectedColor)));
        }
        context= SettingActivity.this;
        initView();
        onClickListner();
        Switch sw = (Switch) findViewById(R.id.switch1);;

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    position = 1;
                    tvSwitchPositionText.setText("Right");

                } else {
                    position = 0;
                    tvSwitchPositionText.setText("Left");
                }
            }
        });

        findViewById(R.id.wdd_btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker();
            }

        });

    }

    private void initView() {
        tvSwitchPositionText = (TextView) findViewById(R.id.wdd_tv_position);
        tvFacialMatch = (TextView) findViewById(R.id.wdd_tv_facialMatch);
        sbFacialMatch = (SeekBar) findViewById(R.id.wdd_SeekBar_face);
        tvSaveButton=(TextView) findViewById(R.id.wdd_tv_save_button);
        backButton = findViewById(R.id.wdd_back_to_home_setting);
    }

    private void onClickListner() {
        sbFacialMatch.setOnSeekBarChangeListener(this);
        tvSaveButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void colorPicker() {
        final Context context = SettingActivity.this;
        ColorPickerDialogBuilder
                .with(context)
                .setTitle(R.string.color_dialog_title)
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int selectedColor) {
                        // Handle on color change
                        Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                        if (allColors != null) {
                            StringBuilder sb = null;

                            for (Integer color : allColors) {
                                if (color == null)
                                    continue;
                                if (sb == null)
                                    sb = new StringBuilder("Color List:");
                                sendcolor=color;

                                sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                            }

                            if (sb != null)
                                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .showColorEdit(true)
                .setColorEditTextColor(ContextCompat.getColor(SettingActivity.this, android.R.color.holo_blue_bright))
                .build()
                .show();
    }




    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.wdd_SeekBar_face) {
            tvFacialMatch.setText("FACIALMATCH "+progress+"%");
            this.facialProgress=progress;
        }
    }




    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {

        if(R.id.wdd_tv_save_button==v.getId()){
            CustomButton.setColor(sendcolor);
            CustomButton.positionOfButton(position);
            CustomButton.TextRactType(tableText,rawText,formText);
            finish();
        }
        else if(R.id.wdd_back_to_home_setting==v.getId())
        {
            startActivity(new Intent(this,WDDStarter.class));
            finish();
        }

    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        int id = view.getId();
        if (id == R.id.radio_raw_text) {
            if (checked)
                // Pirates are the best
                rawText=1;
                return;

        } else if (id == R.id.radio_form_text) {
            if (checked)
            // Ninjas rule
            {
                formText=1;
                return;
            }
        }else if (id == R.id.radio_table_text) {
            if (checked) {
                tableText=1;
                return;
            }
        }
    }


    public static class AdaptedDialogFragment extends AppCompatDialogFragment {
        public AdaptedDialogFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_adapted_dialog, container);
        }
    }




}
