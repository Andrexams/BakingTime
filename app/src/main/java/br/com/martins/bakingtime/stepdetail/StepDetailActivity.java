package br.com.martins.bakingtime.stepdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.martins.bakingtime.R;
import br.com.martins.bakingtime.data.RecipeRamRepository;
import br.com.martins.bakingtime.data.Repository;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnErrorListener {

    private static final String TAG = StepDetailActivity.class.getSimpleName();

    public static final String RECIPE_ID_EXTRA = "RECIPE_ID_EXTRA";
    public static final String STEP_ID_EXTRA = "STEP_ID_EXTRA";

    @BindView(R.id.tv_error_message_display_step_detail)
    TextView mTextViewErrorMessage;

    @BindView(R.id.ll_step_detail)
    LinearLayout mLinearLayoutStepDetail;

    @BindView(R.id.bt_next)
    Button mButtonNext;

    @BindView(R.id.bt_prev)
    Button mButtonPrev;

    private Integer stepId;
    private Long recipeId;
    private Integer stepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        ButterKnife.bind(this);

        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(view.getTag().toString());
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(view.getTag().toString());
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra(RECIPE_ID_EXTRA)){
            recipeId = intent.getLongExtra(RECIPE_ID_EXTRA,0);
            stepId = intent.getIntExtra(STEP_ID_EXTRA,0);
        }

        if(savedInstanceState != null){
           doRestoreInstanceActions(savedInstanceState);
        }

        init(savedInstanceState == null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try{
            outState.putLong(RECIPE_ID_EXTRA,recipeId);
            outState.putInt(STEP_ID_EXTRA,stepId);
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
            recipeId = savedInstanceState.getLong(RECIPE_ID_EXTRA);
            stepId = savedInstanceState.getInt(STEP_ID_EXTRA);
        }catch (Exception e){
            Log.e(TAG,"Error on doRestoreInstanceActions",e);
        }
    }

    private void init(boolean createFragments){
        try{
            Repository repository = new RecipeRamRepository();
            stepCount = repository.getStepCount(recipeId);
            configNavButtons();
            showStepDataView();
            if(createFragments)
                setStep(true);
        }catch (Exception e){
            Log.e(TAG,"Error executing navigation",e);
            showErrorMessage(getString(R.string.error));
        }
    }

    private void configNavButtons() {
        mButtonNext.setEnabled(stepId.intValue() != (stepCount.intValue() - 2));
        mButtonPrev.setEnabled(stepId.intValue() != 0);
    }

    public void setStep(boolean add){

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setRecipeId(recipeId);
        stepDetailFragment.setStepId(stepId);

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

    private void navigate(String operation) {
        try{
            if(operation.equals("+")){
                stepId++;
            }else{
                stepId--;
            }
            configNavButtons();
            setStep(false);
        }catch (Exception e){
            Log.e(TAG,"Error executing navigation",e);
            showErrorMessage(getString(R.string.error));
        }
    }

    private void showStepDataView() {
        mTextViewErrorMessage.setVisibility(View.GONE);
        mButtonPrev.setVisibility(View.VISIBLE);
        mButtonNext.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMessage) {
        mButtonPrev.setVisibility(View.INVISIBLE);
        mButtonNext.setVisibility(View.INVISIBLE);
        mTextViewErrorMessage.setText(errorMessage);
        mTextViewErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorOcurred(Exception e){
        showErrorMessage(getString(R.string.error));
    }

}
