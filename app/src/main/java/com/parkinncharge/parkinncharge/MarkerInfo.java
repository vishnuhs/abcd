package com.parkinncharge.parkinncharge;

import android.widget.Button;
import android.widget.TextView;

public class MarkerInfo {
    String markername;double markerdist;

    public MarkerInfo(String markername, double markerdist) {
        this.markername = markername;
        this.markerdist = markerdist;
    }

    public String getMarkername() {
        return markername;
    }

    public void setMarkername(String markername) {
        this.markername = markername;
    }

    public double getMarkerdist() {
        return markerdist;
    }

    public void setMarkerdist(double markerdist) {
        this.markerdist = markerdist;
    }
}
