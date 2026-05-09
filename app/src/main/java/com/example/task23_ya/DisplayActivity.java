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

/**
 * @author darya
 * @version 1.0
 * @since 10/05/2026
 * Activity for displaying and managing the list of expenses
 */
public class DisplayActivity extends BaseActivity
{

    ListView lvExpenses;
    TextView tvTotalAmount;
    ArrayList<Expense> expensesList;
    ArrayList<String> displayList;
    ArrayAdapter<String> adapter;

    ValueEventListener expenseListener;

    /**
     * Called when the activity is starting.
     * <p>
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
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
            /**
             * This method will be called with a snapshot of the data at this location.
             * <p>
             *
             * @param dS The current data at the location
             */
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

            /**
             * This method will be triggered in the event that this listener either failed at the server, or was removed as a result of the security and Firebase Database rules of the app.
             * <p>
             *
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };

        FBref.refExpenses.addValueEventListener(expenseListener);
        lvExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * Callback method to be invoked when an item in this view has been clicked and held.
             * <p>
             *
             * @param parent The AbsListView where the click happened
             * @param view The view within the AbsListView that was clicked
             * @param position The position of the view in the list
             * @param id The row id of the item that was clicked
             * @return true if the callback consumed the long click, false otherwise
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense = expensesList.get(position);
                showUpdateDeleteDialog(selectedExpense);
                return true;
            }
        });
    }

    /**
     * Called as part of the activity lifecycle when the user no longer actively interacts with the activity, but it is still visible on screen.
     * <p>
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (expenseListener != null) {
            FBref.refExpenses.removeEventListener(expenseListener);
        }
    }

    /**
     * Shows a dialog to update or delete the selected expense.
     * <p>
     *
     * @param expense The expense object to update or delete
     */
    private void showUpdateDeleteDialog(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("פעולות על הוצאה");
        builder.setMessage("מה ברצונך לעשות עם ההוצאה: " + expense.getDescription() + "?");
        builder.setPositiveButton("מחיקה", new DialogInterface.OnClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             * <p>
             *
             * @param dialog The dialog that received the click
             * @param which The button that was clicked
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FBref.refExpenses.child(expense.getKeyID()).removeValue();
                Toast.makeText(DisplayActivity.this, "ההוצאה נמחקה!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("עדכן סכום", new DialogInterface.OnClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             * <p>
             *
             * @param dialog The dialog that received the click
             * @param which The button that was clicked
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText input = new EditText(DisplayActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input.setText(String.valueOf(expense.getAmount()));

                new AlertDialog.Builder(DisplayActivity.this)
                        .setTitle("סכום חדש (₪)")
                        .setView(input)
                        .setPositiveButton("עדכן ב-Firebase", new DialogInterface.OnClickListener() {
                            /**
                             * This method will be invoked when a button in the dialog is clicked.
                             * <p>
                             *
                             * @param dialog2 The dialog that received the click
                             * @param which2 The button that was clicked
                             */
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
