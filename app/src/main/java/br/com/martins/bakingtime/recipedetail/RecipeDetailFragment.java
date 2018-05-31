package br.com.martins.bakingtime.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import br.com.martins.bakingtime.R;
import br.com.martins.bakingtime.data.RecipeRamRepository;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.model.Ingredient;
import br.com.martins.bakingtime.model.Step;
import br.com.martins.bakingtime.step.StepDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class RecipeDetailFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private static final int ACTION_SETEP_DETAILS = 2000;

    @BindView(R.id.rv_recipes_detail)
    RecyclerView mRecyclerViewSteps;

    @BindView(R.id.pb_loading_indicator_detail)
    ProgressBar mProgressBarLoading;

    @BindView(R.id.tv_error_message_display_detail)
    TextView mTextViewErrorMessage;

    @BindView(R.id.fl_main_detail)
    FrameLayout mFrameLayoutActivity;

    private StepAdapter mStepAdapter;

    private static Long recipeId;

    private static final String RV_RECIPE_DETAIL_LAYOUT_STATE = "RV_RECIPE_LAYOUT_STATE";
    private Parcelable savedRecyclerLayoutState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this,rootView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1);
        mRecyclerViewSteps.setLayoutManager(gridLayoutManager);
        mRecyclerViewSteps.setHasFixedSize(true);

        mStepAdapter = new StepAdapter(this);
        mRecyclerViewSteps.setAdapter(mStepAdapter);
        mRecyclerViewSteps.setSaveEnabled(true);

        if(savedInstanceState != null)
        {
            doRestoreInstanceActions(savedInstanceState);
        }

        Intent intent = getActivity().getIntent();
        if(intent.hasExtra(RecipeDetailActivity.RECIPE_ID_EXTRA)){
            recipeId = intent.getLongExtra(RecipeDetailActivity.RECIPE_ID_EXTRA,0);
            fillAdapter();
        }

        return  rootView;
    }

    private void fillAdapter() {
        Repository repository = new RecipeRamRepository();
        List<Step> stepList = repository.getStepList(recipeId);
        List<Ingredient> ingredientList = repository.getIngredientList(recipeId);
        mStepAdapter.setIngredientsData(ingredientList);
        mStepAdapter.setStepData(stepList);
        if (savedRecyclerLayoutState != null) {
            mRecyclerViewSteps.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try{
            outState.putParcelable(RV_RECIPE_DETAIL_LAYOUT_STATE,
                    mRecyclerViewSteps.getLayoutManager().onSaveInstanceState());

        }catch (Exception e){
            Log.e(TAG,"Error on onSaveInstanceState",e);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            doRestoreInstanceActions(savedInstanceState);
        }
    }

    private void doRestoreInstanceActions(Bundle savedInstanceState) {
        try{
            savedRecyclerLayoutState = savedInstanceState
                    .getParcelable(RV_RECIPE_DETAIL_LAYOUT_STATE);
        }catch (Exception e){
            Log.e(TAG,"Error on doRestoreInstanceActions",e);
        }
    }

    private void showRecipeDataView() {
        mTextViewErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerViewSteps.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMessage) {
        mRecyclerViewSteps.setVisibility(View.INVISIBLE);
        mTextViewErrorMessage.setText(errorMessage);
        mTextViewErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Step step) {
        Intent intent = new Intent(this.getActivity(),StepDetailActivity.class);
        intent.putExtra(StepDetailActivity.RECIPE_ID_EXTRA,recipeId);
        intent.putExtra(StepDetailActivity.STEP_ID_EXTRA,step.getId());
        startActivity(intent);
    }
}
