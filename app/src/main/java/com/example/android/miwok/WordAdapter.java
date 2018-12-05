package com.example.android.miwok;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by the_insider on 18/02/2017.
 */
public class WordAdapter extends ArrayAdapter<Word> {

    //Resource Id for he background color for this list of words
    private int mColorResourceId;

    // Define Constructor for the WordAdapter sub-class.
    // it takes 2 arguments the context and the data source
    public WordAdapter(Activity context, ArrayList<Word> words, int colorResourceId) {

        /** Call the super class constructor to initialize the internal storage of the adapter
         * with the context values and the data source.
         * Here anyhow the WordAdapter is going to use a custom list-item layout made of two TextViews
         * inside a LinearLayout so the second argument passed to the (super) constructor
         * should be set to 0. The custom list-item layout will be created and returned
         * by the getView() method below which overrides the super-class getView() method.
         */
        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if a ListView exists in Scrap views queue (i.e is passed as argument) if not inflated (i.e created)
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //get the current word object from the adapter's data source
        Word currentWord = getItem(position);

        /** Each list item of the ListView (to which we attached this WordAdapter in NumbersActivity.java)
         * is made up of a LinearLayout object with 2 child TextViews
         * (1 for miwok word, one fer default word).
         * Here we set the text attribute of each of these 2 children first by selecting them
         * and then by setting the text to the corresponding value of currentWord object.
         * The currentWord object is one of the Word objects (the current one) of the data source
         * (words object) we pass the to WordAdapter constructor as (second) argument in
         * NumberActivity.java
         * */

        // Select miwok TextView by querying its parent view (i.e. listItemView)
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        // Set the text attribute of miwokTextView by calling getMiwokTranslation() method of currentWord object.
        miwokTextView.setText(currentWord.getMiwokTranslation());

        // Select default TextView by querying its parent view (i.e. listItemView)
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        // Set the text attribute of miwokTextView by calling getMiwokTranslation() method of currentWord object.
        defaultTextView.setText(currentWord.getDefaultTranslation());

        // Select default TextView by querying its parent view (i.e. listItemView)
       ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        // Check if currentWord has image provided
        if(currentWord.hasImage()) {
            // Set the src attribute of ImageView by calling getImageResourceId() method of currentWord object.
            imageView.setImageResource(currentWord.getImageResourceId());
            // Make sure that the ImageView is visible because we might be using a scrap view where ImageView was set to hidden.
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Set Image View to View.GONE (vilibility is static member so it belongs to the class not insctances
            // With View.GONE the view will be hidden and will no take space on the screen
            imageView.setVisibility(View.GONE);
        }

        // Set the theme color for the list item
        View textContainer = listItemView.findViewById(R.id.text_container);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        // Set the background color of th e textContainer view
        textContainer.setBackgroundColor(color);

        // return now the parent listItemView now formatted a complete with both miwok word and its translation
        return listItemView;
    }
}
