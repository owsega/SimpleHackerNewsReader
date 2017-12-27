package com.owsega.hackernews.util;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StringUtilTest {

    @Before
    public void init() {
        StringUtil util = new StringUtil();
    }

    @Test
    public void testSingularPluralComments() throws Exception {
        Assert.assertEquals("0 comments", StringUtil.singularPluralComments(null));
        List<String> items = new ArrayList<>();
        Assert.assertEquals("0 comments", StringUtil.singularPluralComments(items));
        items.add("1 item");
        Assert.assertEquals("1 comment", StringUtil.singularPluralComments(items));
        items.add("another one");
        Assert.assertEquals("2 comments", StringUtil.singularPluralComments(items));
        items.add("and another");
        Assert.assertEquals("3 comments", StringUtil.singularPluralComments(items));
    }

    @Test
    public void singularPluralPoints() throws Exception {
        Assert.assertEquals("0 points", StringUtil.singularPluralPoints(0L));
        Assert.assertEquals("1 point", StringUtil.singularPluralPoints(1L));
        Assert.assertEquals("35 points", StringUtil.singularPluralPoints(35L));
    }
}
