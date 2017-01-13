/*
 * Calculator Begins
 * Copyright (C) 2017  Adithya J
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

package com.afollestad.appthemeengine.viewprocessors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.tagprocessors.TagProcessor;

/**
 * @author Aidan Follestad (afollestad)
 */
public class DefaultProcessor implements ViewProcessor<View, Void> {

    @Override
    public void process(@NonNull Context context, @Nullable String key, @Nullable View view, @Nullable Void extra) {
        if (view == null || view.getTag() == null || !(view.getTag() instanceof String))
            return;
        final String tag = (String) view.getTag();
        if (tag.contains(",")) {
            final String[] splitTag = tag.split(",");
            for (String part : splitTag)
                processTagPart(context, key, view, part);
        } else {
            processTagPart(context, key, view, tag);
        }
    }

    private void processTagPart(@NonNull Context context, @Nullable String key, @NonNull View view, @NonNull String part) {
        final int pipe = part.indexOf('|');
        if (pipe == -1) return;
        final String prefix = part.substring(0, pipe);
        final String suffix = part.substring(pipe + 1);
        final TagProcessor processor = ATE.getTagProcessor(prefix);
        if (processor != null) {
            if (!processor.isTypeSupported(view)) {
                throw new IllegalStateException(String.format("A view of type %s cannot use %s tags.",
                        view.getClass().getName(), prefix));
            }
            try {
                processor.process(context, key, view, suffix);
            } catch (Throwable t) {
                //throw new RuntimeException(String.format("Failed to run %s: %s", processor.getClass().getName(), t.getMessage()), t);
            }
        } else {
            throw new IllegalStateException("No ATE tag processors found by prefix " + prefix);
        }
    }
}