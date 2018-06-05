package br.com.martins.bakingtime.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import br.com.martins.bakingtime.model.Step;
import br.com.martins.bakingtime.stepdetail.StepDetailActivity;
import br.com.martins.bakingtime.stepdetail.StepDetailFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class StepListFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler{

    private static final String TAG = StepListFragment.class.getSimpleName();
    public static final String RECIPE_ID_EXTRA = "RECIPE_ID_EXTRA";

    @BindView(R.id.rv_step_list)
    RecyclerView mRecyclerViewSteps;

    @BindView(R.id.tv_error_message_display_steps)
    TextView mTextViewErrorMessage;

    private StepOnClickHandler mStepOnClickHandler;
    public interface StepOnClickHandler {
        void onClick(Step step);
    }

    private StepAdapter mStepAdapter;

    private static final String RV_STEP_LIST_LAYOUT_STATE = "RV_STEP_LIST_LAYOUT_STATE";
    private Parcelable savedRecyclerLayoutState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this,rootView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 1);
        mRecyclerViewSteps.setLayoutManager(gridLayoutManager);
        mRecyclerViewSteps.setHasFixedSize(true);

        mStepAdapter = new StepAdapter(this);
        mRecyclerViewSteps.setAdapter(mStepAdapter);
        mRecyclerViewSteps.setSaveEnabled(true);

        if(savedInstanceState != null)
        {
            doRestoreInstanceActions(savedInstanceState);
        }

        Intent intent = this.getActivity().getIntent();
        if(intent.hasExtra(RECIPE_ID_EXTRA)){
            Long recipeId = intent.getLongExtra(RECIPE_ID_EXTRA,0);
            fill(recipeId);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mStepOnClickHandler = (StepOnClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StepOnClickHandler,");
        }
    }

    private void fill(Long recipeId) {
        try{
            Repository repository = new RecipeRamRepository();
            List<Step> stepList = repository.getStepList(recipeId);

            //Aditional step to store ingredient list
            Step stepIngredients = new Step(-1);
            stepIngredients.setShortDescription(getString(R.string.recipe_ingredients));
            if(!stepList.contains(stepIngredients)){
                stepList.add(0,stepIngredients);
            }

            mStepAdapter.setIngredientsData(repository.getIngredientTextList(recipeId));
            mStepAdapter.setStepData(stepList);
            if (savedRecyclerLayoutState != null) {
                mRecyclerViewSteps.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }catch (Exception e){
            Log.e(TAG,"Error executing fill",e);
            showErrorMessage(getString(R.string.error));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try{
            outState.putParcelable(RV_STEP_LIST_LAYOUT_STATE,
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
                    .getParcelable(RV_STEP_LIST_LAYOUT_STATE);
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
        try {
            //delegate to activity
            mStepOnClickHandler.onClick(step);
        } catch (Exception e) {
            Log.e(TAG, "Error executing onClick", e);
            showErrorMessage(getString(R.string.error));
        }
    }

}
