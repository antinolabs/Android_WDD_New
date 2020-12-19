package com.library.mylibrary.interfaces;

import com.amazonaws.services.rekognition.model.Label;

import java.util.List;

public interface DetectLabelCallback {

        void detectLabelCallBack(List<Label> labels, Boolean checkvalueCompareFace);
    }

