package br.com.martins.bakingtime.recipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.martins.bakingtime.R;
import br.com.martins.bakingtime.model.Recipe;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private RecipeAdapterOnClickHandler mRecipeAdapterOnClickHandler;
    private List<Recipe> mRecipes;

    public RecipeAdapter(RecipeAdapterOnClickHandler mRecipeAdapterOnClickHandler){
        this.mRecipeAdapterOnClickHandler = mRecipeAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdItem = R.layout.recipe_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdItem, parent, shouldAttachToParentImmediately);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.mTextViewRecipeName.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if(mRecipes != null){
            return mRecipes.size();
        }
        return 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewRecipeName;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            mTextViewRecipeName = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mRecipeAdapterOnClickHandler.onClick(mRecipes.get(adapterPosition));
        }
    }

    public void setRecipesData(List<Recipe> mRecipesData) {
        mRecipes = mRecipesData;
        notifyDataSetChanged();
    }

    public interface RecipeAdapterOnClickHandler{
        void onClick(Recipe recipe);
    }
}
