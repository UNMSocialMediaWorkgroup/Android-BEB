package com.example.sm_bebapp;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OnTimerActivity extends Activity {
	
	String phoneNumber, message;
	DatabaseHandler db; 
	TextParser textParser; 
	Random generator;
	private static final String PLAYERSTATE_ACTIVE 		= "active";
	private static final String PLAYERSTATE_PAUSED 		= "paused";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        db = new DatabaseHandler(this);
        textParser = new TextParser(db);
        generator = new Random();
        
        Log.d(">> TIMER"  , "Time to update players");
        UpdatePlayers();
        StartNextTimer();
        printAllPlayers();
        finish(); 
    }
    
    
    //------- loop through all players and parse their last answers and update them. 
    public void UpdatePlayers()
    {
    	Log.d(">> UPDATING PLAYERS"  , "Getting player list...");
    	String outMessage = ""; 
        List<Player> players = db.getAllPlayers(); 
        
        for (Player p : players) 
        {
        	if(p.getState().equals(PLAYERSTATE_ACTIVE))
        	{
        		if(p.getStoryLocation().equals("11a") || p.getStoryLocation().equals("22a"))
        		{
        			SendOutSMS( "+15055733247", "Player "+p.getPhoneNumber()+" as "+p.getName()+", Getting Jimmy's Address in 15 min");
        			SendOutSMS( "+17189095607", "Player "+p.getPhoneNumber()+" as "+p.getName()+", Getting Jimmy's Address in 15 min");
        			SendOutSMS( "+15055776028", "Player "+p.getPhoneNumber()+" as "+p.getName()+", Getting Jimmy's Address in 15 min");
        			//SendOutSMS( "", "Player "+p.getPhoneNumber()+" Getting Jimmy's Address in 15 min");
        		}
        		
        		outMessage = textParser.ParseMesssage(p, p.getLastAnswer());
        		
        		p.setLastAnswer(":;:none:;:"); //clear last answer
        		db.updatePlayer(p); 
        		SendOutSMS( p.getPhoneNumber(), outMessage);
        		
        		
        	}
        	
        	
        }
    	
    }
   
    //------- Start the next timer. 
    public void StartNextTimer()
    {
    	AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	Intent intent = new Intent(this, TimerReciever.class);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
    	Calendar time = Calendar.getInstance();
    	time.setTimeInMillis(System.currentTimeMillis());
    	
    	
    	int randomIndex = generator.nextInt(10);
    	time.add(Calendar.MINUTE, 13+(randomIndex-5));
    	
    	
    	//time.add(Calendar.SECOND, 45+(randomIndex-5));
    	alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    	
    	
    }

    //------- Magic send SMS function 
    public void SendOutSMS(String _phoneNumber, String _outMessage)
    {
    	 // Next Activity call
        Intent sendIntent = new Intent();
    	sendIntent.putExtra("phoneNumber", _phoneNumber);
    	sendIntent.putExtra("message", _outMessage);
        
        sendIntent.setClassName("com.example.sm_bebapp", "com.example.sm_bebapp.SendSMSActivity");
    	sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(sendIntent);
    	Log.d(">> MESSAGE OUT"  , "To " + _phoneNumber + " : \"" +_outMessage +"\"");
    }
    
     //------ Util DB calls
    public String getAllPlayers()
    {
    	List<Player> players = db.getAllPlayers();       
    	String playersString = "";
        for (Player cn : players) 
        {
        	playersString += ">" + cn.toShortString(); 
        	playersString += "\r\n"; 
        }
        return playersString;
    }
    public String getAllResponses()
    {
    	List<Response> responses = db.getAllResponses();    
    	String responseString = "";
    	for (Response r : responses) 
        {
    		responseString += ">" + r.toShortString(); 
    		responseString += "\r\n"; 
        }
        return responseString;
    } 
    public void printAllPlayers()
    {
    	Log.d(">> PLAYER_TABLE ", "Reading all players.."); 
    	List<Player> players = db.getAllPlayers();       
         for (Player p : players) 
         {// Writing Players to log
             String log = p.toShortString();
             Log.d("Player: ", log);
         }
    }
    public void printAllResponses()
    {
    	Log.d(">> RESPONSE_TABLE ", "Reading all responses..");  
    	List<Response> responses = db.getAllResponses();       
         for (Response r : responses) 
         {// Writing Responses to log
             String log = r.toShortString(); 
             Log.d("Response: ", log);
         }
    }
    
}
