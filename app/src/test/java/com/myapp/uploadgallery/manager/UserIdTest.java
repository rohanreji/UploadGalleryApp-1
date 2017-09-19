package com.myapp.uploadgallery.manager;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertTrue;

public class UserIdTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    SharedPreferences preferences;

    @Mock
    SharedPreferences.Editor editor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAlreadyInstantiated() {
        Mockito.when(preferences.getString(UserId.USER_ID, null)).thenReturn("123");
        UserId userId = new UserId(preferences);
        assertTrue("123".equals(userId.get()));
    }

    @Test
    public void testNewlyInstantiated() {
        Mockito.when(preferences.getString(UserId.USER_ID, null)).thenReturn("123");
        UserId userId = new UserId(preferences);

        UserId userId2 = new UserId(preferences);
        assertTrue(userId2.get().equals(userId.get()));
    }
}
