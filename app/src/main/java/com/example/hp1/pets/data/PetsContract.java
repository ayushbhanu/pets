package com.example.hp1.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by Ayush on 01-Jun-17.
 */
public class PetsContract {

private PetsContract(){};
        /* Inner class that defines the table contents */

    public static class pets implements BaseColumns{
      public      final static String TABLE_NAME="pets";


        public  static String _ID = BaseColumns._ID;
        public final static String COLUMN_PET_NAME = "name";
      public   final static String COLUMN_PET_BREED = "breed";
        public final static String COLUMN_PET_GENDER = "gender";
        public final static String COLUMN_PET_WEIGHT = "weight";


       public final static int gender_unknown=0;
       public final static int gender_male=1;
        public final static int gender_female=2;
        public  static final String authority="com.example.hp1.pets";
public final static String PetschemaAndAuthority="content://"+authority;

        public static final Uri BASE_CONTENT=Uri.parse(PetschemaAndAuthority);


        public static final String TABLE_PATH="pets";

        public static final Uri content=Uri.withAppendedPath(BASE_CONTENT,TABLE_PATH);

        public static boolean isValidGender(int gender) {
            if (gender == gender_unknown || gender == gender_male || gender == gender_female) {
                return true;
            }
            return false;
        }





    }


}
