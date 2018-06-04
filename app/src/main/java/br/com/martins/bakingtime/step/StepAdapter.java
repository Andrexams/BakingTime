package br.com.martins.bakingtime.step;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.martins.bakingtime.R;
import br.com.martins.bakingtime.model.Step;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private StepAdapterOnClickHandler mStepAdapterOnClickHandler;
    private List<Step> mSteps;
    private String mIngredients;

    private final static int RECIPE_ITEM_INGREDIENTS_VIEW = 0;
    private final static int RECIPE_ITEM_VIEW = 1;

    public StepAdapter(StepAdapterOnClickHandler mRecipeAdapterOnClickHandler){
        this.mStepAdapterOnClickHandler = mRecipeAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdItem = 0;
        switch (viewType){
            case RECIPE_ITEM_INGREDIENTS_VIEW:
                layoutIdItem = R.layout.step_item_ingredients;
                break;
            case RECIPE_ITEM_VIEW:
                layoutIdItem = R.layout.step_item;
                break;
        }
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdItem, parent, shouldAttachToParentImmediately);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = mSteps.get(position);
        if(position == 0){
            holder.mTextViewIngredients.setText(mIngredients);
        }
        holder.mTextViewStep.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(mSteps != null){
            return mSteps.size();
        }
        return 0;
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewStep;
        private TextView mTextViewIngredients;
        public StepViewHolder(View itemView) {
            super(itemView);
            mTextViewStep = (TextView) itemView.findViewById(R.id.tv_step);
            mTextViewIngredients = (TextView) itemView.findViewById(R.id.tv_ingredients);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if(adapterPosition > 0){
                mStepAdapterOnClickHandler.onClick(mSteps.get(adapterPosition));
            }
        }
    }

    public void setStepData(List<Step> mStepData) {
        mSteps = mStepData;
        notifyDataSetChanged();
    }

    public void setIngredientsData(String mIngredientData) {
        mIngredients = mIngredientData;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return RECIPE_ITEM_INGREDIENTS_VIEW;
        }else{
            return RECIPE_ITEM_VIEW;
        }
    }

    public interface StepAdapterOnClickHandler {
        void onClick(Step step);
    }
}
