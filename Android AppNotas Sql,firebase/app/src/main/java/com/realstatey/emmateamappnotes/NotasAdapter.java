package com.realstatey.emmateamappnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


//ADAPTADOR
public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.ViewHolder>{
    //definir la calse de nuiestro proyecto
    List<Notes>notes;
    Context context;

    public NotasAdapter(Context context,List<Notes>notes){
        this.context=context;
        this.notes=notes;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notas,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
    holder.count.setText(String.valueOf(notes.get(position).count));
    holder.name.setText(notes.get(position).name);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }



    //metodo importante dentor de las
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView count,name;
        //ImageView trash;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.cardView);
            count=(TextView) view.findViewById(R.id.count);
            name=(TextView)view.findViewById(R.id.name);
        //    trash=(ImageView)view.findViewById(R.id.trash);

        }
}
}
