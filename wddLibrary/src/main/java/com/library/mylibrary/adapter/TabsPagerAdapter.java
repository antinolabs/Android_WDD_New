package com.library.mylibrary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.TextDetection;
import com.amazonaws.services.textract.model.Block;
import com.library.mylibrary.Fragment.CompareFaceFragment;
import com.library.mylibrary.Fragment.ObjectDetectionFragment;
import com.library.mylibrary.Fragment.TextDetectionFragment;
import com.library.mylibrary.R;
import java.util.List;


public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.object_detection, R.string.face_comparision, R.string.text_detection};
    private final Context mContext;
    private List<Label> labels;
    private List<CompareFacesMatch> compareFacesMatchList;
    private List<Block> textDetectionImage;
    private Bitmap bitmapcameraImage;
    private Bitmap imagePickeruriImage;
    private int formText,tableText,rawText;



    public TabsPagerAdapter(Context context, FragmentManager fm, List<Label> labels,
                            List<CompareFacesMatch> compareFacesMatchList,
                            Bitmap bitmapcameraImage, Bitmap imagePickeruriImage, List<Block> textDetectionImage, int formText, int tableText, int rawText) {
        super(fm);
        mContext = context;
        this.labels=labels;
        this.compareFacesMatchList=compareFacesMatchList;
        this.bitmapcameraImage=bitmapcameraImage;
        this.imagePickeruriImage=imagePickeruriImage;
        this.textDetectionImage=textDetectionImage;
        this.formText=formText;
        this.tableText=tableText;
        this.rawText=rawText;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ObjectDetectionFragment(labels);
            case 1:
                return new CompareFaceFragment(compareFacesMatchList,bitmapcameraImage,imagePickeruriImage);
            case 2:
                return new  TextDetectionFragment(textDetectionImage,formText,tableText,rawText);
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    public void addAll(List<Label> labels) {

    }
}
