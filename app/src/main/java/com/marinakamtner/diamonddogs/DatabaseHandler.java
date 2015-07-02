package com.marinakamtner.diamonddogs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marina on 25.06.15.
 */
//http://www.vogella.com/tutorials/AndroidSQLite/article.html

public final class DatabaseHandler {

        private static final String TAG = "DatabaseHandler";
        // Database Name
        private static final String DATABASE_NAME = "finanzaemter.db";
        // Database Version
        private static final int DATABASE_VERSION = 1;

    /* Inner class that defines the table: table-name and table-columns contents */
    public static abstract class Finanzaemter implements BaseColumns {
        public static final String TABLE_NAME = "finanzaemter";
        public static final String COLUMN_NAME_IDKZ = "idkz";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_POSTCODE = "postcode"; // PLZ
        public static final String COLUMN_NAME_LOCATION = "location"; // Ort
        public static final String COLUMN_NAME_STREET = "street"; // Strasse
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_PHOTOURL = "photourl";
        public static final String COLUMN_NAME_OPENINGHOURS = "openinghours"; // Oeffnungszeiten
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }

        private DbHelper mDbHelper;

        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public DatabaseHandler(Context context) {
            this.mDbHelper = new DbHelper(context);
        }

// ------------------------------------------------------------------------

        public static class DbHelper extends SQLiteOpenHelper {

            private static final String LOG = "DatabaseHelper";
            private Context dbHelperContext;
            private SQLiteDatabase mDatabase;

            private static final String TEXT_TYPE = " TEXT";
            private static final String COMMA_SEP = ",";

            private static final String SQL_CREATE_TABLE_FINANZAEMTER =
                    "CREATE TABLE " + Finanzaemter.TABLE_NAME + " (" +
                            Finanzaemter._ID + " INTEGER PRIMARY KEY," +
                            Finanzaemter.COLUMN_NAME_IDKZ + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_STREET + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_POSTCODE + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_PHOTOURL + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_OPENINGHOURS + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP +
                            Finanzaemter.COLUMN_NAME_LONGITUDE + TEXT_TYPE + // last line no COMMA_SEP!
                            " )";

            private static final String SQL_DELETE_TABLE_FINANZAEMTER =
                    "DROP TABLE IF EXISTS " + Finanzaemter.TABLE_NAME;

