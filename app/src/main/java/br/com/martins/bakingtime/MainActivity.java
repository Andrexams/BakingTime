package br.com.martins.bakingtime;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import br.com.martins.bakingtime.model.Recipe;
import br.com.martins.bakingtime.recipe.RecipeAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final int ACTION_DETAILS = 1000;

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

        //TODO Init recycler
        //TODO Init adapter
        //TODO load with loader
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {

    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    @Override
    public void onClick(Recipe recipe) {

    }
}
