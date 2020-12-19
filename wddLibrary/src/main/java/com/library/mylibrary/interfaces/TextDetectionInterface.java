package com.library.mylibrary.interfaces;

import com.amazonaws.services.textract.model.Block;

import java.util.List;

public interface TextDetectionInterface {

        void textDetectionCallback(List<Block> textDetections, Boolean checkBooleanResulDetectLabel);
    }