            public DbHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_TABLE_FINANZAEMTER);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // This database is only a cache for online data, so its upgrade policy is
                // to simply to discard the data and start over
                Log.w(DbHelper.class.getName(),
                        "Upgrading database from version " + oldVersion + " to "
                                + newVersion + ", which will destroy all old data.");
                db.execSQL(SQL_DELETE_TABLE_FINANZAEMTER);
                onCreate(db);
            }

            @Override
            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onUpgrade(db, oldVersion, newVersion);
            }

            public void resetDb(SQLiteDatabase db) {
                Log.w(DbHelper.class.getName(),
                        "reset database, which will destroy all old data.");
                db.execSQL(SQL_DELETE_TABLE_FINANZAEMTER);
                onCreate(db);
            }

            public ArrayList<Cursor> getData(String Query){
                //get writable database
                SQLiteDatabase sqlDB = this.getWritableDatabase();
                String[] columns = new String[] { "mesage" };
                //an array list of cursor to save two cursors one has results from the query
                //other cursor stores error message if any errors are triggered
                ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
                MatrixCursor Cursor2= new MatrixCursor(columns);
                alc.add(null);
                alc.add(null);

                try{
                    String maxQuery = Query ;
                    //execute the query results will be save in Cursor c
                    Cursor c = sqlDB.rawQuery(maxQuery, null);

                    //add value to cursor2
                    Cursor2.addRow(new Object[] { "Success" });

                    alc.set(1,Cursor2);
                    if (null != c && c.getCount() > 0) {
                        alc.set(0,c);
                        c.moveToFirst();
                        return alc ;
                    }
                    return alc;
                } catch(SQLException sqlEx){
                    Log.d("printing exception", sqlEx.getMessage());
                    //if any exceptions are triggered save the error message to cursor an return the arraylist
                    Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
                    alc.set(1,Cursor2);
                    return alc;
                } catch(Exception ex){
                    Log.d("printing exception", ex.getMessage());
                    //if any exceptions are triggered save the error message to cursor an return the arraylist
                    Cursor2.addRow(new Object[] { ""+ex.getMessage() });
                    alc.set(1,Cursor2);
                    return alc;
                }
            }
        }

        public static class FinanzaemterDataSource {
            private SQLiteDatabase database;
            private DbHelper dbHelper;
            private String[] allColumns = {
                    Finanzaemter._ID,
                    Finanzaemter.COLUMN_NAME_IDKZ,
                    Finanzaemter.COLUMN_NAME_NAME,
                    Finanzaemter.COLUMN_NAME_STREET,
                    Finanzaemter.COLUMN_NAME_POSTCODE,
                    Finanzaemter.COLUMN_NAME_LOCATION,
                    Finanzaemter.COLUMN_NAME_PHONE,
                    Finanzaemter.COLUMN_NAME_PHOTOURL,
                    Finanzaemter.COLUMN_NAME_OPENINGHOURS,
                    Finanzaemter.COLUMN_NAME_LATITUDE,
                    Finanzaemter.COLUMN_NAME_LONGITUDE
            };

            public FinanzaemterDataSource(Context context) {
                dbHelper = new DbHelper(context);
            }

            public void open() throws SQLException {
                database = dbHelper.getWritableDatabase();
            }

            public void close() {
                dbHelper.close();
            }

            public boolean isOpen(){
                return database.isOpen();
            }

            public Finanzamt createFinanzamt(String idkz, String name, String street, String postcode,
                                             String location, String phone, String openinghours,
                                             String photourl, String latitude, String longitude) {
                ContentValues values = new ContentValues();
                values.put(Finanzaemter.COLUMN_NAME_IDKZ, idkz);
                values.put(Finanzaemter.COLUMN_NAME_NAME, name);
                values.put(Finanzaemter.COLUMN_NAME_LOCATION, location);
                values.put(Finanzaemter.COLUMN_NAME_STREET, street);
                values.put(Finanzaemter.COLUMN_NAME_POSTCODE, postcode);
                values.put(Finanzaemter.COLUMN_NAME_PHONE, phone);
                values.put(Finanzaemter.COLUMN_NAME_PHOTOURL, photourl);
                values.put(Finanzaemter.COLUMN_NAME_OPENINGHOURS, openinghours);
                values.put(Finanzaemter.COLUMN_NAME_LATITUDE, latitude);
                values.put(Finanzaemter.COLUMN_NAME_LONGITUDE, longitude);

                if (!isOpen()){
                    open();
                }

                long insertId = database.insert(Finanzaemter.TABLE_NAME, null, values);
                Cursor cursor = database.query(Finanzaemter.TABLE_NAME,
                        allColumns, Finanzaemter._ID + " = " + insertId, null,
                        null, null, null);
                cursor.moveToFirst();
                Finanzamt newFinanzamt = cursorToFinanzamt(cursor);
                cursor.close();

                return newFinanzamt;
            }

            public void deleteAllRows() {
                database.execSQL("delete from " + Finanzaemter.TABLE_NAME);
            }

            public List<Finanzamt> getAllFinanzaemter() {
                List<Finanzamt> finanzaemter = new ArrayList<Finanzamt>();

                Cursor cursor = database.query(Finanzaemter.TABLE_NAME,
                        allColumns, null, null, null, null, null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Finanzamt finanzamt = cursorToFinanzamt(cursor);
                    finanzaemter.add(finanzamt);
                    cursor.moveToNext();
                }
                cursor.close();
                return finanzaemter;
            }

            private Finanzamt cursorToFinanzamt(Cursor cursor) {
                Finanzamt finanzamt = new Finanzamt();
                finanzamt.setId(cursor.getInt(0));
                finanzamt.setIdkz(cursor.getString(1));
                finanzamt.setName(cursor.getString(2));
                finanzamt.setStreet(cursor.getString(3));
                finanzamt.setPostcode(cursor.getString(4));
                finanzamt.setLocation(cursor.getString(5));
                finanzamt.setPhone(cursor.getString(6));
                finanzamt.setPhotourl(cursor.getString(7));
                finanzamt.setOpeninghours(cursor.getString(8));
                finanzamt.setLatitude(cursor.getString(9));
                finanzamt.setLongitude(cursor.getString(10));
                Cursor mCount = database.rawQuery("select count(*) from " + Finanzaemter.TABLE_NAME, null);
                mCount.moveToFirst();
                int count = mCount.getInt(0);
                mCount.close();
                return finanzamt;
            }
        }
    }


