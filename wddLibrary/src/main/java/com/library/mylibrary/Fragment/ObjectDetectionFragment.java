package com.library.mylibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.amazonaws.services.rekognition.model.Label;
import com.library.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

public class ObjectDetectionFragment extends Fragment {
    private  List<Label> labels;

    public ObjectDetectionFragment(List<Label> labels) {
        this.labels=labels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_layout, container, false);
        final ListView list = rootView.findViewById(R.id.wdd_list_label);
        ArrayList<String> arrayListLables = new ArrayList<>();

        if(labels!=null) {
            for (Label label : labels) {
                System.out.println("Label: " + label.getName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayListLables);
                list.setAdapter(arrayAdapter);
                arrayListLables.add(label.getName() + " " + label.getConfidence().toString());
            }
        }else
        {
            Toast.makeText(getContext(), "Labels is not available", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }
}