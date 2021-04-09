package com.example.yummlyteam.app.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.yummlyteam.app.api.ApiClient;
import com.example.yummlyteam.app.api.ApiInterface;
import com.example.yummlyteam.app.model.RecipeSearchList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeViewModel extends ViewModel {
    private static final String ACCESS_KEY = "955296fc681db8f8b82e0698d02a50c0";
    private static final String APP_ID = "62407325";
    private static final Integer ITEM_PER_PAGE = 18;

    private MutableLiveData<RecipeSearchList> searchList = new MutableLiveData<>();
    private MutableLiveData<Integer> currentSearchPage = new MutableLiveData<>();
    private MutableLiveData<String> query = new MutableLiveData<>();

    public LiveData<RecipeSearchList> getSearchList() {
        return searchList;
    }

    public void clearSearchList() {
        RecipeSearchList recipeSearchList = new RecipeSearchList();
        searchList.setValue(recipeSearchList);
        setSearchQuery("");
    }

    public void setSearchQuery(String q) {
        query.setValue(q);
    }

    public LiveData<String> getSearchQuery() {
        return query;
    }

    public void fetchRecipeSearchList() {
        if (query.getValue() == null || query.getValue().isEmpty()) {
            return;
        }

        //FUTURE WORK: Make into singleton to avoid repeated recreation of this object
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RecipeSearchList> call = apiService.getRecipeList(APP_ID, ACCESS_KEY, query.getValue(), ITEM_PER_PAGE
                , getCurrentSearchPage());

        call.enqueue(new Callback<RecipeSearchList>() {
            @Override
            public void onResponse(Call<RecipeSearchList> call, Response<RecipeSearchList> response) {
                int statusCode = response.code();
                if(statusCode<400) {
                    searchList.setValue(response.body());
                } else {
                    Log.d(getClass().getSimpleName(), response.message());
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchList> call, Throwable t) {
                Log.d(getClass().getSimpleName(), t.getMessage());
            }
        });
    }

    private void setCurrentSearchPage(int page) {
        currentSearchPage.setValue(page);
    }

    public int getCurrentSearchPage() {
        Integer curPage = currentSearchPage.getValue();
        return curPage == null ? 0 : curPage;
    }

    public void nextSearchPage() {
        Integer curPage = currentSearchPage.getValue();
        int newPageNumber = curPage == null ? 0 : curPage + ITEM_PER_PAGE;
        setCurrentSearchPage(newPageNumber);
        fetchRecipeSearchList();
    }

    public void resetSearchPage() {
        setCurrentSearchPage(0);
    }
}
