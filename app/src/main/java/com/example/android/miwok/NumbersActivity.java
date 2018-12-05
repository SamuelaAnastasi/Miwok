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

public class NumbersActivity extends AppCompatActivity {
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
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two,R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));


        // Create ArrayAdapter object with String data source
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

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
                        mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getAudioResourceId());
                        mMediaPlayer.start();
                        /* Set a listener to for the completion event and release resources*/
                        mMediaPlayer.setOnCompletionListener(mCompletionListener);
                    }
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
