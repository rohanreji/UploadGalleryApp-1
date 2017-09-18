package com.myapp.uploadgallery.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UniqueListTest {

    @Test
    public void add() throws Exception {
        List<Integer> array = new UniqueList<>();
        array.add(1);
        array.add(1);
        Assert.assertEquals(1, array.size());

        array.add(2);
        array.add(2);
        Assert.assertEquals(2, array.size());

    }

    @Test
    public void addAll() throws Exception {
        List<Integer> array = new UniqueList<>();
        List<Integer> additional = Arrays.asList(1, 1, 2, 3, 2, 1);

        array.addAll(additional);
        Assert.assertArrayEquals(Arrays.asList(1, 2, 3).toArray(new Integer[]{}),
                array.toArray(new Integer[]{}));
    }

}