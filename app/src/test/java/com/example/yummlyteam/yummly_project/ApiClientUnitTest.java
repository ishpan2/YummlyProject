package com.example.yummlyteam.yummly_project;

import com.example.yummlyteam.app.api.ApiClient;

import org.junit.Assert;
import org.junit.Test;

public class ApiClientUnitTest {
    @Test
    public void testGetClient() {
        Assert.assertEquals("https://api.yummly.com/v1/api/", ApiClient.getClient(null).baseUrl().toString());
    }
}
