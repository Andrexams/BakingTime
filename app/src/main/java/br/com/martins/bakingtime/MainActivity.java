package br.com.martins.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.model.Recipe;
import br.com.martins.bakingtime.recipe.RecipeAdapter;
import br.com.martins.bakingtime.data.RecipeRamRepository;
import br.com.martins.bakingtime.step.StepListActivity;
import br.com.martins.bakingtime.utils.NetworkUtils;
import br.com.martins.bakingtime.utils.RecipeApiUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.support.v4.content.Loader;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";

    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerViewRecipe;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBarLoading;

    @BindView(R.id.tv_error_message_display)
    TextView mTextViewErrorMessage;

    @BindView(R.id.fl_main)
    FrameLayout mFrameLayoutActivity;

    private RecipeAdapter mRecipeAdapter;

    private static final String RV_RECIPE_LAYOUT_STATE = "RV_RECIPE_LAYOUT_STATE";
    private Parcelable savedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,getResources().getInteger(R.integer.rw_recipes_columns));
        mRecyclerViewRecipe.setLayoutManager(gridLayoutManager);
        mRecyclerViewRecipe.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerViewRecipe.setAdapter(mRecipeAdapter);
        mRecyclerViewRecipe.setSaveEnabled(true);

        if(savedInstanceState != null)
        {
            doRestoreInstanceActions(savedInstanceState);
        }

        fill();
    }

    private void fill(){
        if(NetworkUtils.isConnectOnNetwork(this)){
            getSupportLoaderManager().initLoader(1000, null, MainActivity.this);
        }else{
            showErrorMessage(getString(R.string.no_internet_message));
            showSnackRetry(this.mFrameLayoutActivity);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try{
            outState.putParcelable(RV_RECIPE_LAYOUT_STATE,
                    mRecyclerViewRecipe.getLayoutManager().onSaveInstanceState());

        }catch (Exception e){
            Log.e(TAG,"Error on onSaveInstanceState",e);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
        {
            doRestoreInstanceActions(savedInstanceState);
        }
    }

    private void doRestoreInstanceActions(Bundle savedInstanceState) {
        try{
            savedRecyclerLayoutState = savedInstanceState
                    .getParcelable(RV_RECIPE_LAYOUT_STATE);
        }catch (Exception e){
            Log.e(TAG,"Error on doRestoreInstanceActions",e);
        }
    }

    private void showRecipeDataView() {
        mTextViewErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerViewRecipe.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMessage) {
        mRecyclerViewRecipe.setVisibility(View.INVISIBLE);
        mTextViewErrorMessage.setText(errorMessage);
        mTextViewErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Recipe recipe) {
        try{
            Intent intent = new Intent(this,StepListActivity.class);
            intent.putExtra(StepListActivity.RECIPE_ID_EXTRA,recipe.getId());
            startActivity(intent);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingTimeWidget.class));
            BakingTimeWidget.updateIngredientsWidget(this, appWidgetManager, appWidgetIds,recipe.getId());
        }catch (Exception e){
            Log.e(TAG,"Error executing onClick",e);
            showErrorMessage(getString(R.string.error));
        }
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(this) {

            List<Recipe> mRecipes;

            @Override
            protected void onStartLoading() {
                if (mRecipes != null) {
                    deliverResult(mRecipes);
                } else {
                    mProgressBarLoading.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public List<Recipe> loadInBackground() {
                try{
                    List<Recipe> list = RecipeApiUtil.getRecipeList();
                    Repository recipeRamRepository = new RecipeRamRepository();
                    recipeRamRepository.persistList(list);
                    return list;
                }catch (Exception e){
                    Log.e(TAG,"Erro searching recipes.",e);
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable List<Recipe> data) {
                mRecipes = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<Recipe>> loader, List<Recipe> data) {
        mProgressBarLoading.setVisibility(View.INVISIBLE);
        mRecipeAdapter.setRecipesData(data);
        if (savedRecyclerLayoutState != null) {
            mRecyclerViewRecipe.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        if (null == data) {
            showErrorMessage(getString(R.string.no_results));
        } else {
            showRecipeDataView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<Recipe>> loader) { }


    private void showSnackRetry(View parent){
        Snackbar snackbar = Snackbar
                .make(parent, "", Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fill();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
