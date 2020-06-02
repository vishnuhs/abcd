package com.parkinncharge.parkinncharge.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class historyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public historyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is history fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}