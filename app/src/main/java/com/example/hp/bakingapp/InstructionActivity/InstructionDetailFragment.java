package com.example.hp.bakingapp.InstructionActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.bakingapp.R;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;
import com.example.hp.bakingapp.Utilities.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * A fragment representing a single Instruction detail screen.
 * This fragment is either contained in a {@link InstructionListActivity}
 * in two-pane mode (on tablets) or a {@link InstructionDetailActivity}
 * on handsets.
 */
public class InstructionDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private static final String RECIPE_ITEM_ID = "recipe_ID";
    private static final String STEP_ID = "step_id";


    public static Fragment newInstance(int recipeId, int stepId){
        Bundle arguments = new Bundle();
        arguments.putInt(InstructionDetailFragment.RECIPE_ITEM_ID, recipeId);
        arguments.putInt(InstructionDetailFragment.STEP_ID, stepId);

        Fragment fragment = new InstructionDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    /**
     * The dummy content this fragment is presenting.
     */
    private Step mItem;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView playerView;
    private RecipeDatabase db;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstructionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(RECIPE_ITEM_ID) &&
                getArguments().containsKey(STEP_ID)) {
            int recipeId = getArguments().getInt(RECIPE_ITEM_ID);
            int stepId = getArguments().getInt(STEP_ID);

            db = new RecipeDatabase(getContext());
            if (recipeId != -1 && stepId != -1){
                mItem = db.getStepByRecipeId(recipeId, stepId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instruction_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.instruction_detail)).setText(mItem.details);
            playerView = rootView.findViewById(R.id.video_view);
            TextView description = rootView.findViewById(R.id.instruction_detail);
            description.setText(mItem.getDescription());
        }
        initializePlayer();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        db.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer() {
        if (mExoPlayer == null){
            Log.d("exo debug ", "where is my exo?");
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            playerView.setPlayer(mExoPlayer);

            mExoPlayer.setPlayWhenReady(false);

            String thumbnailUrl = mItem.getThumbnailURL();

            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                                 R.drawable.exo_controls_play);

            final Bitmap[] thumbnailBitMap = {icon};

            if (thumbnailUrl != null && !thumbnailUrl.equals("")){
                Target thumbnailTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        thumbnailBitMap[0] = bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                } ;
                Picasso.with(getContext()).load(thumbnailUrl)
                        .placeholder(R.drawable.exo_controls_play)
                        .error(R.drawable.exo_controls_play)
                        .into(thumbnailTarget);
            }

            playerView.setDefaultArtwork(thumbnailBitMap[0]);


            String videoUrl = mItem.getVideoURL();
            Log.d("exo debug url", videoUrl);
            Uri uri = Uri.parse(videoUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource, true, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
