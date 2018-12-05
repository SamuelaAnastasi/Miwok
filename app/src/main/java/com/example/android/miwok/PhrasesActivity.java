package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

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
        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));


        // Create ArrayAdapter object with String data source
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);

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
                        mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());
                        mMediaPlayer.start();
                        /* Set a listener to for the completion event and release resources*/
                        mMediaPlayer.setOnCompletionListener(mCompletionListener);
                    }

                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());
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
