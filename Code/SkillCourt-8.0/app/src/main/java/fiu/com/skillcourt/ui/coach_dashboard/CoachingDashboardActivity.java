package fiu.com.skillcourt.ui.coach_dashboard;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;

public class CoachingDashboardActivity extends BaseActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("users");
    private Menu menu;
    private List<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (savedInstanceState == null) {
            replaceFragment(CoachingFragment.newInstance(), false);
        }


        handleIntent(getIntent());
        setNavigationToolbar();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        this.menu = menu;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextChange (String newText) {
                        //loadHistory(newText);

//                        Query queryRef = mUserRef.orderByChild("role").equalTo(newText);
//
//                        System.out.println(queryRef.toString());






                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        mUserRef.orderByChild("email").startAt(query)
                                .addValueEventListener(
                                        new ValueEventListener() {
                                            public void onDataChange(DataSnapshot snapshot) {
                                                items.clear();
                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                    String email = postSnapshot.child("email").getValue(String.class);
                                                    items.add(email);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                        for(int i = 0; i<items.size();i++)
                            System.out.println(items.get(i));


                        return false;
                    }
                });

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(
                getApplicationContext(), CoachingDashboardActivity.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));

        return true;
    }

    public class ExampleAdapter extends CursorAdapter {

        private List<String> items;
        private TextView text;

        public ExampleAdapter(Context context, Cursor cursor, List<String> items) {
            super(context, cursor, false);
            this.items = items;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item, parent, false);
            text = (TextView) view.findViewById(R.id.item);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            text.setText(items.get(cursor.getPosition()));
        }
    }

    private void loadHistory(String s) {

        // Cursor
        String[] columns = new String[] { "_id", "text" };
        Object[] temp = new Object[] { 0, "default" };

        MatrixCursor cursor = new MatrixCursor(columns);

        for(int i = 0; i < items.size(); i++) {
            temp[0] = i;
            temp[1] = items.get(i);
            cursor.addRow(temp);
        }

        // SearchView
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSuggestionsAdapter(new ExampleAdapter(this, cursor, items));
    }

}
