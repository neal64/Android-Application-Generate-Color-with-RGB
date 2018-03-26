package com.google.developer.colorvalue;

import android.content.ContentValues;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.developer.colorvalue.data.CardProvider;
import com.google.developer.colorvalue.ui.ColorView;

public class AddCardActivity extends AppCompatActivity implements
        View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ColorView mColor;
    private SeekBar mRed;
    private SeekBar mGreen;
    private SeekBar mBlue;
    private TextInputEditText mName;

    private int colorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        mColor = (ColorView) findViewById(R.id.color);

        mRed = (SeekBar) findViewById(R.id.seek_red);
        mGreen = (SeekBar) findViewById(R.id.seek_green);
        mBlue = (SeekBar) findViewById(R.id.seek_blue);

        mName = (TextInputEditText) findViewById(R.id.color_name);

        mRed.setOnSeekBarChangeListener(this);
        mGreen.setOnSeekBarChangeListener(this);
        mBlue.setOnSeekBarChangeListener(this);

        Button button = (Button) findViewById(R.id.button_add);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setColorToView();

        String hex = String.format("#%06X", (0xFFFFFF & colorId));
        String color = mName.getText().toString().trim();

        if (color.length() > 0) {
            ContentValues values = new ContentValues(2);
            values.put(CardProvider.Contract.Columns.COLOR_HEX, hex);
            values.put(CardProvider.Contract.Columns.COLOR_NAME, color);

            getContentResolver().insert(CardProvider.Contract.CONTENT_URI, values);
            finish();
        } else {
            mName.setError(getString(R.string.error_input_name));
            Snackbar.make(mName, R.string.complete_form, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Update {@link ColorView} to new color from SeekBars
     */
    private void setColorToView() {
        int r = mRed.getProgress();
        int g = mGreen.getProgress();
        int b = mBlue.getProgress();

        colorId = Color.rgb(r, g, b);
        mColor.setColor(colorId);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setColorToView();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Not used
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Not used
    }
}
