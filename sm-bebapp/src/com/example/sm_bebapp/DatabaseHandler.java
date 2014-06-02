package com.example.sm_bebapp;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    // Contacts Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_LASTANSWER = "last_answer";
    private static final String KEY_STORYLOCATION = "story_location";
    private static final String KEY_OLDSTORYLOCATION = "old_story_location";
    private static final String KEY_STATE = "state";
    private static final String KEY_YEAR = "year";
    private static final String KEY_WORDS1 = "words1";
    private static final String KEY_WORDS2 = "words2";
    
    
    //Response table name
    private static final String TABLE_RESPONSES = "responses";
    //Response table columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TEXT = "text"; 
    private static final String KEY_TIME = "time"; 
    private static final String KEY_YES_LINK = "yeslink";
    private static final String KEY_NO_LINK = "nolink";
    private static final String KEY_MAYBE_LINK = "maybelink";
    private static final String KEY_ANY_LINK = "anylink";
    private static final String KEY_NORESP_LINK = "noresplink";
    
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
    			+ KEY_PH_NO + 			" TEXT,"
    			+ KEY_NAME  +			" TEXT,"
                + KEY_LASTANSWER + 		" TEXT,"
    			+ KEY_STORYLOCATION + 	" TEXT,"
    			+ KEY_OLDSTORYLOCATION + " TEXT,"
    			+ KEY_STATE + 			" TEXT,"
    			+ KEY_YEAR + 			" TEXT,"
    			+ KEY_WORDS1 + 			" TEXT,"
    			+ KEY_WORDS2 + 			" TEXT"
    			+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        
        String CREATE_RESPONSE_TABLE = "CREATE TABLE " + TABLE_RESPONSES + "("
    			+ KEY_ID + 				" TEXT,"
    			+ KEY_TEXT  +			" TEXT,"
                + KEY_TIME  +			" TEXT,"
    			+ KEY_YES_LINK + 		" TEXT,"
    			+ KEY_NO_LINK + 		" TEXT,"
    			+ KEY_MAYBE_LINK + 		" TEXT,"
    			+ KEY_ANY_LINK + 		" TEXT,"
    			+ KEY_NORESP_LINK + 	" TEXT"
    			+ ")";
        db.execSQL(CREATE_RESPONSE_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new Player
    void addPlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_PH_NO, player.getPhoneNumber()); // Player Phone
        values.put(KEY_NAME, player.getName()); // Player Name
        values.put(KEY_LASTANSWER, player.getLastAnswer());
        values.put(KEY_STORYLOCATION, player.getStoryLocation());
        values.put(KEY_OLDSTORYLOCATION, player.getOldStoryLocation());
        values.put(KEY_STATE, player.getState());
        values.put(KEY_YEAR, player.getYear());
        values.put(KEY_WORDS1, player.getWords1());
        values.put(KEY_WORDS2, player.getWords2());
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
    
 // Adding new RESPONSE
    void addResponse(Response response) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, response.getId()); 
        values.put(KEY_TEXT, response.getText()); 
        values.put(KEY_TIME, response.getTime()); 
        values.put(KEY_YES_LINK, response.getYesLink());
        values.put(KEY_NO_LINK, response.getNoLink());
        values.put(KEY_MAYBE_LINK, response.getMaybeLink());
        values.put(KEY_ANY_LINK, response.getAnyLink());
        values.put(KEY_NORESP_LINK, response.getNoResponseLink());
        // Inserting Row
        db.insert(TABLE_RESPONSES, null, values);
        db.close(); // Closing database connection
    }
    
    // Getting single Player
    Player getPlayer(String _phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
        		KEY_PH_NO, KEY_NAME,  KEY_LASTANSWER, KEY_STORYLOCATION, KEY_OLDSTORYLOCATION, KEY_STATE, KEY_YEAR, KEY_WORDS1, KEY_WORDS2}, KEY_PH_NO + "=?",
                new String[] { _phoneNumber }, null, null, null, null);

        if (cursor.moveToFirst())
        {
        	Log.d(">> DATA HANDLER"  , "Found Player #" + _phoneNumber);
        	Player player = new Player();
            player.setPhoneNumber(		cursor.getString(0));
            player.setName(				cursor.getString(1));
            player.setLastAnswer(		cursor.getString(2));
            player.setStoryLocation(	cursor.getString(3));
            player.setOldStoryLocation(	cursor.getString(4));
            player.setState(			cursor.getString(5));
            player.setYear(				cursor.getString(6));
            player.setWords1(			cursor.getString(7));
            player.setWords2(			cursor.getString(8));
            // return contact
            cursor.close();
            db.close();
            return player;
        } 
        else
        {
        	Log.d(">> DATA HANDLER"  , "No Player with #" + _phoneNumber);
        	return null; 
        }
    }
    
    // Getting single Response
    Response getResponse(String _id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_RESPONSES, new String[] {
        		KEY_ID, KEY_TEXT, KEY_TIME, KEY_YES_LINK, KEY_NO_LINK, KEY_MAYBE_LINK, KEY_ANY_LINK, KEY_NORESP_LINK}, KEY_ID + "=?",
                new String[] { _id }, null, null, null, null);

        if (cursor.moveToFirst())
        {
        	Log.d(">> Data Handler"  , "Found Response id " + _id);
        	Response response = new Response();
            response.setId(				cursor.getString(0));
            response.setText(			cursor.getString(1));
            response.setTime(			cursor.getString(2));
            response.setYesLink(		cursor.getString(3));
            response.setNoLink(			cursor.getString(4));
            response.setMaybeLink(		cursor.getString(5));
            response.setAnyLink(		cursor.getString(6));
            response.setNoResponseLink(	cursor.getString(7));
            // return response
            cursor.close();
            db.close();
            return response;
        } 
        else
        {
        	Log.d(">> Data Handler"  , "No Response with id " + _id);
        	return null; 
        }
    }
    
    
    // Getting All Players
    public List<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<Player>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Player player = new Player();
                player.setPhoneNumber(		cursor.getString(0));
                player.setName(				cursor.getString(1));
                player.setLastAnswer(		cursor.getString(2));
                player.setStoryLocation(	cursor.getString(3));
                player.setOldStoryLocation(	cursor.getString(4));
                player.setState(			cursor.getString(5));
                player.setYear(				cursor.getString(6));
                player.setWords1(			cursor.getString(7));
                player.setWords2(			cursor.getString(8));
                
                // Adding contact to list
                playerList.add(player);
            } while (cursor.moveToNext());
        }
 
        db.close();
        
        // return contact list
        return playerList;
    }
 
    
    // Getting All Responses
    public List<Response> getAllResponses() {
        List<Response> responseList = new ArrayList<Response>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RESPONSES;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Response response = new Response();
            	 response.setId(				cursor.getString(0));
                 response.setText(				cursor.getString(1));
                 response.setTime(				cursor.getString(2));
                 response.setYesLink(			cursor.getString(3));
                 response.setNoLink(			cursor.getString(4));
                 response.setMaybeLink(			cursor.getString(5));
                 response.setAnyLink(			cursor.getString(6));
                 response.setNoResponseLink(	cursor.getString(7));
                 // adding response to list
                responseList.add(response);
            } while (cursor.moveToNext());
        }
 
        db.close();
        
        // return contact list
        return responseList;
    }
    // Updating single contact
    public int updatePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_PH_NO, player.getPhoneNumber()); // Player Phone
        values.put(KEY_NAME, player.getName()); // Player Name
        values.put(KEY_LASTANSWER, player.getLastAnswer());
        values.put(KEY_STORYLOCATION, player.getStoryLocation());
        values.put(KEY_OLDSTORYLOCATION, player.getOldStoryLocation());
        values.put(KEY_STATE, player.getState());
        values.put(KEY_YEAR, player.getYear());
        values.put(KEY_WORDS1, player.getWords1());
        values.put(KEY_WORDS2, player.getWords2());
        
        int dU = db.update(TABLE_CONTACTS, values, KEY_PH_NO + " = ?",
                new String[] { player.getPhoneNumber() });
        
        db.close();
        
        // updating row
        return dU;
       
    }
 
    // Deleting single player
    public void deletePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_PH_NO + " = ?",
                new String[] { player.getPhoneNumber() });
        db.close();
    }
 
    // Getting contacts player
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }
    
    public void ClearPlayerTable()
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Log.d(">> Data Handler"  , "Clearing Player Table");
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
    	
    	String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
    			+ KEY_PH_NO + 			" TEXT,"
    			+ KEY_NAME  +			" TEXT,"
                + KEY_LASTANSWER + 		" TEXT,"
                + KEY_STORYLOCATION + 	" TEXT,"
                + KEY_OLDSTORYLOCATION + " TEXT,"
    			+ KEY_STATE + 			" TEXT,"
    			+ KEY_YEAR + 			" TEXT,"
    			+ KEY_WORDS1 + 			" TEXT,"
    			+ KEY_WORDS2 + 			" TEXT"
    			+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.close();
    }
    
    public void ClearResponseTable()
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Log.d(">> Data Handler"  , "Clearing Response Table");
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);
    	
    	String CREATE_RESPONSE_TABLE = "CREATE TABLE " + TABLE_RESPONSES + "("
    			+ KEY_ID + 				" TEXT,"
    			+ KEY_TEXT  +			" TEXT,"
                + KEY_TIME  +			" TEXT,"
    			+ KEY_YES_LINK + 		" TEXT,"
    			+ KEY_NO_LINK + 		" TEXT,"
    			+ KEY_MAYBE_LINK + 		" TEXT,"
    			+ KEY_ANY_LINK + 		" TEXT,"
    			+ KEY_NORESP_LINK + 	" TEXT"
    			+ ")";
        db.execSQL(CREATE_RESPONSE_TABLE);
        db.close();
    }
    
    
    
    /*
    Player getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Player player = new Player(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        cursor.close();
        db.close();
        return player;
    }
    */
    
 
}