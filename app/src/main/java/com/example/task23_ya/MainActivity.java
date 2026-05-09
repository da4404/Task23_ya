package com.example.task23_ya;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    EditText etDescription, etAmount;
    Spinner spinnerCategory;
    Button btnDate, btnSave;
    TextView tvSelectedDate;

    String selectedDate = "";
    String[] categories = {"אוכל", "בילוי", "תחבורה", "מגשימים", "קונסרבטוריון", "אחר"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnDate = findViewById(R.id.btnDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        btnDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                calendar.set(year, month, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
                                selectedDate = sdf.format(calendar.getTime());
                                tvSelectedDate.setText("תאריך נבחר: " + selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String desc = etDescription.getText().toString();
                String amountStr = etAmount.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();

                if (desc.isEmpty() || amountStr.isEmpty() || selectedDate.isEmpty()) {
                    Toast.makeText(MainActivity.this, "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);

                String keyID = FBref.refExpenses.push().getKey();

                if (keyID != null) {
                    Expense newExpense = new Expense(keyID, desc, amount, category, selectedDate);

                    FBref.refExpenses.child(keyID).setValue(newExpense).addOnCompleteListener(task -> {if (task.isSuccessful()) {
                                    Log.d("FirebaseSave", "ההוצאה נשמרה בהצלחה במסד הנתונים!");
                                    Toast.makeText(MainActivity.this, "ההוצאה נשמרה!", Toast.LENGTH_SHORT).show();
                                    etDescription.setText("");
                                    etAmount.setText("");
                                    tvSelectedDate.setText("לא נבחר תאריך");
                                    selectedDate = "";
                                } else {
                                    Log.e("FirebaseSave", "שגיאה בשמירת נתונים", task.getException());
                                }
                            });
                }
            }
        });
    }
}