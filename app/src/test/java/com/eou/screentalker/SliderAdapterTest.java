package com.eou.screentalker;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
//import static org.mockito.internal.verification.VerificationModeFactory.times;

import android.content.Context;

import com.eou.screentalker.Adapters.SliderAdapter;
import com.eou.screentalker.Models.DataModel;

public class SliderAdapterTest {

    @Test
    public void testRenewItems() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Create a mock context for the adapter
        Context context = mock(Context.class);
        List<DataModel> dataModels = new ArrayList<>();
        //adapter instance
        SliderAdapter sliderAdapter   = new SliderAdapter(context, dataModels);
        // get reference to the private method
        Method privateMethod = SliderAdapter.class.getDeclaredMethod("setDataModels", List.class);
        privateMethod.setAccessible(true); // make the method accessible
        privateMethod.invoke(sliderAdapter, dataModels); // invoke the method on the adapter object with new data models


        // Create a list of data models to be passed to the renewItems method
        dataModels.add(new DataModel("Title 1", "url 1", "vid 1"));
        dataModels.add(new DataModel("Title 2", "url 2", "vid 2"));
        dataModels.add(new DataModel("Title 3", "url 3", "vid 3"));

//        System.out.println(sliderAdapter.getCount());

        // Verify that the dataModels field was set to the input list
        assertEquals(dataModels, sliderAdapter.getDataModels());
    }

    @Test
    public void testDeleteItems() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        //create list of data models
        List<DataModel> dataModels = new ArrayList<>();
        dataModels.add(new DataModel("Title 1", "url 1", "vid 1"));
        dataModels.add(new DataModel("Title 2", "url 2", "vid 2"));
        dataModels.add(new DataModel("Title 3", "url 3", "vid 3"));
        // Create a mock context for the adapter
        Context context = mock(Context.class);
        //adapter instance
        SliderAdapter sliderAdapter   = new SliderAdapter(context, dataModels);
        //test model of expected outcome
        List<DataModel> expectedDataModels = new ArrayList<>();
        expectedDataModels.add(dataModels.get(0));
        expectedDataModels.add(dataModels.get(2));
//        System.out.println(dataModels);
//        System.out.println(expectedDataModels);

        // Call the private remove() method using reflection
        Method removeMethod = SliderAdapter.class.getDeclaredMethod("remove", int.class);
        removeMethod.setAccessible(true);
        removeMethod.invoke(sliderAdapter, 1);

        // Verify that the correct item was removed from the dataModels list
        assertEquals(expectedDataModels, sliderAdapter.getDataModels());
    }

    @Test
    public void testGetCount() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        //create list of data models
        List<DataModel> dataModels = new ArrayList<>();
        dataModels.add(new DataModel("Title 1", "url 1", "vid 1"));
        dataModels.add(new DataModel("Title 2", "url 2", "vid 2"));
        dataModels.add(new DataModel("Title 3", "url 3", "vid 3"));
        // Create a mock context for the adapter
        Context context = mock(Context.class);
        //adapter instance
        SliderAdapter sliderAdapter   = new SliderAdapter(context, dataModels);

        //call count method
        sliderAdapter.getCount();

        // Verify that the correct item was removed from the dataModels list
        assertEquals(3, sliderAdapter.getCount());
    }
}
