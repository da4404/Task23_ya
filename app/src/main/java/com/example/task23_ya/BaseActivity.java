package com.example.task23_ya;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author darya
 * @version 1.0
 * @since 10/05/2026
 * Base activity class providing common menu functionality
 */
public class BaseActivity extends AppCompatActivity
{

    /**
     * Initialize the contents of the Activity's standard options menu.
     * <p>
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * <p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
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