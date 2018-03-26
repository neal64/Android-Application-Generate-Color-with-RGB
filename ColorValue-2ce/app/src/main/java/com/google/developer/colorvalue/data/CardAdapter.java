package com.google.developer.colorvalue.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.developer.colorvalue.CardActivity;
import com.google.developer.colorvalue.R;
import com.google.developer.colorvalue.ui.ColorView;

import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context context;
    private List<Card> cardList = new ArrayList<>();
    private Card card;
    private String cardName = null;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        View cardView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(cardView);
    }

    public CardAdapter (Context context, Cursor cursor){
        this.context = context;
        this.mCursor = cursor;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO bind data to view
        if (mCursor!=null) {
            mCursor.moveToLast();
            if (mCursor.moveToLast()) {
                while (!mCursor.isBeforeFirst()) {
                    Card card = new Card(mCursor);
                    cardList.add(card);
                    mCursor.moveToPrevious();
                }
            }
        }
        card = cardList.get(position);
        holder.name.setText(card.getHex());
        holder.hexa.setBackgroundColor(Color.parseColor(card.getHex()));
        holder.hexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CardActivity.class);
                intent.putExtra(CardProvider.Contract.Columns.CARD_ID,cardList.get(position).getID());
                intent.putExtra(CardProvider.Contract.Columns.COLOR_HEX, cardList.get(position).getHex());
                intent.putExtra(CardProvider.Contract.Columns.COLOR_NAME, cardList.get(position).getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Return a {@link Card} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Card}
     */
    private Card getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new Card(mCursor);
        }else
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        private RelativeLayout hexa;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.color_name);
            hexa = itemView.findViewById(R.id.card);
        }
    }
}
