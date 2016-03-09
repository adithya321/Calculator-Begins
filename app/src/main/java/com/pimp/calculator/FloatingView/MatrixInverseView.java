package com.pimp.calculator.FloatingView;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.InputType;

import com.pimp.calculator.MutableString;
import com.pimp.calculator.R;
import com.xlythe.engine.theme.Theme;
import com.xlythe.engine.theme.ThemedTextView;

public class MatrixInverseView extends ThemedTextView {
    private final static char PLACEHOLDER = '\uFEFF';
    public final static String PATTERN = PLACEHOLDER + "^-1";

    public MatrixInverseView(Context context) {
        super(context);
    }

    public MatrixInverseView(final AdvancedDisplay display) {
        super(display.getContext());
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setText(Html.fromHtml("<sup><small>-1</small></sup>"));
        setTextAppearance(display.getContext(), R.style.Theme_Calculator_Display);
        setPadding(0, 0, 0, 0);
        setTextColor(Theme.get(R.color.display_text_color));
        Typeface tf = Theme.getFont(getContext());
        if (tf != null) setTypeface(tf);
        setFont("display_font");
    }

    public static boolean load(final MutableString text, final AdvancedDisplay parent) {
        boolean changed = MatrixInverseView.load(text, parent, parent.getChildCount());
        if (changed) {
            // Always append a trailing EditText
            CalculatorEditText.load(parent);
        }
        return changed;
    }

    public static boolean load(final MutableString text, final AdvancedDisplay parent, final int pos) {
        if (!text.startsWith(PATTERN)) return false;

        text.setText(text.substring(PATTERN.length()));

        MatrixInverseView mv = new MatrixInverseView(parent);
        parent.addView(mv, pos);

        return true;
    }

    @Override
    public String toString() {
        return PATTERN;
    }
}
