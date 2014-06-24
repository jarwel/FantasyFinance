package com.fantasystocks.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Pool;

public class OpenPoolArrayAdapter extends ArrayAdapter<Pool> {

	public OpenPoolArrayAdapter(Context context, List<Pool> pools) {
		super(context, R.layout.item_open_pool, pools);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Pool pool = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_open_pool, parent, false);
		}

		TextView tvPoolName = (TextView) convertView.findViewById(R.id.tvPlayerRank);
		TextView tvPoolPlayers = (TextView) convertView.findViewById(R.id.tvPlayerName);

		tvPoolName.setText(pool.getName());
		tvPoolPlayers.setText(String.format("%s(%s) players", 4, pool.getPlayerLimit()));
		return convertView;
	}
}
