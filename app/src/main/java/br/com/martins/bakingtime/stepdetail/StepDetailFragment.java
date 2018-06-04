package br.com.martins.bakingtime.stepdetail;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.martins.bakingtime.R;
import br.com.martins.bakingtime.data.RecipeRamRepository;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.model.Step;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class StepDetailFragment extends Fragment {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private OnErrorListener mOnErrorListener;
    public interface OnErrorListener {
        void onErrorOcurred(Exception e);
    }

    public static final String RECIPE_ID_SI = "RECIPE_ID_SI";
    public static final String STEP_ID_SI  = "STEP_ID_SI";

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    @BindView(R.id.tv_step_detail)
    TextView mTextStepDetail;

    private Integer stepId;
    private Long recipeId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this,rootView);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.pv_video_step);

        if(savedInstanceState != null){
            doRestoreInstanceActions(savedInstanceState);
        }

        fill();
        return  rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnErrorListener = (OnErrorListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnErrorListener,");
        }
    }

    private void fill() {
        try{
            Repository repository = new RecipeRamRepository();
            Step step = repository.getStep(recipeId,stepId);

            if(step.getVideoURL() != null && !step.getVideoURL().equals("")){
                Uri uri = Uri.parse(step.getVideoURL());
                initializePlayer(uri);
            }

            mTextStepDetail.setText(step.getDescription());
        }catch (Exception e){
            Log.e(TAG,"Error executing fill",e);
            mOnErrorListener.onErrorOcurred(e);
        }
    }

    private void initializePlayer(Uri mediaUri) {

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try{
            outState.putLong(RECIPE_ID_SI,recipeId);
            outState.putInt(STEP_ID_SI,stepId);
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
            recipeId = savedInstanceState.getLong(RECIPE_ID_SI);
            stepId = savedInstanceState.getInt(STEP_ID_SI);
        }catch (Exception e){
            Log.e(TAG,"Error on doRestoreInstanceActions",e);
        }
    }

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    private void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
