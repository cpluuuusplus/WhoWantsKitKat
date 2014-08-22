package com.ensaitechnomobile.pamplemousse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ensai.appli.R;
import com.ensaitechnomobile.metier.DayItem;
import com.ensaitechnomobile.metier.Item;
import com.ensaitechnomobile.metier.LessonItem;

public class LessonAdapter extends ArrayAdapter<Item> {
	private SimpleDateFormat hFormat = new SimpleDateFormat("HH:mm",
			Locale.FRENCH);
	private SimpleDateFormat dFormat = new SimpleDateFormat(
			"EEEE, dd MMMM yyyy\n", Locale.FRENCH);

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

	public LessonAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
			if (i.isSection()) {
				DayItem di = (DayItem) i;
				v = vi.inflate(R.layout.list_item_day, parent, false);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView sectionView = (TextView) v
						.findViewById(R.id.list_item_section_text);
				sectionView.setText(dFormat.format(new Date(di.getTitle())));

			} else {
				LessonItem li = (LessonItem) i;
				v = vi.inflate(R.layout.list_item_lesson, parent, false);
				final TextView debut = (TextView) v.findViewById(R.id.debut);
				final TextView salle = (TextView) v.findViewById(R.id.salle);
				final TextView nom = (TextView) v.findViewById(R.id.nom);
				final TextView fin = (TextView) v.findViewById(R.id.fin);

				if (debut != null)
					debut.setText(hFormat.format(new Date(li.getDebut())));
				if (salle != null)
					salle.setText(li.getSalle());
				if (nom != null)
					nom.setText(li.getNom());
				if (fin != null)
					fin.setText(hFormat.format(new Date(li.getFin())));
			}
		}
		return v;
	}
}

// @Override
// public View getView(int position, View convertView, ViewGroup parent) {
// View v = convertView;
//
// final Item i = items.get(position);
// if (i != null) {
// if (i.isSection()) {
// DayItem si = (DayItem) i;
// v = vi.inflate(R.layout.list_item_section,null);
//
// v.setOnClickListener(null);
// v.setOnLongClickListener(null);
// v.setLongClickable(false);
//
// final TextView sectionView = (TextView) v
// .findViewById(R.id.list_item_section_text);
// sectionView.setText(dFormat.format(new Date(si.getTitle())));
//
// } else {
// EntryItem ei = (EntryItem) i;
// v = vi.inflate(R.layout.list_item_entry, parent, false);
// final TextView title = (TextView) v
// .findViewById(R.id.list_item_entry_title);
// final TextView subtitle = (TextView) v
// .findViewById(R.id.list_item_entry_summary);
//
// if (title != null)
// title.setText(ei.title);
// if (subtitle != null)
// subtitle.setText(ei.subtitle);
// }
// }
// return v;
// }
//
// }
