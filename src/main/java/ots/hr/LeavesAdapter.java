package ots.hr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class LeavesAdapter extends ArrayAdapter<Leave> {

    private ArrayList<Leave> leaves;
    private Context context;

    public LeavesAdapter(Context context, ArrayList<Leave> objects) {
        super(context, 0, objects);
        this.leaves = objects;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        Leave l = leaves.get(position);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.list_item, null);
        TextView leaveIdView = (TextView)rowView.findViewById(R.id.list_item_1);
        leaveIdView.setText("Leave id: "+l.getId());
        TextView descriptionView =  (TextView)rowView.findViewById(R.id.list_item_2);
        descriptionView.setText("Leave category: "+l.getDescription());
        TextView fromDateView = (TextView)rowView.findViewById(R.id.list_item_3);
        fromDateView.setText("Starting date: "+ l.getFromDate());
        TextView toDateView = (TextView)rowView.findViewById(R.id.list_item_4);
        toDateView.setText("Ending date: "+l.getToDate());
        TextView durationView = (TextView)rowView.findViewById(R.id.list_item_5);
        durationView.setText("Duration: "+l.getDuration());
        TextView statusView = (TextView)rowView.findViewById(R.id.list_item_6);
        statusView.setText("Status: "+l.getStatus());

        return  rowView;
    }


}

