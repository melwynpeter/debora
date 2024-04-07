package com.mel.debora_v11.utilities;

import org.tensorflow.lite.support.label.Category;

import java.util.List;

public interface AudioClassificationListener {
        void onError(String error);
        void onResult(List<Category> results, Long inferenceTime);
}
