package com.eou.screentalker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ScreenTalkerUnitTest{
    private SliderAdapter s;
    @Mock
    List<DataModel> dataModels;
    @Mock
    DataModel dataModel1;
    @Mock
    DataModel dataModel2;
    @Mock
    DataModel dataModel3;

    @Before
    public void initMocks(){
        when(dataModel1.getTtitle()).thenReturn("Mad");
        when(dataModel1.getTurl()).thenReturn("mad picture");
        when(dataModel1.getTvid()).thenReturn("Mad video");

        when(dataModel2.getTtitle()).thenReturn("Vad");
        when(dataModel2.getTurl()).thenReturn("Vad picture");
        when(dataModel2.getTvid()).thenReturn("vad video");

        when(dataModel3.getTtitle()).thenReturn("bad");
        when(dataModel3.getTurl()).thenReturn("bad picture");
        when(dataModel3.getTvid()).thenReturn("bad video");
    }
    @Test
    public void deleteItemsTest(){
        //arrange
        dataModels.add(dataModel1);
        dataModels.add(dataModel2);
        dataModels.add(dataModel3);
        System.out.println(dataModels);
        //act
        s.deleteItems(1);
        //assert
        verify(dataModels).remove(dataModel2);
        System.out.println(dataModels);
    }
}
//public class ScreenTalkerUnitTest {

//}
