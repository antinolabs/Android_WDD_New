package com.library.mylibrary.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.TextDetection;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.Relationship;
import com.library.mylibrary.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TextDetectionFragment extends Fragment {
    List<Block> textDetections;
    String lineWord = "";
    String tableDataWord = "";
    List<String> rawTextList = new ArrayList<>();
    List<String> formTextList = new ArrayList<>();
    List<String> tableTextList = new ArrayList<>();
    List<Block> tableListContainer = new ArrayList<>();
    List<Block> keyListContainer = new ArrayList<>();
    HashMap<String, Block> hashMap = new HashMap<>();
    HashMap<String, Block> lineOFKeyValueMap = new HashMap<>();
    ListView list;
    TextView tvNoData;


    private int formText, tableText, rawText;
    int countOFNumberLine = 0, countOFTableLine = 0, countOFformLine = 0;

    public TextDetectionFragment(List<Block> labels, int formText, int tableText, int rawText) {
        this.textDetections = labels;
        this.formText = formText;
        this.tableText = tableText;
        this.rawText = rawText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_detection, container, false);
        list = view.findViewById(R.id.wdd_text_detected);
        tvNoData = view.findViewById(R.id.tv_no_data);
        if (textDetections != null) {
            displayBlockInfo(textDetections);
            if (formText == 1 && countOFformLine > 0) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, formTextList);
                list.setAdapter(arrayAdapter);
            } else if (tableText == 1 && countOFTableLine > 0) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, tableTextList);
                list.setAdapter(arrayAdapter);
            } else if (rawText == 1 && countOFNumberLine > 0) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, rawTextList);
                list.setAdapter(arrayAdapter);
            }
            else
            {
                list.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }

        }

        return view;
    }

    private void displayBlockInfo(List<Block> block) {
        Log.d("ListData", "displayBlockInfo: " + "RunBLOCK");

        for (Block blockObjects : block) {
            if (blockObjects.getBlockType().equals("TABLE")) {
                tableListContainer.add(blockObjects);
            } else if (blockObjects.getEntityTypes() != null && blockObjects.getEntityTypes().get(0).equals("KEY")) {
                keyListContainer.add(blockObjects);
            } else {
                Log.d("ListData Else", "displayBlockInfo: " + "InsideForLoop" + blockObjects);
                hashMap.put(blockObjects.getId(), blockObjects);
            }


            if (blockObjects.getBlockType().equals("LINE")) {
                for (String keyIdCollection : blockObjects.getRelationships().get(0).getIds())
                    lineOFKeyValueMap.put(keyIdCollection, blockObjects);
            }

        }
        Log.d("TableData and hashmap", "displayBlockInfo: " + tableListContainer.size() + hashMap.size());

        //IF Table is available then this code is run.
        for (int i = 0; i < tableListContainer.size(); i++) {
            Block tableBlock = tableListContainer.get(i);
            for (String idTable : tableBlock.getRelationships().get(0).getIds()) {
                Block cellBlock = hashMap.get(idTable);
                lineWord = "";
                try {
                    for (String idCell : cellBlock.getRelationships().get(0).getIds()) {
                        lineWord = lineWord + hashMap.get(idCell).getText();
                        tableTextList.add(lineWord);
                        countOFTableLine++;
                    }
                    Log.d("ListData", "displayBlockInfo: " + lineWord);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        // If KEY_SET is available then this logic work.
        for (int i = 0; i < keyListContainer.size(); i++) {
            Block keyBlock = keyListContainer.get(i);
            if (keyBlock.getRelationships().size() > 1 && keyBlock.getRelationships().get(1).getType().equals("CHILD")) {
                String firstPickerKey = keyBlock.getRelationships().get(1).getIds().get(0);
                Log.d("firstPickerKey", "displayBlockInfo: " + firstPickerKey);
                Log.d("firstKeyName", "displayBlockInfo: " + lineOFKeyValueMap.get(firstPickerKey).getText());
                formTextList.add(lineOFKeyValueMap.get(firstPickerKey).getText());
                countOFformLine++;

            }

        }


        // IF data is available in Line Format
        for (Block lineBlock : block) {
            if (lineBlock.getBlockType().equals("LINE")) {
                rawTextList.add(lineBlock.getText());
                Log.d("rawText", "displayBlockInfo: " + rawTextList.size());
                countOFNumberLine++;
            }
        }


    }

}


