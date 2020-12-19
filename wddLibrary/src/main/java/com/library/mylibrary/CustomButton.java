package com.library.mylibrary;


import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CustomButton extends RelativeLayout implements View.OnClickListener {


    public static Activity context;
    private LinearLayout linearLayout;
    private static String position = "left";
    private static int counterOfView = 0;
    private static int selectedColor;
    private static String AccessKeyToken = "";
    private static String SecretKeyToken = "";
    private static String CognitioPoolId = "";
    private static String WddBucketOnboarding = "";
    private static int tableText=0,formText=0,rawText=0;



    public CustomButton(Activity context) {
        super(context);
        this.context = context;

    }

    public void init() {
        //First Time Button load is in Left Side
        if (counterOfView == 0) {
            if (position.equals("left")) {
                LayoutParams bparams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                bparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);


                //ALL MARGIN AND PADDING FOR THE BUTTONS
                int width = 70;
                int height = 70;
                int radius = 35;
                int paddingImage = 35;
                int marginLeft = 25;
                int marginBottom = 25;
                int dpWidthCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());
                int dpHeightCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
                int dpCardRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, context.getResources().getDisplayMetrics());
                int dppaddingImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingImage, context.getResources().getDisplayMetrics());
                int dpmarginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginLeft, context.getResources().getDisplayMetrics());
                int dpmarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginBottom, context.getResources().getDisplayMetrics());


                //CIRCULAR CARDVIEW WITH IMAGE
                CardView card = new CardView(context);
                card.setLayoutParams(new RelativeLayout.LayoutParams(dpWidthCard, dpHeightCard));
                card.setId((int) 0X101);
                card.setRadius(dpCardRadius);
                if (counterOfView == 0)
                    card.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_blue_bright)));
                else
                    card.setCardBackgroundColor(selectedColor);

                //IMAGE IN CARDVIEW
                ImageView imageView = new ImageView(context);
                imageView.setPadding(dppaddingImage, dppaddingImage, dppaddingImage, dppaddingImage);
                imageView.setBackgroundResource(R.drawable.flash);
                card.addView(imageView);

                //MARGIN FOR CARDVIEW IN PARENT LAYOUT
                LinearLayout.LayoutParams buttonMarginWithoutBubbleParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                buttonMarginWithoutBubbleParams.setMargins(dpmarginLeft, 0, 0, dpmarginBottom);
                card.setLayoutParams(buttonMarginWithoutBubbleParams);

                //CARDVIEW IS ADDED INSIDE IN LINEARLAYOUT
                linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(bparams);
                linearLayout.addView(card);
                addView(linearLayout);
                card.setOnClickListener(this);
            }
        }
        if (counterOfView != 0) {

            if (position.equals("left")) {
                LayoutParams bparams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                bparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                int width = 70;
                int height = 70;
                int radius = 35;
                int paddingImage = 35;
                int marginLeft = 25;
                int marginBottom = 25;
                int dpWidthCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());
                int dpHeightCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
                int dpCardRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, context.getResources().getDisplayMetrics());
                int dppaddingImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingImage, context.getResources().getDisplayMetrics());
                int dpmarginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginLeft, context.getResources().getDisplayMetrics());
                int dpmarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginBottom, context.getResources().getDisplayMetrics());

                CardView card = new CardView(context);
                card.setLayoutParams(new RelativeLayout.LayoutParams(dpWidthCard, dpHeightCard));
                card.setId((int) 0X101);
                card.setRadius(dpCardRadius);
                if (counterOfView == 0)
                    card.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_blue_bright)));
                else
                    card.setCardBackgroundColor(selectedColor);

                ImageView imageView = new ImageView(context);
                imageView.setPadding(dppaddingImage, dppaddingImage, dppaddingImage, dppaddingImage);
                imageView.setBackgroundResource(R.drawable.flash);
                card.addView(imageView);

                //Margin for the Card
                LinearLayout.LayoutParams buttonMarginWithoutBubbleParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                buttonMarginWithoutBubbleParams.setMargins(dpmarginLeft, 0, 0, dpmarginBottom);

                //Custom Margin Added for Button
                card.setLayoutParams(buttonMarginWithoutBubbleParams);

                //A Layout Where Button is added
                linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(bparams);
                linearLayout.addView(card);
                addView(linearLayout);
                card.setOnClickListener(this);
            } else if (position.equals("right")) {

                LayoutParams bparams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                bparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                int width = 70;
                int height = 70;
                int radius = 35;
                int paddingImage = 35;
                int marginLeft = 25;
                int marginBottom = 25;
                int dpWidthCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());
                int dpHeightCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
                int dpCardRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, context.getResources().getDisplayMetrics());
                int dppaddingImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingImage, context.getResources().getDisplayMetrics());
                int dpmarginRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginLeft, context.getResources().getDisplayMetrics());
                int dpmarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginBottom, context.getResources().getDisplayMetrics());

                CardView card = new CardView(context);
                card.setLayoutParams(new RelativeLayout.LayoutParams(dpWidthCard, dpHeightCard));
                card.setId((int) 0X101);
                card.setRadius(dpCardRadius);
                if (counterOfView == 0)
                    card.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_blue_bright)));
                else
                    card.setCardBackgroundColor(selectedColor);

                ImageView imageView = new ImageView(context);
                imageView.setPadding(dppaddingImage, dppaddingImage, dppaddingImage, dppaddingImage);
                imageView.setBackgroundResource(R.drawable.flash);
                card.addView(imageView);

                //Margin for the Card
                LinearLayout.LayoutParams buttonMarginWithoutBubbleParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                buttonMarginWithoutBubbleParams.setMargins(0, 0, dpmarginRight, dpmarginBottom);

                //Custom Margin Added for Button
                card.setLayoutParams(buttonMarginWithoutBubbleParams);

                //A Layout Where Button is added
                linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(bparams);
                linearLayout.addView(card);
                addView(linearLayout);
                card.setOnClickListener(this);
            }
        }
    }

    //PICK COLOR FROM COLORPICKER
    public static void setColor(int SelectedColors) {
        selectedColor = SelectedColors;
        counterOfView++;

    }

    //POSITION OF BUTTON
    public static String positionOfButton(int value) {
        counterOfView++;
        if (value == 0) {
            position = "left";
            return position;
        } else if (value == 1) {
            position = "right";
            return position;
        }

        return null;

    }

    public static void KeyAccess(String AccessKey, String SecretKey, String CognitoPoolId, String BucketName) {

        AccessKeyToken = AccessKey;
        SecretKeyToken = SecretKey;
        CognitioPoolId = CognitoPoolId;
        WddBucketOnboarding = BucketName;

        Log.d(TAG, "KeyAccess: "+ AccessKeyToken +  SecretKeyToken +  CognitioPoolId + WddBucketOnboarding );

    }


    public static void TextRactType(int table, int raw,int form)
    {
        tableText = table;
        rawText = raw;
        formText =form;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0X101:
                Log.d(TAG, "onCreate: "+AccessKeyToken+SecretKeyToken+CognitioPoolId+WddBucketOnboarding);
                context.startActivity(new Intent(context, WDDStarter.class).putExtra(Constants.ACCESS_KEY, AccessKeyToken)
                        .putExtra(Constants.SECRET_KEY, SecretKeyToken)
                        .putExtra(Constants.COGNITO_POOL_ID, CognitioPoolId)
                        .putExtra(Constants.BUCKET_NAME, WddBucketOnboarding)
                        .putExtra(Constants.TABLETEXT,tableText)
                        .putExtra(Constants.FORMTEXT,formText)
                        .putExtra(Constants.RAWTEXT,rawText)
                .putExtra(Constants.COLORTYPE,selectedColor));
                removeAllViews();

        }
    }
}

