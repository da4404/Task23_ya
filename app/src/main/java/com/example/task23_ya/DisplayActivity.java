package com.example.task23_ya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayActivity extends BaseActivity
{

    ListView lvExpenses;
    TextView tvTotalAmount;
    ArrayList<Expense> expensesList;
    ArrayList<String> displayList;
    ArrayAdapter<String> adapter;

    ValueEventListener expenseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        lvExpenses = findViewById(R.id.lvExpenses);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        expensesList = new ArrayList<>();
        displayList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvExpenses.setAdapter(adapter);
        expenseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                expensesList.clear();
                displayList.clear();
                double total = 0;
                for (DataSnapshot data : dS.getChildren())
                {
                    Expense exp = data.getValue(Expense.class);
                    if (exp != null) {
                        expensesList.add(exp);
                        String itemDisplay = "תאריך: " + exp.getDate() + " | קטגוריה: " + exp.getCategory() + "\n"
                                + "תיאור: " + exp.getDescription() + "\n"
                                + "סכום: ₪" + exp.getAmount();
                        displayList.add(itemDisplay);
                        total += exp.getAmount();
                    }
                }
                adapter.notifyDataSetChanged();
                tvTotalAmount.setText("סך הכל הוצאות: " + total + " ₪");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };

        FBref.refExpenses.addValueEventListener(expenseListener);
        lvExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense = expensesList.get(position);
                showUpdateDeleteDialog(selectedExpense);
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (expenseListener != null) {
            FBref.refExpenses.removeEventListener(expenseListener);
        }
    }

    private void showUpdateDeleteDialog(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("פעולות על הוצאה");
        builder.setMessage("מה ברצונך לעשות עם ההוצאה: " + expense.getDescription() + "?");
        builder.setPositiveButton("מחיקה", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FBref.refExpenses.child(expense.getKeyID()).removeValue();
                Toast.makeText(DisplayActivity.this, "ההוצאה נמחקה!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("עדכן סכום", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText input = new EditText(DisplayActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input.setText(String.valueOf(expense.getAmount()));

                new AlertDialog.Builder(DisplayActivity.this)
                        .setTitle("סכום חדש (₪)")
                        .setView(input)
                        .setPositiveButton("עדכן ב-Firebase", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int which2) {
                                String newAmountStr = input.getText().toString();
                                if (!newAmountStr.isEmpty()) {
                                    double newAmount = Double.parseDouble(newAmountStr);
                                    expense.setAmount(newAmount);
                                    FBref.refExpenses.child(expense.getKeyID()).setValue(expense);
                                    Toast.makeText(DisplayActivity.this, "הסכום עודכן!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("ביטול", null)
                        .show();
            }
        });

        builder.setNeutralButton("חזור", null);
        builder.show();
    }
}