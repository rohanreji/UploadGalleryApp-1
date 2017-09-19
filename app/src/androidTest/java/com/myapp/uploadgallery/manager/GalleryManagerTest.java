package com.myapp.uploadgallery.manager;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.ui.Viewable;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class GalleryManagerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    UserId userId;

    @Mock
    GalleryEndpoint endpoint;

    @Mock
    Viewable view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        when(interactor.getUserProfile()).thenReturn(Observable.just(new UserProfile()));
//        presenter = new ProfilePresenter(interactor);
//        presenter.attachView(view);
    }

}
