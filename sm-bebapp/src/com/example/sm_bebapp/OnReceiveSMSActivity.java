package com.example.sm_bebapp;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

 
public class OnReceiveSMSActivity extends Activity {
	
	String phoneNumber, message;
	DatabaseHandler db; 
	TextParser textParser; 
	ResponseTreeBuilder rtBuilder; 
	
	private static final String COMMAND_PREFIX 			= "CMD"+" ";
	private static final String PLAYERSTATE_ACTIVE 		= "active";
	private static final String PLAYERSTATE_PAUSED 		= "paused";
   
	private static final String pauseList    = "(stop|piss off|go away|turn off|leave me|pause)";
	private static final String unPauseList  = "(unpause|resume|go|start|un pause|continue)"; 
	private static final String restartList  = "(restart|re start|start over|startover|BEB|new game|newgame)"; 
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        db = new DatabaseHandler(this);
        textParser = new TextParser(db);
        rtBuilder = new ResponseTreeBuilder(db);
        
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        message = getIntent().getExtras().getString("message");
        
        Log.d(">> NEW MESSAGE"  , "From " + phoneNumber + " : \"" + message +"\"");

        Player player = null; 
        if(CheckAdminCommand(phoneNumber,message) ==  false)//Check if admin command and execute command if it is. Continue otherwise
        {
        	player = GetPlayer(phoneNumber); //get the player. returns null if player was not found. 
        	if(player == null)// if player was not found 
        	{
        		if(textParser.ParseWord("BEB", message))//and the incoming message it BEB  
        		{
        			AddNewPlayer(phoneNumber, message); //add the player. 
        		}
        	}
        	else 
        	{
        		if(CheckPlayerCommand(phoneNumber,message,player) == false)//check if player wants to execute command. if not update the player. 
        		{
        			UpdatePlayerState(phoneNumber, message, player);
        		}
        	}
        }
        
       
    	// Reading all contacts
        printAllPlayers();
        //printAllResponses(); 
        finish();
    }
    
    //------ Checks the message for an admin command. If found it executes it and returns true. Otherwise it returns false. 
    public boolean CheckAdminCommand(String _phoneNumber, String _message)
    {
    	String[] args = FormatCommand(_message);//splits the message on - to get the args
    	
    	if(CheckCommand("delete all players", args[0]))
        {//--Clear All Players from the Player table
    		AdminCMD_DeleteAllPlayers(_phoneNumber,args); 
         	return true; 
        }
    	else if(CheckCommand("delete player", args[0]))
        {//--Delete a player 
    		AdminCMD_DeletePlayer(_phoneNumber,args); 
    		return true; 	
        }
    	else if(CheckCommand("show all players", args[0]))
        {//--Show All PLayers 
    		AdminCMD_ShowAllPlayers(_phoneNumber,args); 
    		return true; 
        }
    	else if(CheckCommand("show player", args[0]))
        {//--Show a PLayer 
    		AdminCMD_ShowPLayer(_phoneNumber,args); 
    		return true; 
        }
    	else if(CheckCommand("move player", args[0]))
        {//--Show a PLayer 
    		AdminCMD_MovePLayer(_phoneNumber,args); 
    		return true; 
        }
    	else if(CheckCommand("build response table", args[0]))
        {//--Build All Responses 
    		AdminCMD_BuildResponseTable(_phoneNumber,args); 
    		return true;
        }
    	else if(CheckCommand("show response table", args[0]))
        {//--Show All Responses 
    		AdminCMD_ShowResponseTable(_phoneNumber,args); 
    		return true; 
        }
    	else if(CheckCommand("set all player state", args[0]))
        {//--Show All Responses 
    		AdminCMD_ToggleAllPlayerPause(_phoneNumber,args); 
    		return true; 
        }
    	else if (CheckCommand(" ", args[0]))
    	{//--if message was a command but could not be understood. 
    		SendOutSMS(phoneNumber, "BEB> Bad Command. Try Again.");
    		return true; 
    	}
    	else
    	{//-- message was not a command. 
    		return false;
    	}
    }
    //------ Compares the message to the given command string and returns true if the message contains the command. ei "CMD some command" == "CMD some command -arg1 -arg2"
    public boolean CheckCommand(String _command, String _message)
  	{
  		String command = COMMAND_PREFIX + _command;
  		command = command.toLowerCase(Locale.getDefault()); 
  		if (_message.toLowerCase(Locale.getDefault()).startsWith(command))
  		{
  			return true;
  		}
  		return false; 
  	}
    //------ Splits a message formatted into commands into an array of arguments. "CMD some command -arg1 -arg2" -> [CMD some command][arg1][arg2]"
    public String[] FormatCommand(String _message)
    {
      String[] args = _message.split(" -");
      return args;   	
    }
    //------ Admin Comands 
    public void AdminCMD_DeleteAllPlayers(String _phoneNumber,  String[] args)
    {
    	db.ClearPlayerTable(); 	
     	SendOutSMS(_phoneNumber, "BEB> Player table cleared"); 
    }
    public void AdminCMD_DeletePlayer(String _phoneNumber,  String[] args)
    {
    	String phoneNumber =""; 
		Boolean DeletePlayer = false; 	
		if(args.length >= 2)
		{
			if(textParser.ParseWord("me", args[1]))
			{
				phoneNumber = textParser.FormatPhoneNumber(_phoneNumber); 
				DeletePlayer = true; 
			}
			else 
			{
				phoneNumber = textParser.FormatPhoneNumber(args[1]);
				DeletePlayer = true; 
			}
		}
		else 
		{
			SendOutSMS(_phoneNumber, "BEB> ERROR \r\n You must specify a phone number for player.");
			DeletePlayer = false;
		}
		
		if(DeletePlayer)
		{
    		Player player = null;
    	    player = db.getPlayer(phoneNumber); //find the player
    	    if(player == null) //If the player does not exist
    	    {
    	    	SendOutSMS(_phoneNumber, "BEB> Player ["+phoneNumber+"] not in the player list");
    	    }
    	    else  // otherwise remove the player. 
    	    {
    	        db.deletePlayer(player);
    	        SendOutSMS(_phoneNumber, "BEB> Player ["+phoneNumber+"] removed from the player list.");
    	    }
		}
    }
    public void AdminCMD_ShowAllPlayers(String _phoneNumber,  String[] args)
    {
    	SendOutSMS(_phoneNumber, "BEB> PLAYER TABLE \r\n" + getAllPlayers());
    }
    public void AdminCMD_ShowPLayer(String _phoneNumber,  String[] args)
    {
    	String phoneNumber =""; 
		Boolean ShowPlayer = false; 
		
		if(args.length >= 2)
		{
			if(textParser.ParseWord("me", args[1])) //if "me" is used for player number it will use your number
			{
				phoneNumber = textParser.FormatPhoneNumber(_phoneNumber); 
				ShowPlayer = true; 
			}
			else 
			{
				phoneNumber = textParser.FormatPhoneNumber(args[1]);
				ShowPlayer = true; 
			}
		}
		else 
		{
			SendOutSMS(_phoneNumber, "BEB> ERROR \r\n You must specify a phone number for player.");
			ShowPlayer = false;
		}
		
		if(ShowPlayer)
		{
    		Player player = null;
    	    player = db.getPlayer(phoneNumber); //find the player
    	    if(player == null) //If the player does not exist
    	    {
    	    	SendOutSMS(_phoneNumber, "BEB> Player ["+phoneNumber+"] not in the player list.");
    	    }
    	    else  // otherwise print the player. 
    	    {
    	        SendOutSMS(_phoneNumber, player.toShortString());
    	    }
		}
    }
    public void AdminCMD_MovePLayer(String _phoneNumber,  String[] args)
    {
    	String playerNumber =""; 
		Boolean MovePlayer = false; 
		
		if(args.length >= 3)
		{
			if(textParser.ParseWord("me", args[1])) //if "me" is used for player number it will use your number
			{
				playerNumber = textParser.FormatPhoneNumber(_phoneNumber); 
				MovePlayer = true; 
			}
			else 
			{
				playerNumber = textParser.FormatPhoneNumber(args[1]);
				MovePlayer = true; 
			}
		}
		else 
		{
			SendOutSMS(_phoneNumber, "BEB> ERROR \r\n You must specify a phone number for player and id of response you want to move to.");
			MovePlayer = false;
		}
		
		if(MovePlayer)
		{
    		Player player = null;
    	    Response response = null;
    		player = db.getPlayer(playerNumber); //find the player
    		response = db.getResponse(args[2].trim());
    	    if(player == null) //If the player does not exist
    	    {
    	    	SendOutSMS(_phoneNumber, "BEB> Player ["+playerNumber+"] not in the player list.");
    	    }
    	    else if (response == null)
    	    {
    	    	SendOutSMS(_phoneNumber, "BEB> Response ["+args[2]+"] not in the response table");
    	    }
    	    else  // otherwise print the player. 
    	    {
    	        player.setStoryLocation(args[2]);
    	        db.updatePlayer(player);
    	    	SendOutSMS(_phoneNumber, "BEB> Player ["+playerNumber+"] moved to  ["+args[2]+"]");
    	        
    	    }
		}
    }
    public void AdminCMD_BuildResponseTable(String _phoneNumber,  String[] args)
    {
    	db.ClearResponseTable();
    	if(args.length >= 2)
		{
	    	if(rtBuilder.PopulateResponses(args[1]))
			{
	    		
	    		printAllResponses();
	    		SendOutSMS(_phoneNumber, "BEB> Response table Built"); 
			}
			else 
			{
				SendOutSMS(_phoneNumber, "BEB> ERROR \r\n "+args[1]+"not correct id for a table. Try \"beb\" or \"test\"");	
			}
		}
    	else
    	{
    		SendOutSMS(_phoneNumber, "BEB> ERROR \r\n Table id must be specified. Try \"beb\" or \"test\" ");	
    	}
    }
    public void AdminCMD_ShowResponseTable(String _phoneNumber,  String[] args)
    {
    	printAllResponses(); 
		//SendOutSMS(_phoneNumber, "BEB> PLAYER TABLE \r\n" + getAllResponses());
    }
    public void AdminCMD_ToggleAllPlayerPause(String _phoneNumber,  String[] args)
    {
    	
    	List<Player> players = db.getAllPlayers();       
    	
        for (Player p : players) 
        {
        	if(args[1].equals("pause"))
        	{
        		p.setState(PLAYERSTATE_PAUSED );
        		db.updatePlayer(p);
        	}
        	else if(args[1].equals("active"))
        	{
        		p.setState(PLAYERSTATE_ACTIVE);
        		db.updatePlayer(p);
        	}
        	
        }
		SendOutSMS(_phoneNumber, "BEB> All PLayers set to "+args[1]);
    }
    
    
    //------ Gets the player with specified number from the database. returns null if one is not found. 
    public Player GetPlayer(String _phoneNumber)
    {
    	Player player = null;
    	player = db.getPlayer(_phoneNumber); //find the player
    	return player; 
    }
    
    //------ Adds a new player to the database
    public void AddNewPlayer(String _phoneNumber, String _message)
    {
    	SendOutSMS(_phoneNumber, 	"Welcome to BEB");
    	Player player = new Player();
        player.setName(				 	":;:none:;:");
        player.setPhoneNumber(			_phoneNumber);
        player.setLastAnswer(			":;:none:;:");
        player.setStoryLocation(				"-1");
        player.setOldStoryLocation(				"-1");
        player.setYear(						"");
        player.setWords1(					"");
        player.setWords2(					"");
        player.setState(		   "active");
        Log.d(">> NEW PLAYER ", _phoneNumber); 
        db.addPlayer(player); 
    }
    
    //------ Checks the message for a Player command. If found it executes it and returns true. Otherwise it returns false. 
    public boolean CheckPlayerCommand(String _phoneNumber, String _message, Player _player)
    {
    	if(CheckIfPlayerPausedGame(_player, _message))
		{//--Player pausing the game
    		PlayerCMD_SetPaused(_player); 
    		return true; 
		}
		else if(CheckIfPlayerUnPausedGame(_player, _message))
		{//--Player resuming the game if already paused. 
			PlayerCMD_SetUnPausedGame(_player); 
			return true; 
		}
		else if (CheckIfPlayerRestartsGame(_player, _message))
		{//-- Player restarting the game. 
			PlayerCMD_Restart(_player); 
			return true; 
		}
		else 
		{
			return false;
		}
    }
    //------ Checks for Player Commands
    public boolean CheckIfPlayerPausedGame(Player _player, String _message)
    {
    	if(textParser.ParseWord(pauseList, _message) )
    	{
    		return true; 
    	}
    	return false; 
    }
    public boolean CheckIfPlayerUnPausedGame(Player _player, String _message)
    {
    	if(textParser.ParseWord(unPauseList, _message) && !(_player.getState().equals(PLAYERSTATE_ACTIVE)) )
    	{
    		return true; 
    	}
    	return false; 
    }
    public boolean CheckIfPlayerRestartsGame(Player _player, String _message)
    {
    		if(textParser.ParseWord(restartList, _message))
    		{
    			return true; 
    		}
    		else 
    		{
    			return false; 
    		}
    		
    }
    	//------ Player Commands
    //------ Player Commands
    public void PlayerCMD_SetPaused(Player _player)
    {
    	_player.setState(PLAYERSTATE_PAUSED);
		db.updatePlayer(_player);
		Log.d(">> RESPONSE"  , "Player " + _player.getPhoneNumber() + " has paused the game.");
		SendOutSMS(_player.getPhoneNumber(), "BEB> Game Paused. You can resume at any time by texting \"Unpause\" to unpause or \"Restart\" to restart");
    }
    public void PlayerCMD_SetUnPausedGame(Player _player)
    {
    	_player.setState(PLAYERSTATE_ACTIVE);
		_player.setStoryLocation(_player.getOldStoryLocation());
		db.updatePlayer(_player);
		Log.d(">> RESPONSE"  , "Player " + _player.getPhoneNumber() + " has resumed the game.");
		SendOutSMS(_player.getPhoneNumber(), "BEB> Game Resumed");
		//return "BEB> Game Resumed:\r\n" + ExicuteResponseTree(_player, _player.getLastAnswer());
    }
    public void PlayerCMD_Restart(Player _player)
    {
    	if(_player.getState().equals(PLAYERSTATE_PAUSED))
		{
			_player.setState(PLAYERSTATE_ACTIVE);
			_player.setStoryLocation("-1"); //Is this needed? 
			db.updatePlayer(_player);
			Log.d(">> RESPONSE"  , "Player " + _player.getPhoneNumber() + " has restarted the game.");
			SendOutSMS(_player.getPhoneNumber(), "BEB> Game Restarted");
			//return "BEB> Game Restarted:\r\n" + ExicuteResponseTree(_player, _player.getLastAnswer());
		}
		else
		{
			_player.setState(PLAYERSTATE_PAUSED);
			db.updatePlayer(_player);
			SendOutSMS(_player.getPhoneNumber(), "BEB> Are you sure you want to restart the game? The Game is paused. You can resume at any time by texting \"Unpause\" to unpause or \"Restart\" to restart.");
		}
    }
    
    //------ Stores the message in the player's last message position. 
    public void UpdatePlayerState(String _phoneNumber, String _message, Player _player)
    {
    	if(_player.getState().equals(PLAYERSTATE_ACTIVE))
    	{
    		if(_player.getLastAnswer().equals(":;:none:;:"))//If player hasn't answered yes set last answer to message. 
    		{
    			_player.setLastAnswer(_message); 
    		}
    		else //Otherwise add the message to the last message. ":;:" separate messages so they can be parsed separatly. 
    		{
    			_player.setLastAnswer(_player.getLastAnswer() + ":;:" + _message); 
    		}
    		db.updatePlayer(_player); 
    		Log.d(">> PLAYER UPDATE"  , _player.getPhoneNumber() + " : \"" + _player.getLastAnswer() +"\"");
    	}
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
    
    
    /*
    public void ParseCommands(String _phoneNumber, String _message) 
    {
    	
    	String[] args = ParseCommand(_message);//splits the message on - to get the args
    	
    	//-------Clear All Players from the Player table
    	if(CheckCommand("delete all players", args[0]))
        {
    		db.ClearPlayerTable(); 	
         	SendOutSMS(_phoneNumber, "BEB> Player table cleared"); 
         	
        }
    	//-------Delete a player 
    	else if(CheckCommand("delete player", args[0]))
        {
    
    		String phoneNumber =""; 
    		Boolean DeletePlayer = false; 
    		
    		if(args.length >= 2)
    		{
    			if(textParser.ParseWord("me", args[1]))
    			{
    				phoneNumber = textParser.ParseNumber(_phoneNumber); 
    				DeletePlayer = true; 
    			}
    			else 
    			{
    				phoneNumber = textParser.ParseNumber(args[1]);
    				DeletePlayer = true; 
    			}
    		}
    		else 
    		{
    			SendOutSMS(_phoneNumber, "BEB> ERROR \r\n You must specify a phone number for player.");
    			DeletePlayer = false; 
    		}
    		
    		if(DeletePlayer)
    		{
	    		Player player = null;
	    	    player = db.getPlayer(phoneNumber); //find the player
	    	    if(player == null) //If the player does not exist
	    	    {
	    	    	SendOutSMS(_phoneNumber, "BEB> Player ["+phoneNumber+"] not in the player list");
	    	    }
	    	    else  // otherwise remove the player. 
	    	    {
	    	        db.deletePlayer(player);
	    	        SendOutSMS(_phoneNumber, "BEB> Player ["+phoneNumber+"] removed from the player list.");
	    	    }
    		}
        }
    	//-------Show All PLayers 
    	else if(CheckCommand("show all players", args[0]))
        {
    		 SendOutSMS(phoneNumber, "BEB> PLAYER TABLE \r\n" + getAllPlayers());
    		 
        }
    	//-------Show a PLayer 
    	else if(CheckCommand("show player", args[0]))
        {
    		String phoneNumber =""; 
    		Boolean ShowPlayer = false; 
    		
    		if(args.length >= 2)
    		{
    			if(textParser.ParseWord("me", args[1])) //if "me" is used for player number it will use your number
    			{
    				phoneNumber = textParser.ParseNumber(_phoneNumber); 
    				ShowPlayer = true; 
    			}
    			else 
    			{
    				phoneNumber = textParser.ParseNumber(args[1]);
    				ShowPlayer = true; 
    			}
    		}
    		else 
    		{
    			SendOutSMS(_phoneNumber, "BEB> ERROR \r\n You must specify a phone number for player.");
    			ShowPlayer = false; 
    		}
    		
    		if(ShowPlayer)
    		{
	    		Player player = null;
	    	    player = db.getPlayer(phoneNumber); //find the player
	    	    if(player == null) //If the player does not exist
	    	    {
	    	    	SendOutSMS(_phoneNumber, "BEB> Player ["+phoneNumber+"] not in the player list.");
	    	    }
	    	    else  // otherwise print the player. 
	    	    {
	    	        SendOutSMS(_phoneNumber, player.toShortString());
	    	    }
    		}
    		 
        }
    	//-------Build All Responses 
    	else if(CheckCommand("build response table", args[0]))
        {
    		db.ClearResponseTable();
    		if(rtBuilder.PopulateResponses(args[1]))
    		{
    			SendOutSMS(phoneNumber, "BEB> Response table Built"); 
    		}
    		else 
    		{
    			SendOutSMS(phoneNumber, "BEB> ERROR \r\n "+args[1]+"not correct id for a table. Try \"beb\" or \"test\"");
    		}
    		
        }
    	//-------Show All Responses 
    	else if(CheckCommand("show response table", args[0]))
        {
    		printAllResponses(); 
    		//SendOutSMS(phoneNumber, "BEB> PLAYER TABLE \r\n" + getAllResponses());
        }
    	else 
    	{
    		
    		SendOutSMS(phoneNumber, "BEB> Bad Command. Try Again.");
    	}
	} 
    
    public boolean CheckCommand(String _word, String _message)
	{
		String word = COMMAND_PREFIX + _word;
		word = word.toLowerCase(Locale.getDefault()); 
		if (_message.toLowerCase(Locale.getDefault()).startsWith(word))
		{
			return true;
		}
		return false; 
	}
    
    //Given a command with arguments "CMD set story -[player #] -[story location] parses the arguments  after the -. arg[0] is the command. ard[n] are the vars
    public String[] ParseCommand(String _message)
    {
    	String[] args = _message.split(" -");
    	return args; 
    	
    }
    
   
    public void ParseMessage(String _phoneNumber, String _message)
    {
    	Player player = null;
    	player = db.getPlayer(_phoneNumber); //find the player
    	if(player == null)//&& _message.equals("BEB") //If the player does not exist, make a new one
    	{
    		
    		SendOutSMS(phoneNumber, "Welcome to BEB");
    		player = new Player();
            player.setName(				 "____");
            player.setPhoneNumber(	phoneNumber);
            player.setLastAnswer(		message);
            player.setStoryLocation(		"0");
            player.setOldStoryLocation(		"0");
            player.setState(		   "active");
            
            db.addPlayer(player); 
            
            
            Response firstrResponse = db.getResponse("0");
            SendOutSMS(phoneNumber, firstrResponse.getText());
            
    	}
    	else  // otherwise update last message and story
    	{
    		//Story parsing here
            String outMessage = textParser.ParseMesssage(player, _message);
            SendOutSMS(phoneNumber, outMessage);
    	}

    }
     */
   
    
   
}
