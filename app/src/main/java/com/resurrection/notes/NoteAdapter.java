package com.resurrection.notes;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.noteHolder> {

    private ArrayList<NoteTemplate> noteTemplateArrayList;
    private Context context;
     DatabaseHelper databaseHelper;


    public NoteAdapter(Context context,ArrayList<NoteTemplate> noteTemplateArrayList) {
        this.noteTemplateArrayList = noteTemplateArrayList;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
    }


    @NonNull
    @Override
    public NoteAdapter.noteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item,parent,false);
        return new noteHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.noteHolder holder, int position) {
        NoteTemplate noteTemplate = noteTemplateArrayList.get(position);
        String id = noteTemplate.getId();
        System.out.println("note adapterdaki note ıd "+id);
        String header = noteTemplate.getHeader();
        String content = noteTemplate.getContent();
        String date = noteTemplate.getDate();

        holder.header.setText(header);
        holder.content.setText(content);
        holder.date.setText(date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, id+" no notunuz", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,EditNote.class);
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return noteTemplateArrayList.size();
    }

    public class noteHolder extends RecyclerView.ViewHolder{

        TextView header,content,date;

        public noteHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
        }
    }
}
