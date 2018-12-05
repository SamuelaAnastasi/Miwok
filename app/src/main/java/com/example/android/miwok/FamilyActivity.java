package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /* Handles audio focus when playing an audio file*/
    private AudioManager mAudioManager;

    /* The private member var holds a OnCompletionListener Object and its callback onCompletion()
    * that is going to be called by the system once the media file has finished playing*/
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        /* Create and set up the {@link AudioManager} to request audio focus */
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // create an arrayList english words

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "wama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));


        // Create ArrayAdapter object with String data source
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_family);

        // Select ListView of this activity to be populated by ArrayAdapter
        ListView listView = (ListView) findViewById(R.id.list);

        // Attach the selected ListView to the itemAdapter object
        if(listView != null){
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Word word = words.get(position);

                    /* Make sure to release MediaPlayer resources before starting a new audio
                    * the user may start to play another audio before the the completion of
                    * the previous audio is reached so the completion event never occurs.*/
                    releaseMediaPlayer();

                    // Request audio focus for playback
                    int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                            AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                        // We have focus now
                        mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getAudioResourceId());
                        mMediaPlayer.start();
                        /* Set a listener to for the completion event and release resources*/
                        mMediaPlayer.setOnCompletionListener(mCompletionListener);
                    }

                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getAudioResourceId());
                    mMediaPlayer.start();
                    /* Set a listener to for the completion event and release resources*/
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped release MediaPlayer resources because we won't use them anymore
        releaseMediaPlayer();
    }

    // helper method to release MediaPlayer resources on completion event
    private void releaseMediaPlayer() {
        // check if a valid MediaPlayer object exists
        if (mMediaPlayer != null) {
            // then release it
            mMediaPlayer.release();
            // now set MediaPlayer object to null
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
