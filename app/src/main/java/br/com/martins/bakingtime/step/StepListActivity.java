package br.com.martins.bakingtime.step;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import br.com.martins.bakingtime.R;
import br.com.martins.bakingtime.data.RecipeRamRepository;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.model.Ingredient;
import br.com.martins.bakingtime.model.Step;
import br.com.martins.bakingtime.stepdetail.StepDetailActivity;
import br.com.martins.bakingtime.stepdetail.StepDetailFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class StepListActivity extends AppCompatActivity implements StepAdapter.StepAdapterOnClickHandler,
        StepDetailFragment.OnErrorListener{

    private static final String TAG = StepListActivity.class.getSimpleName();
    public static final String RECIPE_ID_EXTRA = "RECIPE_ID_EXTRA";

    @BindView(R.id.rv_step_list)
    RecyclerView mRecyclerViewSteps;

    @BindView(R.id.pb_loading_indicator_steps)
    ProgressBar mProgressBarLoading;

    @BindView(R.id.tv_error_message_display_steps)
    TextView mTextViewErrorMessage;

    @BindView(R.id.fl_main_detail)
    FrameLayout mFrameLayoutActivity;

    private Boolean multiPane;

    private StepAdapter mStepAdapter;

    private static Long recipeId;

    private static final String RV_STEP_LIST_LAYOUT_STATE = "RV_STEP_LIST_LAYOUT_STATE";
    private Parcelable savedRecyclerLayoutState;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerViewSteps.setLayoutManager(gridLayoutManager);
        mRecyclerViewSteps.setHasFixedSize(true);

        mStepAdapter = new StepAdapter(this);
        mRecyclerViewSteps.setAdapter(mStepAdapter);
        mRecyclerViewSteps.setSaveEnabled(true);

        if(savedInstanceState != null)
        {
            doRestoreInstanceActions(savedInstanceState);
        }

        Intent intent = getIntent();
        if(intent.hasExtra(RECIPE_ID_EXTRA)){
            recipeId = intent.getLongExtra(RECIPE_ID_EXTRA,0);
            fill();
        }

        multiPane = findViewById(R.id.ll_sw_group) != null;

        if(multiPane && savedInstanceState == null){
            setFirstStepDetail();
        }
    }

    private void fill() {
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
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
        if(multiPane){
            setStep(false,step);
        }else{
            Intent intent = new Intent(this,StepDetailActivity.class);
            intent.putExtra(StepDetailActivity.RECIPE_ID_EXTRA,recipeId);
            intent.putExtra(StepDetailActivity.STEP_ID_EXTRA,step.getId());
            startActivity(intent);
        }
    }

    public void setStep(boolean add,Step step){

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setRecipeId(recipeId);
        stepDetailFragment.setStepId(step.getId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(add){
            fragmentManager.beginTransaction()
                    .add(R.id.fl_container, stepDetailFragment)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_container, stepDetailFragment)
                    .commit();
        }
    }

    private void setFirstStepDetail() {
        Repository repository = new RecipeRamRepository();
        List<Step> stepList = repository.getStepList(recipeId);
        setStep(true,stepList.get(1));
    }

    @Override
    public void onErrorOcurred(Exception e) {

    }

}
