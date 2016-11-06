/*
 * Calculator Begins
 * Copyright (C) 2016  Adithya J
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.pimp.calculator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.pimp.calculator.FloatingView.CalculatorDisplay;
import com.pimp.calculator.FloatingView.CalculatorViewPager;
import com.pimp.calculator.FloatingView.Cling;
import com.pimp.calculator.FloatingView.HistoryLine;
import com.pimp.calculator.Page.LargePanel;
import com.pimp.calculator.Page.NormalPanel;
import com.pimp.calculator.Page.SmallPanel;
import com.xlythe.engine.theme.Theme;
import com.xlythe.math.Base;
import com.xlythe.math.BaseModule;
import com.xlythe.slider.Slider;
import com.xlythe.slider.Slider.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Calculator extends Activity implements Logic.Listener, OnClickListener, OnMenuItemClickListener, CalculatorViewPager.OnPageChangeListener {
    private static final String STATE_CURRENT_VIEW = "state-current-view";
    private static final String STATE_CURRENT_VIEW_SMALL = "state-current-view-small";
    private static final String STATE_CURRENT_VIEW_LARGE = "state-current-view-large";
    public EventListener mListener = new EventListener();
    public boolean flag;
    private CalculatorDisplay mDisplay;
    private Persist mPersist;
    private History mHistory;
    private ListView mHistoryView;
    private BaseAdapter mHistoryAdapter;
    private Logic mLogic;
    private CalculatorViewPager mPager;
    private CalculatorViewPager mSmallPager;
    private CalculatorViewPager mLargePager;
    private View mClearButton;
    private View mBackspaceButton;
    private View mOverflowMenuButton;
    private Slider mHistorySlider;
    private Graph mGraph;
    private List<Page> mPages;
    private TextView mDetails;
    private boolean clingActive = false;

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Disable IME for this application
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        Theme.buildResourceMap(com.pimp.calculator.R.class);
        Theme.setPackageName(CalculatorSettings.getTheme(getContext()));
        Theme.setPackageOverride("com.pimp.calculator");
        int customTheme = Theme.getTheme(getContext());
        if (customTheme != 0) {
            super.setTheme(customTheme);
        }

        setContentView(R.layout.main);

        mPager = (CalculatorViewPager) findViewById(R.id.panelswitch);

        if (mClearButton == null) {
            mClearButton = findViewById(R.id.clear);
            mClearButton.setOnClickListener(mListener);
            mClearButton.setOnLongClickListener(mListener);
        }
        if (mBackspaceButton == null) {
            mBackspaceButton = findViewById(R.id.del);
            mBackspaceButton.setOnClickListener(mListener);
            mBackspaceButton.setOnLongClickListener(mListener);
        }

        mDetails = (TextView) findViewById(R.id.details);

        mDisplay = (CalculatorDisplay) findViewById(R.id.display);

        mLogic = new Logic(this, mDisplay);
        mLogic.setListener(this);
        mLogic.getBaseModule().setOnBaseChangeListener(new BaseModule.OnBaseChangeListener() {
            @Override
            public void onBaseChange(Base newBase) {
                updateDetails();
            }
        });
        mLogic.setLineLength(mDisplay.getMaxDigits());

        mHistorySlider = (Slider) findViewById(R.id.pulldown);
        mHistorySlider.setBarHeight(getResources().getDimensionPixelSize(R.dimen.history_bar_height));
        mHistorySlider.setSlideDirection(Direction.DOWN);
        if (CalculatorSettings.clickToOpenHistory(this)) {
            mHistorySlider.enableClick(true);
            mHistorySlider.enableTouch(false);
        }
        mHistorySlider.setBarBackground(Theme.getDrawable(getContext(), R.drawable.btn_slider));
        mHistorySlider.enableVibration(CalculatorSettings.vibrateOnPress(getContext()), CalculatorSettings.getVibrationStrength());
        Drawable sliderBackground = Theme.getDrawable(getContext(), "slider_background");
        if (sliderBackground == null)
            sliderBackground = Theme.getDrawable(getContext(), R.drawable.background);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            mHistorySlider.setBackgroundDrawable(sliderBackground);
        } else {
            mHistorySlider.setBackground(sliderBackground);
        }
        mHistoryView = (ListView) mHistorySlider.findViewById(R.id.history);

        mGraph = new Graph(mLogic);

        Page matrix = new Page(getContext(), LargePanel.MATRIX);
        if (mPager != null) {
            CalculatorPageAdapter adapter = new PageAdapter(getContext(), mListener, mGraph, mLogic);
            mPages = adapter.getPages();
            mPager.setAdapter(adapter);
            mPager.scrollToMiddle();
            if (state != null) {
                mPager.setCurrentItem(state.getInt(STATE_CURRENT_VIEW, mPager.getCurrentItem()));
            } else {
                Page basic = new Page(getContext(), NormalPanel.BASIC);
                if (CalculatorSettings.isPageEnabled(getContext(), basic)) {
                    scrollToPage(basic);
                }
            }
            mPages = Page.removeDuplicates(mPages);
            mPager.setOnPageChangeListener(this);
            runCling(false);
            mListener.setHandler(this, mLogic, mPager);
        } else if (mSmallPager != null && mLargePager != null) {
            // Expanded UI
            CalculatorPageAdapter smallAdapter = new SmallPageAdapter(getContext(), mLogic);
            CalculatorPageAdapter largeAdapter = new LargePageAdapter(getContext(), mListener, mGraph, mLogic);
            mPages = new ArrayList<Page>(smallAdapter.getPages());
            mPages.addAll(largeAdapter.getPages());
            mSmallPager.setAdapter(smallAdapter);
            mLargePager.setAdapter(largeAdapter);
            mSmallPager.scrollToMiddle();
            mLargePager.scrollToMiddle();
            if (state != null) {
                mSmallPager.setCurrentItem(state.getInt(STATE_CURRENT_VIEW, mSmallPager.getCurrentItem()));
                mLargePager.setCurrentItem(state.getInt(STATE_CURRENT_VIEW, mLargePager.getCurrentItem()));
            } else {
                Page basic = new Page(getContext(), LargePanel.BASIC);
                Page advanced = new Page(getContext(), SmallPanel.ADVANCED);
                if (CalculatorSettings.isPageEnabled(getContext(), basic)) {
                    scrollToPage(basic);
                }
                if (CalculatorSettings.isPageEnabled(getContext(), advanced)) {
                    scrollToPage(advanced);
                }
            }
            mPages = Page.removeDuplicates(mPages);

            mSmallPager.setOnPageChangeListener(this);
            mLargePager.setOnPageChangeListener(this);
            runCling(false);
            mListener.setHandler(this, mLogic, mSmallPager, mLargePager);
        }

        mDisplay.setOnKeyListener(mListener);

        updateDeleteMode();

        mHistorySlider.bringToFront();
        updateDetails();

        boolean main = getIntent().getBooleanExtra("MAIN", false);
        if (!main)
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        mLogic.updateHistory();
        mPersist.setDeleteMode(mLogic.getDeleteMode());
        mPersist.setMode(mLogic.getBaseModule().getBase());
        mPersist.save();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load new history
        mPersist = new Persist(this);
        mPersist.load();

        if (mPersist.getMode() != null) mLogic.getBaseModule().setBase(mPersist.getMode());
        mLogic.setDeleteMode(mPersist.getDeleteMode());

        mHistory = mPersist.mHistory;

        mLogic.setHistory(mHistory);
        mLogic.resumeWithHistory();

        mHistoryAdapter = new HistoryAdapter(this, mHistory);
        mHistory.setObserver(mHistoryAdapter);
        setUpHistory();
    }

    private void setUpHistory() {
        registerForContextMenu(mHistoryView);
        mHistoryView.setAdapter(mHistoryAdapter);
        mHistoryView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mHistoryView.setStackFromBottom(true);
        mHistoryView.setFocusable(false);
        mHistoryView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int deleteMode = mLogic.getDeleteMode();
                if (mDisplay.getText().isEmpty()) deleteMode = Logic.DELETE_MODE_CLEAR;
                mDisplay.insert(((HistoryLine) view).getHistoryEntry().getEdited());
                mLogic.setDeleteMode(deleteMode);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_history:
                mHistory.clear();
                mLogic.onClear();
                mHistoryAdapter.notifyDataSetInvalidated();
                break;

            case R.id.show_history:
                mHistorySlider.animateSliderOpen();
                break;

            case R.id.hide_history:
                mHistorySlider.animateSliderClosed();
                break;
            default:
                // Menu item is for switching pages
                scrollToPage(Page.getPage(mPages, item.getTitle().toString()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_top, menu);

        for (Page p : mPages) {
            menu.add(p.getName());
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        Page page = mPager == null ? Page.getCurrentPage(mLargePager) : Page.getCurrentPage(mPager);
        Page smallPage = mPager == null ? Page.getCurrentPage(mSmallPager) : null;

        for (int i = 0; i < menu.size(); i++) {
            MenuItem m = menu.getItem(i);

            boolean equalToLargePage = page != null && m.getTitle().toString().equals(page.getName());
            boolean equalToSmallPage = smallPage != null && m.getTitle().toString().equals(smallPage.getName());

            m.setVisible(!mHistorySlider.isSliderOpen() && !equalToLargePage && !equalToSmallPage);
        }

        MenuItem clearHistory = menu.findItem(R.id.clear_history);
        clearHistory.setVisible(mHistorySlider.isSliderOpen());

        MenuItem showHistory = menu.findItem(R.id.show_history);
        showHistory.setVisible(!mHistorySlider.isSliderOpen());

        MenuItem hideHistory = menu.findItem(R.id.hide_history);
        hideHistory.setVisible(mHistorySlider.isSliderOpen());

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        View history = mHistoryAdapter.getView(info.position, null, null);
        if (history instanceof HistoryLine) ((HistoryLine) history).onCreateContextMenu(menu);
    }

    private boolean getPagingEnabled() {
        if (mPager != null) return mPager.getPagingEnabled();
        if (mSmallPager != null) return mSmallPager.getPagingEnabled();
        if (mLargePager != null) return mLargePager.getPagingEnabled();
        return true;
    }

    private void setPagingEnabled(boolean enabled) {
        if (mPager != null) mPager.setPagingEnabled(enabled);
        if (mSmallPager != null) mSmallPager.setPagingEnabled(enabled);
        if (mLargePager != null) mLargePager.setPagingEnabled(enabled);
    }

    private Context getContext() {
        return Calculator.this;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return onOptionsItemSelected(item);
    }

    protected void scrollToPage(Page p) {
        CalculatorViewPager pager = mPager;
        int order = Page.getOrder(mPages, p);
        int pagesSize = mPages.size();

        if (pager == null) {
            pager = p.isSmall() ? mSmallPager : mLargePager;
            order = Page.getOrder(((CalculatorPageAdapter) pager.getAdapter()).getPages(), p);
            pagesSize = ((CalculatorPageAdapter) pager.getAdapter()).getPages().size();
        }

        if (CalculatorSettings.useInfiniteScrolling(getContext())) {
            int offset = 0;
            while ((pager.getCurrentItem() + offset) % pagesSize != order && (pager.getCurrentItem() - offset) % pagesSize != order) {
                offset++;
            }
            if ((pager.getCurrentItem() + offset) % pagesSize == order) {
                pager.setCurrentItem(pager.getCurrentItem() + offset);
            } else {
                pager.setCurrentItem(pager.getCurrentItem() - offset);
            }
        } else {
            pager.setCurrentItem(order);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        Page matrix = new Page(getContext(), LargePanel.MATRIX);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!clingActive) {
                if (mHistorySlider.isSliderOpen()) {
                    mHistorySlider.animateSliderClosed();
                    return true;
                } else if (mPager != null && Page.getCurrentPage(mPager) != null && !Page.getCurrentPage(mPager).isBasic() && CalculatorSettings.isPageEnabled(getContext(), NormalPanel.BASIC)) {
                    // Infinite scrolling makes this tricky
                    scrollToPage(new Page(getContext(), NormalPanel.BASIC));
                    return true;
                } else if (mSmallPager != null && mLargePager != null) {
                    boolean scrolled = false;
                    if (CalculatorSettings.isPageEnabled(getContext(), SmallPanel.ADVANCED)) {
                        if (!Page.getCurrentPage(mSmallPager).isAdvanced()) {
                            scrollToPage(new Page(getContext(), SmallPanel.ADVANCED));
                            scrolled = true;
                        }
                    }
                    if (CalculatorSettings.isPageEnabled(getContext(), LargePanel.BASIC)) {
                        if (!Page.getCurrentPage(mLargePager).isBasic()) {
                            scrollToPage(new Page(getContext(), LargePanel.BASIC));
                            scrolled = true;
                        }
                    }
                    if (!scrolled) finish();
                    return true;
                }
            }

            finish();
            return true;
        }

        return super.onKeyDown(keyCode, keyEvent);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        if (mPager != null) {
            state.putInt(STATE_CURRENT_VIEW, mPager.getCurrentItem());
        }

        if (mSmallPager != null) {
            state.putInt(STATE_CURRENT_VIEW_SMALL, mSmallPager.getCurrentItem());
        }

        if (mLargePager != null) {
            state.putInt(STATE_CURRENT_VIEW_LARGE, mLargePager.getCurrentItem());
        }
    }

    @Override
    public void onDeleteModeChange() {
        updateDeleteMode();
    }

    private void updateDeleteMode() {
        if (mLogic.getDeleteMode() == Logic.DELETE_MODE_BACKSPACE) {
            mClearButton.setVisibility(View.GONE);
            mBackspaceButton.setVisibility(View.VISIBLE);
        } else {
            mClearButton.setVisibility(View.VISIBLE);
            mBackspaceButton.setVisibility(View.GONE);
        }
    }

    public void showFirstRunSimpleCling(boolean animate) {
        // Enable the clings only if they have not been dismissed before
        if (isClingsEnabled() && !CalculatorSettings.isDismissed(getContext(), Cling.SIMPLE_CLING_DISMISSED_KEY)) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int[] location = new int[3];
            location[0] = 0;
            location[1] = size.y / 2;
            location[2] = 10;
            initCling(R.id.simple_cling, location, 0, true, animate);
        } else {
            removeCling(R.id.simple_cling);
        }
    }

    /* Cling related */
    private boolean isClingsEnabled() {
        // disable clings when running in a test harness
        return !ActivityManager.isRunningInTestHarness();
    }

    private Cling initCling(int clingId, int[] positionData, float revealRadius, boolean showHand, boolean animate) {
        setPagingEnabled(false);
        clingActive = true;

        Cling cling = (Cling) findViewById(clingId);
        if (cling != null) {
            cling.init(this, positionData, revealRadius, showHand);
            cling.setVisibility(View.VISIBLE);
            cling.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            if (animate) {
                cling.buildLayer();
                cling.setAlpha(0f);
                cling.animate().alpha(1f).setInterpolator(new AccelerateInterpolator()).setDuration(Cling.SHOW_CLING_DURATION).setStartDelay(0).start();
            } else {
                cling.setAlpha(1f);
            }
        }
        return cling;
    }

    private void removeCling(int id) {
        setPagingEnabled(true);
        clingActive = false;

        final View cling = findViewById(id);
        if (cling != null) {
            final ViewGroup parent = (ViewGroup) cling.getParent();
            parent.post(new Runnable() {
                @Override
                public void run() {
                    parent.removeView(cling);
                }
            });
        }
    }

    public void showFirstRunMatrixCling(boolean animate, View matrixPage) {
        // Enable the clings only if they have not been dismissed before
        if (isClingsEnabled() && !CalculatorSettings.isDismissed(getContext(), Cling.MATRIX_CLING_DISMISSED_KEY)) {
            View v = matrixPage.findViewById(R.id.matrix);
            int[] location = new int[3];

            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v);
                    dismissMatrixCling(v);
                    v.setOnClickListener(mListener);
                }
            });

            v.getLocationOnScreen(location);
            location[0] = location[0] + v.getWidth() / 2;
            location[1] = location[1] + v.getHeight() / 2;
            location[2] = -1;
            initCling(R.id.matrix_cling, location, v.getWidth() / 2, false, animate);
        } else {
            removeCling(R.id.matrix_cling);
        }
    }

    public void dismissMatrixCling(View v) {
        Cling cling = (Cling) findViewById(R.id.matrix_cling);
        dismissCling(cling, Cling.MATRIX_CLING_DISMISSED_KEY, Cling.DISMISS_CLING_DURATION);
    }

    private void dismissCling(final Cling cling, final String flag, int duration) {
        setPagingEnabled(true);
        clingActive = false;

        if (cling != null) {
            cling.dismiss();
            ObjectAnimator anim = ObjectAnimator.ofFloat(cling, "alpha", 0f);
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cling.setVisibility(View.GONE);
                    cling.cleanup();
                    CalculatorSettings.saveKey(getContext(), flag, true);
                }

            });
            anim.start();
        }
    }

    public void showFirstRunHexCling(boolean animate) {
        // Enable the clings only if they have not been dismissed before
        if (isClingsEnabled() && !CalculatorSettings.isDismissed(getContext(), Cling.HEX_CLING_DISMISSED_KEY)) {
            initCling(R.id.hex_cling, null, 0, false, animate);
        } else {
            removeCling(R.id.hex_cling);
        }
    }

    public void showFirstRunGraphCling(boolean animate) {
        // Enable the clings only if they have not been dismissed before
        if (isClingsEnabled() && !CalculatorSettings.isDismissed(getContext(), Cling.GRAPH_CLING_DISMISSED_KEY)) {
            initCling(R.id.graph_cling, null, 0, false, animate);
        } else {
            removeCling(R.id.graph_cling);
        }
    }

    public void dismissSimpleCling(View v) {
        Cling cling = (Cling) findViewById(R.id.simple_cling);
        dismissCling(cling, Cling.SIMPLE_CLING_DISMISSED_KEY, Cling.DISMISS_CLING_DURATION);
    }

    public void dismissHexCling(View v) {
        Cling cling = (Cling) findViewById(R.id.hex_cling);
        dismissCling(cling, Cling.HEX_CLING_DISMISSED_KEY, Cling.DISMISS_CLING_DURATION);
    }

    public void dismissGraphCling(View v) {
        Cling cling = (Cling) findViewById(R.id.graph_cling);
        dismissCling(cling, Cling.GRAPH_CLING_DISMISSED_KEY, Cling.DISMISS_CLING_DURATION);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            setPagingEnabled(true);
            runCling(true);
        }
    }

    private void runCling(boolean animate) {
        Page largePage = mPager == null ? Page.getCurrentPage(mLargePager) : Page.getCurrentPage(mPager);
        Page smallPage = mPager == null ? Page.getCurrentPage(mSmallPager) : null;
        if (largePage != null) largePage.showTutorial(this, animate);
        if (smallPage != null) smallPage.showTutorial(this, animate);
    }

    private void updateDetails() {
        if (mDetails != null && CalculatorSettings.showDetails(getContext())) {
            String text = "";
            String units = CalculatorSettings.useRadians(getContext()) ? getString(R.string.radians) : getString(R.string.degrees);
            String base = "";
            if (CalculatorSettings.isPageEnabled(getContext(), NormalPanel.HEX)) {
                switch (mLogic.getBaseModule().getBase()) {
                    case HEXADECIMAL:
                        base = getString(R.string.hex).toUpperCase(Locale.getDefault());
                        break;
                    case BINARY:
                        base = getString(R.string.bin).toUpperCase(Locale.getDefault());
                        break;
                    case DECIMAL:
                        base = getString(R.string.dec).toUpperCase(Locale.getDefault());
                        break;
                }
            }
            if (!base.isEmpty()) text += base + " | ";
            text += units;

            mDetails.setMovementMethod(LinkMovementMethod.getInstance());
            mDetails.setText(text, BufferType.SPANNABLE);
            Spannable spans = (Spannable) mDetails.getText();
            ClickableSpan clickSpan = getClickableSpan(units);
            spans.setSpan(clickSpan, text.indexOf(units), text.indexOf(units) + units.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                CalculatorSettings.setRadiansEnabled(getContext(), !CalculatorSettings.useRadians(getContext()));
                updateDetails();
            }

            public void updateDrawState(TextPaint ds) {
            }
        };
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        super.onBackPressed();
    }
}
