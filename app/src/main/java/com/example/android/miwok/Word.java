package com.example.android.miwok;

/**
 * Created by the_insider on 18/02/2017.
 * Custom class to hold both languages translation
 * and to return a value
 */
public class Word {
    private String mMiwokTranslation;
    private String mDefaultTranslation;
    private int mAudioResourceId;
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED = -1;

    // Class constructor 1 - Takes 2 Strings arguments
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceId) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mAudioResourceId = audioResourceId;
    }

    // Class constructor 2 - Takes 2 Strings  and 1 int arguments
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId,
                int audioResourceId) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    // Public methods
    public String getMiwokTranslation(){return mMiwokTranslation;}

    public String getDefaultTranslation() {return mDefaultTranslation;}

    public int getImageResourceId () {return mImageResourceId;}

    public int getAudioResourceId() {return mAudioResourceId;}

    // The method checks if the word object is provided with an image
    public boolean hasImage() {return mImageResourceId != NO_IMAGE_PROVIDED;}
}
