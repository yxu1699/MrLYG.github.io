package usc.yuangang.es.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import usc.yuangang.es.R;

public class ExpandableTextView extends AppCompatTextView {
    private static final int DEFAULT_COLLAPSED_LINES = 3;
    private int collapsedLines;
    private CharSequence fullText;
    private boolean isCollapsed = true;

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
       /* TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        collapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_collapsedLines, DEFAULT_COLLAPSED_LINES);
        typedArray.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isCollapsed = !isCollapsed;
                updateText();
            }
        });*/
    }

//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        fullText = text;
//        updateText();
//    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        fullText = text;
        post(new Runnable() {
            @Override
            public void run() {
                updateText();
            }
        });
    }
    private void updateText() {

        if (isCollapsed) {
            setMaxLines(collapsedLines);
            super.setText(getTrimmedText(fullText.toString()), BufferType.SPANNABLE);
        } else {
            setMaxLines(Integer.MAX_VALUE);
            super.setText(fullText, BufferType.SPANNABLE);
        }
    }

    private CharSequence getTrimmedText(String text) {
        if (text == null) {
            return "";
        }

        if (getLayout() == null) {
            return text;
        }

        int lastLineVisibleIndex;
        int lineCount = getLineCount();

        if (lineCount <= collapsedLines) {
            return text;
        }

        lastLineVisibleIndex = getLayout().getLineEnd(collapsedLines - 1);

        if (lastLineVisibleIndex >= text.length()) {
            return text;
        }

        SpannableStringBuilder trimmedText = new SpannableStringBuilder(text.substring(0, lastLineVisibleIndex));
        trimmedText.append("...");
        return trimmedText;
    }


}
