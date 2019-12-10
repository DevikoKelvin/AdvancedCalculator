package com.example.advancedcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.advancedcalculator.Adapter.historyListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private final String tag = "MainActivity";
    private List<Numbers> numbers = new ArrayList<>();
    private RecyclerView recyclerView;
    private historyListAdapter adapter;

    private EditText input1, input2;
    private RadioButton add, subtract, multiply, divide;
    private RadioGroup operators;
    private TextView result;
    private Button count;

    private RecyclerView.LayoutManager manager;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input1 = findViewById(R.id.firstInput);
        input2 = findViewById(R.id.secondInput);
        operators = findViewById(R.id.aritmathicOperation);
        add = findViewById(R.id.addRadio);
        subtract = findViewById(R.id.subtractRadio);
        multiply = findViewById(R.id.multiplyRadio);
        divide = findViewById(R.id.divideRadio);
        result = findViewById(R.id.resultText);
        count = findViewById(R.id.countButton);
        recyclerView = findViewById(R.id.historyRecycler);

        manager = new LinearLayoutManager(getApplicationContext());
        adapter = new historyListAdapter(numbers);

        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        viewRecycler();

        count.setEnabled(false);
        operators.setEnabled(false);
        add.setEnabled(false);
        subtract.setEnabled(false);
        multiply.setEnabled(false);
        divide.setEnabled(false);

        input1.addTextChangedListener(executeWatcher);
        input2.addTextChangedListener(executeWatcher);

        count.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(add.isChecked())
                {
                    add();
                }
                else if(subtract.isChecked())
                    {
                    subtract();
                    }
                    else if(multiply.isChecked())
                        {
                            multiply();
                        }
                        else if(divide.isChecked())
                            {
                                divide();
                            }
                            else
                            {
                                Log.w(tag, "Please select the arithmetic operation.");
                            }
            }
        });
    }

    private TextWatcher executeWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            String input1_input = input1.getText().toString().trim();
            String input2_input = input2.getText().toString().trim();

            count.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            operators.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            add.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            subtract.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            multiply.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            divide.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    };

    public void add()
    {
        double first = Double.parseDouble(input1.getText().toString());
        double second = Double.parseDouble(input2.getText().toString());
        double res = first+second;
        String textResult = "" + first + " + " + second + " = " + res;

        result.setText(String.valueOf(res));
        addData(textResult);
    }

    public void subtract()
    {
        double first = Double.parseDouble(input1.getText().toString());
        double second = Double.parseDouble(input2.getText().toString());
        double res = first-second;
        String textResult = "" + first + " - " + second + " = " + res;

        result.setText(String.valueOf(res));
        addData(textResult);
    }

    public void multiply()
    {
        double first = Double.parseDouble(input1.getText().toString());
        double second = Double.parseDouble(input2.getText().toString());
        double res = first*second;
        String textResult = "" + first + " x " + second + " = " + res;

        result.setText(String.valueOf(res));
        addData(textResult);
    }

    public void divide()
    {
        double first = Double.parseDouble(input1.getText().toString());
        double second = Double.parseDouble(input2.getText().toString());
        double res = first/second;
        String textResult = "" + first + " ÷ " + second + " = " + res;

        result.setText(String.valueOf(res));
        addData(textResult);
    }

    public void addData(String historyText)
    {
        Map<String, Object> history = new HashMap<>();
        history.put("text", historyText);

        db.collection("CalculatorHistory").add(history);
    }

    public void viewRecycler()
    {
        db.collection("CalculatorHistory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                            {
                                Numbers num = new Numbers();
                                num.setResult(documentSnapshot.get("text").toString());
                                numbers.add(num);
                            }
                        }
                    }
                });
    }

    public void clearAll(View view)
    {
        input1.setText("");
        input2.setText("");
        result.setText("");
    }
}
