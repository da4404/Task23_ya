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

public class SearchActivity extends BaseActivity {

    EditText etSearchDesc, etMinAmount;
    ListView lvSearchResults;

    ArrayList<String> resultsList;
    ArrayAdapter<String> adapter;

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

    public void searchByDesc(View view) {
        String searchTxt = etSearchDesc.getText().toString();
        if (searchTxt.isEmpty()) return;
        Query queryDesc = FBref.refExpenses.orderByChild("description").equalTo(searchTxt);
        executeSearchQuery(queryDesc);
    }

    public void filterByAmount(View view) {
        String amountTxt = etMinAmount.getText().toString();
        if (amountTxt.isEmpty()) return;

        double minAmount = Double.parseDouble(amountTxt);
        Query queryAmount = FBref.refExpenses.orderByChild("amount").startAt(minAmount);
        executeSearchQuery(queryAmount);
    }

    private void executeSearchQuery(Query query) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSearch", "שגיאה בחיפוש: " + error.getMessage());
            }
        });
    }
}