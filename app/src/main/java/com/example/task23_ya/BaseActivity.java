package com.example.task23_ya;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity
{

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_display) {
            Intent intent = new Intent(this, DisplayActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_credits) {
            Intent intent = new Intent(this, CreditsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}