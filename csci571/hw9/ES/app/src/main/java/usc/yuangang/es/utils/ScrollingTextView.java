package usc.yuangang.es.utils;


import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Rect;
import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;


public class ScrollingTextView extends AppCompatTextView {

    public ScrollingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollingTextView(Context context) {
        super(context);
    }

    public boolean mFocus = false;

    public void setFocus(boolean mFocus) {
        this.mFocus = mFocus;
    }

    @Override
    public boolean isFocused() {
        return mFocus;
    }
}



