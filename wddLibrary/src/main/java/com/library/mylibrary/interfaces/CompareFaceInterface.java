package com.library.mylibrary.interfaces;

import com.amazonaws.services.rekognition.model.CompareFacesMatch;

import java.util.List;

    public interface CompareFaceInterface
    {
        void compareFaceCallback(List<CompareFacesMatch> list, Boolean checkvalueCompareFace);
    }

