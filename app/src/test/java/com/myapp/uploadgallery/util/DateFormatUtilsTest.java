package com.myapp.uploadgallery.util;

import com.google.common.truth.Truth;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DateFormatUtilsTest {
    @Test
    public void testParse() {
        String string = "2017-09-18T15:34:20Z";
        DateTime time = new DateTime()
                .withDate(2017, 9, 18)
                .withTime(15, 34, 20, 0);
        Truth.assertThat(DateFormatUtils.parseTime(string)).isEqualTo(time.getMillis());
    }
}
