package com.example.yummlyteam.yummly_project;

import com.example.yummlyteam.app.Util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilUnitTest {
  private final String mockSearchResponse_HuiThirdLine = "    \"q\":\"hui\",";
  @Test
  public void testTimeFormatterNull() {
    Assert.assertEquals("--", Util.timeFormatter(null));
  }
  @Test
  public void testTimeFormatter0() {
    Assert.assertEquals("0m", Util.timeFormatter(0));
  }
  @Test
  public void testTimeFormatterMinute() {
    Assert.assertEquals("1m", Util.timeFormatter(59));
  }
  @Test
  public void testTimeFormatterMinutes() {
    Assert.assertEquals("16m", Util.timeFormatter(982));
  }
  @Test
  public void testTimeFormatterHour() {
    Assert.assertEquals("1h", Util.timeFormatter(3600));
  }
  @Test
  public void testTimeFormatterHoursLarge() {
    Assert.assertEquals("25006h", Util.timeFormatter(90020190));
  }
  @Test
  public void testGetJsonFromAssets() {
    String[] jsonLines = Util.getJsonFromAssets("SearchResponse_Hui_Page0.json").split("\n");
    Assert.assertEquals(mockSearchResponse_HuiThirdLine, jsonLines[2]);
  }
}