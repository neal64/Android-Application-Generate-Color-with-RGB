package com.google.developer.colorvalue;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.developer.colorvalue.data.Card;
import com.google.developer.colorvalue.data.CardProvider;
import com.google.developer.colorvalue.service.CardService;
import com.google.developer.colorvalue.ui.ColorView;

import static android.content.Intent.ACTION_DELETE;

/**
 * Created by nileshpatel on 3/24/18.
 */

public class CardActivity extends AppCompatActivity {
    private ColorView colorView;
    private Bundle bundle;
    private String strHex;
    private String colorName;
    private String isId;
    private TextView txtName;
    private static final String ACTION_DELETE = CardService.class.getSimpleName() + ".DELETE";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            isId = bundle.getString(CardProvider.Contract.Columns.CARD_ID);
            strHex = bundle.getString(CardProvider.Contract.Columns.COLOR_HEX);
            colorName = bundle.getString(CardProvider.Contract.Columns.COLOR_NAME);
        }
        initialization();

    }

    public void initialization(){
        colorView = findViewById(R.id.card_details);
        txtName = findViewById(R.id.txt_color);
        txtName.setText(String.format("%s\n%s", colorName, strHex));
        colorView.isShowingText(false);
        colorView.setColor(strHex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        //Disable setting option
        MenuItem settingMenu = menu.findItem(R.id.action_settings);
        settingMenu.setVisible(false);

        //Display Delete option
        MenuItem deletMenu = menu.findItem(R.id.action_delete);
        deletMenu.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int manuItems = item.getItemId();

        //delete color on click
        if (manuItems == R.id.action_delete) {

            //send data
            Intent cardService = new Intent(this,CardService.class);
            cardService.setAction(ACTION_DELETE);
            cardService.putExtra(Intent.EXTRA_TEXT, strHex);
            cardService.setType("text/plain");
            startService(cardService);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
