/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hp1.pets;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp1.pets.data.PetsContract.pets;
import com.example.hp1.pets.EditorActivity;
import com.example.hp1.pets.data.PetsDatabase;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.widget.SimpleCursorAdapter;
/**
 * Displays list of pets that were entered and stored in the app.
 */


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

private final int Pet_Loader = 0;
PetCursorAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        PetsDatabase     mDbHelper = new PetsDatabase(this);
        // SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ListView lvItems = (ListView) findViewById(R.id.list_view_pet);
// Setup cursor adapter using cursor from last step
        View empty=findViewById(R.id.empty_view);
        lvItems.setEmptyView(empty);

         mAdapter = new PetCursorAdapter(this,null);
lvItems.setAdapter(mAdapter);
        //kick off loader

        getLoaderManager().initLoader(Pet_Loader,null,this);
    }

   // @Override
//
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }

    public void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
      //  PetsDatabase  mDbHelper = new PetsDatabase(this);


        // Create and/or open a database to read from it
       //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
//        Cursor cursor = db.rawQuery("SELECT * FROM " + pets.TABLE_NAME, null);

//String projection[]={pets._ID,pets.COLUMN_PET_NAME,pets.COLUMN_PET_BREED,pets.COLUMN_PET_GENDER,pets.COLUMN_PET_WEIGHT};


        // old method to work on sql database.

     //  Cursor cur= db.query(pets.TABLE_NAME,null,null,null,null,null,null);

        Cursor cur=getContentResolver().query(pets.content,null,null,null,null);
        Log.v("MainActivity","HELLO!!!!!!!!!!!!!!!");

        try {

            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
//            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
//            displayView.setText("Number of rows in pets database table: " + cur.getCount());
            // cur.moveToFirst();



//            int id=cur.getColumnIndex(pets._ID);
//            int name=cur.getColumnIndex(pets.COLUMN_PET_NAME);
//            int breed=cur.getColumnIndex(pets.COLUMN_PET_BREED);
//            int gender=cur.getColumnIndex(pets.COLUMN_PET_GENDER);
//            int weight=cur.getColumnIndex(pets.COLUMN_PET_WEIGHT);



          //  displayView.append("\n \n"+pets._ID+" - "+pets.COLUMN_PET_NAME +" - "+pets.COLUMN_PET_BREED+" - "+pets.COLUMN_PET_GENDER+" - "+pets.COLUMN_PET_WEIGHT );
//            while(cur.moveToNext()){
//                int pid=cur.getInt(id);
//
//                String pname=cur.getString(name);
//                String pbreed=cur.getString(breed);
//                int pgender=cur.getInt(gender);
//                int pweight=cur.getInt(weight);
//                displayView.append("\n"+pid+" - "+pname+" - "+pbreed+" - "+pgender+" - "+pweight);
//
//            }

            // Find ListView to populate
            ListView lvItems = (ListView) findViewById(R.id.list_view_pet);
// Setup cursor adapter using cursor from last step
            View empty=findViewById(R.id.empty_view);
            lvItems.setEmptyView(empty);

            PetCursorAdapter todoAdapter = new PetCursorAdapter(this, cur);
// Attach cursor adapter to the ListView
            lvItems.setAdapter(todoAdapter);



        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            //cur.close();
        }
    }

    public  void insertdummy(){
        // Log.v("Catalog Activity","Hello!!!!!!!! ");
//        PetsDatabase  mDbHelper = new PetsDatabase(this);
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
//value.put(pets.TABLE_NAME,"pets");
        values.put(pets.COLUMN_PET_NAME,EditorActivity.name);
        values.put(pets.COLUMN_PET_BREED,EditorActivity.breed);
        values.put(pets.COLUMN_PET_GENDER, EditorActivity.gender );
        values.put(pets.COLUMN_PET_WEIGHT, EditorActivity.weight);

       // long newrow=db.insert(pets.TABLE_NAME,null,values);

        // calling content provider method by content resolver
        Uri uri =getContentResolver().insert(pets.content,values);

        Log.v("Catalog Activity", "ERROR in " + uri);
        Toast.makeText(MainActivity.this, "Pets Saves "+uri, Toast.LENGTH_SHORT).show();
       // displayDatabaseInfo();



    }

    public  void delete(){
        int del=getContentResolver().delete(pets.content,null,null);

        Toast.makeText(MainActivity.this, "ALL"+del+ "pets deleted", Toast.LENGTH_SHORT).show();

displayDatabaseInfo();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:

                Intent intent= new Intent(MainActivity.this,EditorActivity.class);

                startActivity(intent);

                //insertdummy();
                // displayDatabaseInfo();


                // Do nothing for now
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now

                delete();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {

            /*
     * Takes action based on the ID of the Loader that's being created
     */
            switch (loaderID) {
                case Pet_Loader:
                    // Returns a new CursorLoader
                    return new CursorLoader(
                            this,   // Parent activity context
                            pets.content,        // Table to query
                            null,     // Projection to return
                            null,            // No selection clause
                            null,            // No selection arguments
                            null             // Default sort order
                    );
                default:
                    // An invalid id was passed in
                    return null;
            }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);


    }
}
