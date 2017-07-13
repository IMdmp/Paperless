package com.example.dominic.paperless;

/**
 * Created by jmkhilario on 07/04/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dominic.paperless.Model.Event;

import org.w3c.dom.Text;

public class SurveyAdapter  extends CursorRecyclerViewAdapter<SurveyAdapter.SurveyViewHolder>{


    public SurveyAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }
    @Override
    public void onBindViewHolder(SurveyViewHolder viewHolder, Cursor cursor) {
        Event e = new Event();
        e.setFormName(cursor.getString(cursor.getColumnIndex(Event.COLUMN_FORMNAME)));
        e.setOrganizerName(cursor.getString(cursor.getColumnIndex(Event.COLUMN_ORGANIZERNAME)));


       String firstLetter = e.getOrganizerName().substring(0, 1);
        System.out.println("LOCAL: got first letter: "+firstLetter);
        viewHolder.tv_event_name.setText(e.getFormName());
        viewHolder.tv_organizer_name.setText(e.getOrganizerName());
        viewHolder.tv_organizer_initial.setText(firstLetter);


        //TODO: remove.
        String htmlName = cursor.getString(cursor.getColumnIndex(Event.COLUMN_HTMLNAME));
        viewHolder.llayout_one.setTag(R.string.htmlName,htmlName);

        int eventID =cursor.getInt(cursor.getColumnIndex(Event.COLUMN_ID));
        viewHolder.llayout_one.setTag(R.string.eventID,eventID);
        viewHolder.llayout_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v);
            }
        });
    }

    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_survey_item_row,parent,false);
        return new SurveyViewHolder(v);
    }


    public class SurveyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_organizer_name,tv_event_name,tv_organizer_initial;
        RelativeLayout rlayout_one;
        LinearLayout llayout_one;


        public SurveyViewHolder(View itemView) {
            super(itemView);
            tv_organizer_name = (TextView) itemView.findViewById(R.id.tv_organizer_name);
            tv_event_name = (TextView) itemView.findViewById(R.id.tv_event_name);
            tv_organizer_initial = (TextView) itemView.findViewById(R.id.tv_organizer_initial);
            rlayout_one = (RelativeLayout) itemView.findViewById(R.id.rlayout_one);
            llayout_one  = (LinearLayout) itemView.findViewById(R.id.llayout_one);
        }
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(View v);

    }
}
