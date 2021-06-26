package com.example.familyhealthhandbook.Adapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.familyhealthhandbook.Model.Sickness;
import com.example.familyhealthhandbook.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AutoCompleteTextViewSicknessAdapter extends ArrayAdapter<Sickness> {

    private List<Sickness> sicknesses;
    private List<Sickness> allSicknesses;
    private Context context;


    public AutoCompleteTextViewSicknessAdapter(@NonNull Context context, @NonNull ArrayList<Sickness> sicknesses) {
        super(context, 0, sicknesses);
        this.context = context;
        this.sicknesses = new ArrayList<>(sicknesses);
        this.allSicknesses = new ArrayList<>(sicknesses);
    }

    @Override
    public int getCount() {
        return sicknesses.size();
    }

    @Nullable
    @Override
    public Sickness getItem(int position) {
        return sicknesses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        TextView textView = convertView.findViewById(android.R.id.text1);

        Sickness sickness  = getItem(position);
        if(sickness!= null)
        {
            textView.setText(sickness.getName());
        }
        return  convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return sicknessFilter;
    }

    private Filter sicknessFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Sickness> suggestions = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
                suggestions.addAll(allSicknesses);
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Sickness item: allSicknesses)
                {
                    if(item.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            clear();
//            addAll((List) results.values);
//            notifyDataSetChanged();
            if (results.values != null) {
                sicknesses = (ArrayList<Sickness>)results.values;
            } else {
                sicknesses = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Sickness) resultValue).getName();
        }
    };
}
