package com.example.advancedcalculator.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancedcalculator.Numbers;
import com.example.advancedcalculator.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class historyListAdapter extends RecyclerView.Adapter<historyListAdapter.NumberViewHolder>
{
    private List<Numbers> numberList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public historyListAdapter(List<Numbers> numberList)
    {
        this.numberList = numberList;
    }

    @NonNull
    @Override
    public historyListAdapter.NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull historyListAdapter.NumberViewHolder holder, final int position)
    {
        final Numbers history = numberList.get(position);
        holder.textResult.setText(history.getResult());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                return deleteData(position);
            }
        });
    }

    public boolean deleteData(int position)
    {
        db.collection("CalculatorHistory").document(String.valueOf(getItemId(position))).delete();
        return (true);
    }

    @Override
    public int getItemCount()
    {
        return numberList.size();
//
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textResult;

        NumberViewHolder(View itemView)
        {
            super(itemView);
            textResult = itemView.findViewById(R.id.resultText);
        }
    }
}
