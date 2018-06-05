package br.com.martins.bakingtime.step;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.martins.bakingtime.R;
import br.com.martins.bakingtime.data.RecipeRamRepository;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.model.Step;
import br.com.martins.bakingtime.stepdetail.StepDetailActivity;
import br.com.martins.bakingtime.stepdetail.StepDetailFragment;
import butterknife.ButterKnife;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class StepListActivity extends AppCompatActivity implements StepListFragment.StepOnClickHandler, StepDetailFragment.OnErrorListener{

    private static final String TAG = StepListActivity.class.getSimpleName();
    public static final String RECIPE_ID_EXTRA = "RECIPE_ID_EXTRA";

    private Boolean multiPane;
    private Long recipeId;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(RECIPE_ID_EXTRA)) {
            recipeId = intent.getLongExtra(RECIPE_ID_EXTRA, 0);
        }

        multiPane = findViewById(R.id.ll_sw_group) != null;

        if(multiPane){
            if(savedInstanceState == null){
                setFirstStepDetail();
            }
            LinearLayout ll = findViewById(R.id.detail_nav_buttons);
            ll.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(Step step) {
        if (multiPane) {
            setStep(false, step);
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(StepDetailActivity.RECIPE_ID_EXTRA, recipeId);
            intent.putExtra(StepDetailActivity.STEP_ID_EXTRA, step.getId());
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
        try{
            Repository repository = new RecipeRamRepository();
            List<Step> stepList = repository.getStepList(recipeId);
            setStep(true,stepList.get(1));
        }catch (Exception e){
            Log.e(TAG,"Error executing setFirstStepDetail",e);
            onErrorOcurred(e);
        }
    }

    @Override
    public void onErrorOcurred(Exception e) {
        if(multiPane){
            TextView mTextViewErrorMessage = findViewById(R.id.tv_error_message_display_step_detail);
            mTextViewErrorMessage.setText(getString(R.string.error));
            mTextViewErrorMessage.setVisibility(View.VISIBLE);
        }
    }
}
