package com.example.hp1.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Ayush on 04-Jun-17.
 */
public class PetsProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

   private PetsDatabase pd;

    public  static final int PETS=100;
    public static final int PETS_ID=101;


    static {

        sUriMatcher.addURI(PetsContract.pets.authority, PetsContract.pets.TABLE_PATH,PETS);
        sUriMatcher.addURI(PetsContract.pets.authority, PetsContract.pets.TABLE_PATH+"/#",PETS_ID);


    }

    /** Tag for the log messages */
    public static final String LOG_TAG = PetsProvider.class.getSimpleName();

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        //  Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.

        pd=new PetsDatabase(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        SQLiteDatabase db=pd.getReadableDatabase();
        Cursor cursor;

        /*
         * Choose the table to query and a sort order based on the code returned for the incoming
         * URI. Here, too, only the statements for table 3 are shown.
         */
        int match=sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform database query on pets table
                cursor = db.query(PetsContract.pets.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);


            break;
            case PETS_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetsContract.pets._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = db.query(PetsContract.pets.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        return cursor; }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
// Insert into database by sql methods
    public   Uri insertdb(Uri uri,ContentValues values){
        // Check that the name is not null
        String name = values.getAsString(PetsContract.pets.COLUMN_PET_NAME);
        if (name .isEmpty()) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // Check that the gender is valid
        Integer gender = values.getAsInteger(PetsContract.pets.COLUMN_PET_GENDER);
        if (gender == null || !PetsContract.pets.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(PetsContract.pets.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }
        SQLiteDatabase db=pd.getWritableDatabase();
      long row=  db.insert(PetsContract.pets.TABLE_NAME, null, values);

        if(row==-1){

            Log.v("PetsProvider"," error inserting row");

        }

        return ContentUris.withAppendedId(uri,row);


    }


// calling content provider insert to furthur call insertdb to add in database
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match=sUriMatcher.match(uri);
        switch (match){
            case PETS:
               return insertdb(uri, contentValues);

            default:
                throw new IllegalArgumentException( "Inseryion not supported for"+uri);

        }

    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */

    public int updatepd(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){

        SQLiteDatabase db= pd.getWritableDatabase();

        int count = db.update(
                PetsContract.pets.TABLE_NAME,
                contentValues,
                selection,
                selectionArgs);

return  count;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {


        final int match=sUriMatcher.match(uri);
        switch(match){

            case PETS:
                return updatepd( uri,  contentValues,  selection,  selectionArgs);

            case PETS_ID:
                selection = PetsContract.pets._ID+ "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatepd(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);


        }

    }

    /**
     * Delete the data at the given selection and selection arguments.
     */

    public  int deletepd(Uri uri, String selection, String[] selectionArgs){

        SQLiteDatabase db=pd.getWritableDatabase();
     return   db.delete(PetsContract.pets.TABLE_NAME,selection,selectionArgs);




    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

int match=sUriMatcher.match(uri);
        switch (match){

            case PETS:
                return deletepd(uri,selection,selectionArgs);

        }

        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }






}
