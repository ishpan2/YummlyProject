package com.example.yummlyteam.yummly_project;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.executor.TaskExecutor;

import com.example.yummlyteam.app.Util;
import com.example.yummlyteam.app.model.Criteria;
import com.example.yummlyteam.app.model.Match;
import com.example.yummlyteam.app.model.RecipeSearchList;
import com.example.yummlyteam.app.search.RecipeViewModel;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeViewModelUnitTest {
  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Test
  public void testFetchRecipeSearchList() {
    Util.areMocksEnabled = true;
    RecipeViewModel model = new RecipeViewModel();
    //LiveData by default runs on a separate thread, therefore use InstantTaskExecutor for this test to avoid threading error
    instantTaskExecutorRule.enableRule();
    model.setSearchQuery("hui");
    Assert.assertEquals(model.getSearchQuery().getValue(), "hui");
    model.fetchRecipeSearchList();
    //Async process that will execute nearly instantly due to mock flag being turned on.
    //However, we must still wait for response, therefore a rare positive use case here of Thread.sleep in a Unit test
    //FUTURE WORK: Either break down function to test each piece and avoid the async call, or handle multithreading  in test more cleanly
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    RecipeSearchList actualList = model.getSearchList().getValue();
    RecipeSearchList expectedList = getMockRecipeSearchList();
    mockSearchListEquals(actualList, expectedList);
    model.clearSearchList();
    Assert.assertEquals(model.getSearchList().getValue().getMatches(), new RecipeSearchList().getMatches());
  }

  @Test
  public void testCurrentSearchPage() {
    instantTaskExecutorRule.enableRule();
    RecipeViewModel model = new RecipeViewModel();
    Assert.assertEquals(0, model.getCurrentSearchPage());
    model.nextSearchPage();
    Assert.assertEquals(18, model.getCurrentSearchPage());
    model.resetSearchPage();
    Assert.assertEquals(0, model.getCurrentSearchPage());
  }

  private RecipeSearchList getMockRecipeSearchList() {
    RecipeSearchList mockList = new RecipeSearchList();
    mockList.setTotalMatchCount(666);
    mockList.setCriteria(new Criteria());

    Match firstMatch = new Match();
    firstMatch.setId("Twice-Cooked-Pork-_Hui-Guo-Rou_-571942");
    firstMatch.setSourceDisplayName("No Recipes");
    firstMatch.setRecipeName("Twice Cooked Pork (Huí Guō Ròu)");
    List<String> ingredients = Arrays.asList("pork belly", "bean paste", "Shaoxing wine", "soy sauce", "sugar", "vegetable oil",
            "ginger", "chili bean paste", "garlic scapes", "scallions");
    firstMatch.setIngredients(ingredients);
    List<Match> matches = new ArrayList<>();
    matches.add(firstMatch);

    mockList.setMatches(matches);
    return mockList;
  }
  private void mockSearchListEquals(RecipeSearchList actualList, RecipeSearchList expectedList) {
    Match actualMatch0 = actualList.getMatches().get(0);
    Match expectedMatch0 = actualList.getMatches().get(0);
    Assert.assertEquals(expectedList.getTotalMatchCount(), actualList.getTotalMatchCount());
    Assert.assertEquals(expectedList.getCriteria().getAllowedAttributes(), actualList.getCriteria().getAllowedAttributes());
    Assert.assertEquals(expectedMatch0.getId(), actualMatch0.getId());
    Assert.assertEquals(expectedMatch0.getRecipeName(), actualMatch0.getRecipeName());
    Assert.assertEquals(expectedMatch0.getIngredients(), actualMatch0.getIngredients());
  }
  private class InstantTaskExecutorRule extends TestWatcher {
    public void enableRule()  {
      ArchTaskExecutor.getInstance().setDelegate(new TaskExecutor() {
        @Override
        public void executeOnDiskIO(Runnable runnable) {
          runnable.run();
        }

        @Override
        public void postToMainThread(Runnable runnable) {
          runnable.run();
        }

        @Override
        public boolean isMainThread() {
          return true;
        }
      });
    }

    public void disableRule() {
      ArchTaskExecutor.getInstance().setDelegate(null);
    }

    @Override
    protected void finished(Description description) {
      super.finished(description);
      disableRule();
    }
  }
}