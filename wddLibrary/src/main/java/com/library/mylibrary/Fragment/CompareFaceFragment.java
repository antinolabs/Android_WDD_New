package com.library.mylibrary.Fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.library.mylibrary.R;

import java.util.ArrayList;
import java.util.List;


public class CompareFaceFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "page_number";
    private String mParam1;
    private List<CompareFacesMatch> compareFacesMatchList= new ArrayList<>();
    private Bitmap bitmap;
    private Bitmap imagePickeruriImage;
    private int count=0;


    public CompareFaceFragment(List<CompareFacesMatch> compareFacesMatchList, Bitmap bitmapcameraImage, Bitmap imagePickeruriImage) {
        // Required empty public constructor
        this.compareFacesMatchList=compareFacesMatchList;
        this.bitmap=bitmapcameraImage;
        this.imagePickeruriImage=imagePickeruriImage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_compare_face, container, false);
        ImageView imageView = rootView.findViewById(R.id.wdd_iv_cameramatching_picture);
        imageView.setImageBitmap(bitmap);
        TextView textViewResult = rootView.findViewById(R.id.wdd_tv_cameramatching_picture_result);

        ImageView imageViewPicker = rootView.findViewById(R.id.wdd_iv_imagematching_picture);
        imageViewPicker.setImageBitmap(imagePickeruriImage);

        if (!compareFacesMatchList.isEmpty()) {
            for (CompareFacesMatch match: compareFacesMatchList){
                if(match.getSimilarity()>=80f)
                {
                    textViewResult.setText("Matched\n"+ match.getSimilarity()+"%");
                }
            }
            }
        else
        {
            textViewResult.setText("Face does not match");
        }
        return  rootView;

    }
}
