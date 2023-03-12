package com.eou.screentalker;

import static org.junit.Assert.assertEquals;

import com.eou.screentalker.Adapters.SeriesAdapter;
import com.eou.screentalker.Models.SeriesModel;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SeriesAdapterTest {
    @Test
    public void testGetCount() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        //create list of data models
        List<SeriesModel> seriesModels = new ArrayList<>();
        seriesModels.add(new SeriesModel());
        seriesModels.add(new SeriesModel());
        seriesModels.add(new SeriesModel());
        // Create a mock context for the adapter
//        Context context = mock(Context.class);
        //adapter instance
        SeriesAdapter seriesAdapter   = new SeriesAdapter (seriesModels);

        //call count method
        seriesAdapter.getItemCount();

        // Verify that the correct item was removed from the dataModels list
        assertEquals(3, seriesAdapter.getItemCount());
    }
}
