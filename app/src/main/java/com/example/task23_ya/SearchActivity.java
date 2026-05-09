package com.example.task23_ya;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * @author darya
 * @version 1.0
 * @since 10/05/2026
 * Activity for searching and filtering expenses
 */
public class SearchActivity extends BaseActivity {

    EditText etSearchDesc, etMinAmount;
    ListView lvSearchResults;

    ArrayList<String> resultsList;
    ArrayAdapter<String> adapter;

    /**
     * Called when the activity is starting.
     * <p>
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearchDesc = findViewById(R.id.etSearchDesc);
        etMinAmount = findViewById(R.id.etMinAmount);
        lvSearchResults = findViewById(R.id.lvSearchResults);

        resultsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultsList);
        lvSearchResults.setAdapter(adapter);
    }

    /**
     * Searches expenses by description.
     * <p>
     *
     * @param view The view that was clicked.
     */
    public void searchByDesc(View view) {
        String searchTxt = etSearchDesc.getText().toString();
        if (searchTxt.isEmpty()) return;
        Query queryDesc = FBref.refExpenses.orderByChild("description").equalTo(searchTxt);
        executeSearchQuery(queryDesc);
    }

    /**
     * Filters expenses by minimum amount.
     * <p>
     *
     * @param view The view that was clicked.
     */
    public void filterByAmount(View view) {
        String amountTxt = etMinAmount.getText().toString();
        if (amountTxt.isEmpty()) return;

        double minAmount = Double.parseDouble(amountTxt);
        Query queryAmount = FBref.refExpenses.orderByChild("amount").startAt(minAmount);
        executeSearchQuery(queryAmount);
    }

    /**
     * Executes the Firebase query and updates the result list.
     * <p>
     *
     * @param query The Firebase query to execute.
     */
    private void executeSearchQuery(Query query) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * This method will be called with a snapshot of the data at this location.
             * <p>
             *
             * @param dS The current data at the location
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                resultsList.clear();

                for (DataSnapshot data : dS.getChildren()) {
                    Expense exp = data.getValue(Expense.class);
                    if (exp != null) {
                        String itemDisplay = exp.getDate() + " | " + exp.getCategory() + "\n"
                                + exp.getDescription() + " - ₪" + exp.getAmount();
                        resultsList.add(itemDisplay);
                    }
                }

                if (resultsList.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "לא נמצאו תוצאות", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or was removed as a result of the security and Firebase Database rules of the app.
             * <p>
             *
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSearch", "שגיאה בחיפוש: " + error.getMessage());
            }
        });
    }
}