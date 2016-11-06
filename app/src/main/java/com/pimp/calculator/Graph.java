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

import android.content.Context;
import android.view.ViewTreeObserver;

import com.pimp.calculator.FloatingView.GraphView;
import com.xlythe.engine.theme.Theme;
import com.xlythe.math.Point;

import java.util.LinkedList;
import java.util.List;

public class Graph {
    private final Logic mLogic;
    private GraphView mGraphView;
    private List<Point> mData = new LinkedList<Point>();

    public Graph(Logic l) {
        mLogic = l;
    }

    public GraphView createGraph(Context context) {
        mLogic.setGraph(this);

        mGraphView = new GraphView(context);
        mGraphView.setPanListener(new GraphView.PanListener() {
            @Override
            public void panApplied() {
                mLogic.setDomain(mGraphView.getXAxisMin(), mGraphView.getXAxisMax());
                mLogic.setRange(mGraphView.getYAxisMin(), mGraphView.getYAxisMax());
                mLogic.graph();
            }
        });
        mGraphView.setZoomListener(new GraphView.ZoomListener() {
            @Override
            public void zoomApplied(float level) {
                mLogic.setZoomLevel(level);
                mLogic.graph();
            }
        });

        mGraphView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLogic.setDomain(mGraphView.getXAxisMin(), mGraphView.getXAxisMax());
                mLogic.setRange(mGraphView.getYAxisMin(), mGraphView.getYAxisMax());
            }
        });
        mGraphView.setBackgroundColor(Theme.getColor(context, R.color.graph_background));
        mGraphView.setTextColor(Theme.getColor(context, R.color.graph_labels_color));
        mGraphView.setGridColor(Theme.getColor(context, R.color.graph_grid_color));
        mGraphView.setGraphColor(Theme.getColor(context, R.color.graph_color));
        return mGraphView;
    }

    public List<Point> getData() {
        return mData;
    }

    public void setData(List<Point> data) {
        mData = data;
        mGraphView.setData(mData);
    }
}
