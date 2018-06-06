package br.com.martins.bakingtime.stepdetail;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.martins.bakingtime.R;
import br.com.martins.bakingtime.data.RecipeRamRepository;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.model.Step;
import br.com.martins.bakingtime.utils.MediaUtils;
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
    public static final String PLAYER_POSITION  = "PLAYER_POSITION";
    public static final String PLAY_WHEN_READY  = "PLAY_WHEN_READY";

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    @BindView(R.id.tv_step_detail)
    TextView mTextStepDetail;

    @BindView(R.id.iv_step)
    ImageView mImageViewStep;

    private Integer stepId;
    private Long recipeId;

    private Long playerCurrentPosition;
    private Boolean playWhenReady = Boolean.FALSE;

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

            if(MediaUtils.isVideo(step.getVideoURL())){
                initializePlayer(step.getVideoURL());

            }else if (MediaUtils.isVideo(step.getThumbnailURL())){
                initializePlayer(step.getThumbnailURL());

            }else if (MediaUtils.isImage(step.getVideoURL())){
               loadImage(step.getVideoURL());

            }else if (MediaUtils.isImage(step.getThumbnailURL())){
                loadImage(step.getThumbnailURL());
            }

            mTextStepDetail.setText(step.getDescription());
        }catch (Exception e){
            Log.e(TAG,"Error executing fill",e);
            mOnErrorListener.onErrorOcurred(e);
        }
    }

    private void loadImage(String imgUrl){
        mImageViewStep.setVisibility(View.VISIBLE);
        Picasso.with(mImageViewStep.getContext())
                .load(imgUrl)
                .into(mImageViewStep);
    }


    private void initializePlayer(String videoUrl) {

        mPlayerView.setVisibility(View.VISIBLE);

        Uri mediaUri = Uri.parse(videoUrl);
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);

        boolean restoreState  = playerCurrentPosition != null && playerCurrentPosition.intValue() > 0;
        if(restoreState){
            mExoPlayer.seekTo(playerCurrentPosition);
        }
        mExoPlayer.setPlayWhenReady(playWhenReady);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try{
            outState.putLong(RECIPE_ID_SI,recipeId);
            outState.putInt(STEP_ID_SI,stepId);

            if(mExoPlayer != null){
                outState.putLong(PLAYER_POSITION,mExoPlayer.getCurrentPosition());
                outState.putBoolean(PLAY_WHEN_READY,playWhenReady);
            }

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

            if(savedInstanceState.get(PLAYER_POSITION) != null){
                playerCurrentPosition = savedInstanceState.getLong(PLAYER_POSITION);
                playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            }
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

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null) {
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.setPlayWhenReady(false);
        }
    }

}
