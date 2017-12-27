package com.owsega.hackernews.util;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Utility methods for string manipulations
 */
public class StringUtil {

    /**
     * returns grammatically correct number of comments in given List
     */
    public static String singularPluralComments(@Nullable List items) {
        if (items == null || items.isEmpty()) return "0 comments";
        if (items.size() == 1) return "1 comment";
        return items.size() + " comments";
    }

    /**
     * appends "point" or "points" to the given int depending on whether it is singular or plural
     */
    public static String singularPluralPoints(Long score) {
        if (score == 1) return score + " point";
        return score + " points";
    }
}
