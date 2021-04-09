package com.example.yummlyteam.app.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yummlyteam.app.api.ApiClient;
import com.example.yummlyteam.app.api.ApiInterface;
import com.example.yummlyteam.app.model.RecipeSearchList;
import com.example.yummlyteam.yummly_project.R;
import com.example.yummlyteam.app.Util;
import com.example.yummlyteam.app.model.Match;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

  private List<Match> recipeList;

  public RecipeListAdapter(List<Match> recipeList) {
    this.recipeList = recipeList;
  }

  @NonNull
  @Override
  public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
    View view = LayoutInflater.from(parent.getContext()).
        inflate(R.layout.recipe_row, parent, false);
    return new RecipeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {

    Match recipe = recipeList.get(i);

    //FUTURE WORK: Use lambda  function to handle null value and simplify these calls
    recipeViewHolder.recipeName.setText(recipe.getRecipeName()!=null ? recipe.getRecipeName() : "--");
    recipeViewHolder.totalTime.setText(Util.timeFormatter(recipe.getTotalTimeInSeconds()));
    recipeViewHolder.totalCalories.setText("--");
    recipeViewHolder.ingredients.setText(recipe.getIngredients()!=null ? String.valueOf(recipe.getIngredients().size()) : "--");

    if(recipe.getSmallImageUrls()!=null && recipe.getSmallImageUrls().size()>=1) {
      RequestCreator pictureRequest = Picasso.with(recipeViewHolder.itemView.getContext())
              .load(recipe.getSmallImageUrls().get(0))
              .error(android.R.drawable.sym_def_app_icon);
      if(!Util.isNetworkConnectionAvailable(recipeViewHolder.itemView.getContext())) {
        pictureRequest = pictureRequest.networkPolicy(NetworkPolicy.OFFLINE);
      }
      pictureRequest.into(recipeViewHolder.recipeImageView);
    } else {
      recipeViewHolder.recipeImageView.setImageResource(android.R.drawable.sym_def_app_icon);
    }

    if (recipe.getFlavors()!= null && recipe.getFlavors().getBitter()!=null && recipe.getFlavors().getBitter().equals(1.0)) {
      recipeViewHolder.recipeBitternessIndicator.setVisibility(View.VISIBLE);
    } else {
      recipeViewHolder.recipeBitternessIndicator.setVisibility(View.GONE);
    }

    ApiInterface apiService =
            ApiClient.getClient(null).create(ApiInterface.class);
    Call<Match> getRecipeCall = apiService.getRecipe(recipe.getId());

    getRecipeCall.enqueue(new Callback<Match>() {
      @Override
      public void onResponse(Call<Match> call, Response<Match> response) {

      }

      @Override
      public void onFailure(Call<Match> call, Throwable t) {

      }
    });
  }

  @Override
  public int getItemCount() {
    return recipeList.size();
  }

  private void addItems(List<Match> newItems) {
      recipeList.addAll(newItems);
  }

  public void updateList(RecipeSearchList searchList, int pageNumber) {
    if (searchList == null || searchList.getMatches() == null) { // clear the list
      clearList();
    } else {
      if(pageNumber==0) {
        clearList();
      }
      addItems(searchList.getMatches());
    }
    notifyDataSetChanged();
  }

  private void clearList() {
    recipeList.clear();
  }


  public static class RecipeViewHolder extends RecyclerView.ViewHolder {

    TextView ingredients, recipeName, totalCalories, totalTime, recipeBitternessIndicator;
    ImageView recipeImageView;

    public RecipeViewHolder(@NonNull View itemView) {
      super(itemView);
      recipeName = itemView.findViewById(R.id.recipeName);
      ingredients = itemView.findViewById(R.id.ingredients);
      totalCalories = itemView.findViewById(R.id.totalCalories);
      totalTime = itemView.findViewById(R.id.totalTime);
      recipeImageView = itemView.findViewById(R.id.recipeImageView);
      recipeBitternessIndicator = itemView.findViewById(R.id.bitter_label);
    }
  }

}
