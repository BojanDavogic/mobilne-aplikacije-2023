package com.example.slagalica.pomocniAlati;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

public class DisableTouchFragment {
    private Context context;
    private View overlayView;
    private ViewGroup rootView;

    public DisableTouchFragment(Context context) {
        this.context = context;
        initOverlay();
    }

    private void initOverlay() {
        rootView = ((FragmentActivity) context).findViewById(android.R.id.content);

        overlayView = new View(context);
        overlayView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        overlayView.setBackgroundColor(Color.argb(120, 20, 20, 20));

        overlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void enableTouch() {
        if (rootView != null && overlayView != null && overlayView.getParent() != null) {
            rootView.removeView(overlayView);
        }
    }

    public void disableTouch() {
        if (rootView != null && overlayView != null && overlayView.getParent() == null) {
            rootView.addView(overlayView);
        }
    }
}
