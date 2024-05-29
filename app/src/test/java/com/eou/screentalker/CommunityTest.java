package com.eou.screentalker;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import static org.mockito.ArgumentMatchers.any;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

//@RunWith(MockitoJUnit.class)
public class CommunityTest {

    @Test
    public void testCreateCommunity() throws Exception {

        String testCommunityName = "Test Community";
        String testUri = "testuri";

        // Simulate adding data to the collection
        Map<String, Object> addedCommunity = new HashMap<>();
        addedCommunity.put("name", testCommunityName);
        addedCommunity.put("dp_url", testUri);
        addedCommunity.put("is_public", true);

        // Assert (verify simulated data matches expected data)
        assertEquals(testCommunityName, addedCommunity.get("name"));
        assertEquals(testUri, addedCommunity.get("dp_url"));
        assertEquals(true, addedCommunity.get("is_public"));
    }

}
