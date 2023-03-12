package com.eou.screentalker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.eou.screentalker.Adapters.FeaturedAdapter;
import com.eou.screentalker.Models.FeaturedModel;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class FeaturedAdapterTest {
    @Test
    public void testGetCount() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        //create list of data models
        List<FeaturedModel> featuredModels = new ArrayList<>();
        featuredModels.add(new FeaturedModel());
        featuredModels.add(new FeaturedModel());
        featuredModels.add(new FeaturedModel());
        // Create a mock context for the adapter
//        Context context = mock(Context.class);
        //adapter instance
        FeaturedAdapter featuredAdapter   = new FeaturedAdapter (featuredModels);

        //call count method
        featuredAdapter.getItemCount();

        // Verify that the correct item was removed from the dataModels list
        assertEquals(3, featuredAdapter.getItemCount());
    }
}
