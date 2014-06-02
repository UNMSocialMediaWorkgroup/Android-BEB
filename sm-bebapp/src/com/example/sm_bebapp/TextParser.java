package com.example.sm_bebapp;

import java.util.Calendar;

import android.util.Log;


//This Class provides fucntions to parse Text 
public class TextParser {
	
	
	String yesList 		= "(yes|yeah|y|yup|yep|yuppers|true|correct|sure|of course|I suppose|Damn straight|Definitely|positive|agreed|right on|righton|groovy|si|sí|oui)"; 
	String noList 		= "(no|nope|n|nah|not|noo|negative|false|wrong|sorry|no|siento|pas|désolé)"; 
	String maybeList 	= "(maybe|sometimes|not sure|can't tell|possibly|don't know|kinda|kind of|kindof|sorta|sortof|sort of|no idea)"; 
	
	private static final String PLAYERSTATE_ACTIVE 		= "active";
	private static final String PLAYERSTATE_PAUSED 		= "paused";
	
	
	DatabaseHandler db; 
	
	public TextParser(DatabaseHandler _db)
	{
		db = _db; 
	}
	
	public String ParseMesssage(Player _player, String _message)
	{
		String storyId = _player.getStoryLocation();
		Response currentResponse = db.getResponse(storyId);
		
		if(currentResponse != null)
		{
			if (CheckTime(currentResponse))
			{
				switch (ParseYesNo( _message))
				{				
					case 0:  
						return updateAnswer( _player, _message, currentResponse, currentResponse.getNoResponseLink() , "No Response");
					case 1:  
						return updateAnswer( _player, _message, currentResponse, currentResponse.getYesLink() , "Yes");
					case 2:  
						return updateAnswer( _player, _message, currentResponse, currentResponse.getNoLink() , "No");
					case 3:  
						return updateAnswer( _player, _message, currentResponse, currentResponse.getMaybeLink() , "Maybe");
					case 4:  
						return updateAnswer( _player, _message, currentResponse, currentResponse.getAnyLink() , "Any");
					default: 
						 Log.d(">> ERROR!"  , " Could not parse Yes/No on message. Dump> Message: " + _message + " Player " + _player.toShortString());
						return "BEB> Error! could not parse Yes No. If you are seing this error something really bad happened I don't know what."; 
				}
			}
			else 
			{
				return "";// time out of range
			}
		}
		else
		{
			_player.setStoryLocation("-1");
			db.updatePlayer(_player);
			Log.d(">> ERROR!"  , " Player does not have a response Id. Dump> Message: " + _message + " Player " + _player.toShortString());
			return "BEB> Error! You are no longer on the dialog tree. Resetting location to the start";
		}
		
		
	}
	
	public boolean CheckTime(Response _response)
	{
		Calendar time = Calendar.getInstance();
		int currentHour = 100*Integer.parseInt(time.get(Calendar.HOUR_OF_DAY)+"");
		int currentMin	= Integer.parseInt(time.get(Calendar.MINUTE)+"");
		int currentTime = currentHour+currentMin;
		
		Log.d(">> TIME R"  , _response.getTime());
		String[] responseTimeArgs = _response.getTime().split(" "); //"a|700" -> [a][700] or b|900 -> [b][900]
		
		if(!_response.getTime().equals("0"))
		{
			Log.d(">> TIME "  , "ct:"+currentTime + " rt:"+responseTimeArgs[0] +"-"+ responseTimeArgs[1] +"-");
			
			if ( responseTimeArgs[0].equals("a") && currentTime > Integer.parseInt(responseTimeArgs[1])) //current time is after response time
			{
				return true;
			}
			else if (responseTimeArgs[0].equals("b") && currentTime < Integer.parseInt(responseTimeArgs[1])) //current time is before response time
			{
				return true;
			}
			else 
			{
				return false;
			}
		}
		else 
		{
			return true; //response does not have a time restriction. 
		}
		
	}
	
	//Parses the message for yes or nos or maybes
	public int ParseYesNo(String _message)
	{
		if (_message.equals(":;:none:;:"))
		{
			return 0; //"none"; 
		}
		else if (ParseWord(yesList,_message))
		{
			return 1; //"yes";
		}
		else if (ParseWord(noList,_message))
		{
			return 2; //"no";
		}
		else if (ParseWord(maybeList,_message))
		{
			return 3; //"maybe"; 
		}
		else 
		{
			return 4; //"any";
		}
	}
	
	//searches for a word or a list of words in the message. returns true or false if a word is found. Lists are formatted as (foobar|foobaz|...)
	public boolean ParseWord(String _word, String _message)
	{
		if (FormatMessage(_message).matches("(?i).*\\b"+_word+"\\b.*"))	
		{
			return true;
		}
		return false; 
	}
	
	public String updateAnswer(Player _player, String _message, Response _currentResponse,  String _responseLink, String _responseType)
	{
	    //---------- Checks to see if special player data need to be stored based on the response id. SO UGLY!! 
		StoreData(_player, _currentResponse, _message);
		
		if(_responseLink.equals("none"))
	    {
	    	_responseLink =  _currentResponse.getAnyLink();
	    }
		
		Response outResponse = db.getResponse(_responseLink);
		if(outResponse != null)
		{
			_player.setStoryLocation(outResponse.getId());
    		_player.setLastAnswer( _message);
    		_player.setOldStoryLocation(_currentResponse.getId());
			db.updatePlayer(_player);
			return  BuildResponse(_player, _message, outResponse);
			//return 		outResponse.getText(); //parse text here
		}
		else 
		{
			Log.d(">> ERROR!"  , " Response " +_currentResponse.getId()+" has no out link for " + _responseType + ". Dump> " + _currentResponse.toShortString());
			return "BEB> ERROR! Response " +_currentResponse.getId()+" has no out link for " + _responseType + ".";
		}
		
	}
	
	public void StoreData(Player _player, Response _currentResponse, String _message)
	{
		if(_currentResponse.getId().equals("END"))//If the game is over pause the player. 
		{
			_player.setState(PLAYERSTATE_PAUSED); 
			Log.d(">> PLAYER ENDED", _player.getPhoneNumber() + " ended the game. Seting to paused.");
			
		}
		else if(_currentResponse.getId().equals("1a") || _currentResponse.getId().equals("1nr") || _currentResponse.getId().equals("1m")|| _currentResponse.getId().equals("1n")|| _currentResponse.getId().equals("1b1")|| _currentResponse.getId().equals("1b2")|| _currentResponse.getId().equals("p0") )//stor the name for the name question. Hacky! I know. 
		{
			if(_message.equals(":;:none:;:"))
			{
				_player.setName(BuildName("X"));
			}
			else 
			{
				_player.setName(BuildName(_message)); 
			}
		}
		else if(_currentResponse.getId().equals("0") || _currentResponse.getId().equals("0b1") ||_currentResponse.getId().equals("0b2"))
		{
			_player.setYear(BuildName(_message));
		}
		else if(_currentResponse.getId().equals("5.75a")||_currentResponse.getId().equals("5.75nr")||_currentResponse.getId().equals("5.75b1")||_currentResponse.getId().equals("5.75b2"))
		{
			_player.setWords1(_message);
		}
		else if(_currentResponse.getId().equals("10.5a") || _currentResponse.getId().equals("10.5b1") ||_currentResponse.getId().equals("10.5b2"))
		{
			_player.setWords2(_message);
		}
		db.updatePlayer(_player);
	}
	
	//Parses the out response for the key words between ; ; and replaces them appropreatly. 
	public String BuildResponse(Player _player, String _message, Response _outResponse)
	{
		String[] resposnseArgs = _outResponse.getText().split(";");
		String response = "";
		
		for (String r : resposnseArgs) {
			if(r.equals("name"))
			{
				r = _player.getName(); 
			}
			else if(r.equals("lastanswer"))
			{
				r = FormatMessage(_message).toLowerCase(); //To lower case makes it less obvious that its the same exact thing the player typed. 
			}
			else if(r.equals("year"))
			{
				r= _player.getYear();
			}
			else if(r.equals("words1"))
			{
				if (_player.getWords1().equals(":;:none:;:"))
				{
					r = "Someone who called themselves Y who is now dead.";
				}
				else
				{
					r= "We found your texts on a phone we confiscated from someone who called themselves Y who is now dead. <br> \"" + _player.getWords1().trim()+"\"";
					if (_player.getWords2().equals(":;:none:;:"))
					{
						r += "";
					}
					else
					{
						r += "and \"" + _player.getWords2().trim()+"\"";
					}
				}
				
			}
			else if(r.equals("words2"))
			{
				if (_player.getWords2().equals(":;:none:;:"))
				{
					r = "";
				}
				else
				{
					r= "\"" + _player.getWords2().trim()+"\"";
				}
			}
			response += r;
		}
		return response; 
	}
	
	//Takes a message to be stored as a name and rebuilds it with proper case. "garruS VAKarian" => "Garrus Vakarian"
	public String BuildName(String _message)
	{
		
		
		String[] nameArgs = _message.split(" ");
		String name = ""; 
		for (String n : nameArgs) 
		{
			if (n.length() == 0)
			{
				//Empty do nothing
			}
			else if (n.length() == 1)
			{
				name += n.toUpperCase() + " "; //String of length 1
			}
			else 
			{
				name += n.substring(0,1).toUpperCase() + n.substring(1).toLowerCase() + " "; 
			}
			
		}
		Log.d(">> BUILD NAME"  , "Built new name " +_message+ " -> "+name);
		return name.trim();
	}
	
	public String FormatPhoneNumber( String _phonenumber )
	{
		String phonenumber = _phonenumber;//.split("+")[0]; //remove + 
		String[] phonenumberchunks = phonenumber.split("-");//ex: 505-573-3247
		phonenumber = "";
		for (String n : phonenumberchunks) 
		{
			 phonenumber += n; 
		}
		
		if(phonenumber.length() == 10)//ex: 15055733247
		{
			return "+1" + phonenumber; //ex: +15055733247
		}
		else if(phonenumber.length() == 11 && phonenumber.startsWith("1"))//ex: 15055733247
		{
			return "+" + phonenumber; //ex: +15055733247
		}
		else if (phonenumber.length() == 12 && phonenumber.startsWith("+"))//ex: +5055733247
		{
			return phonenumber;//ex: +5055733247
		}
		else
		{
			return "ERROR! "+phonenumber+" is not a phone number."; 
		}
		
		
	
	}
	
	//removes :;: from mesage strings and replaces them with spaces. 
	public String FormatMessage(String _message)
	{
		String[] _messageArgs = _message.split(":;:");
		
		String message = "";
		for (String m : _messageArgs) {
			message += m +" "; 
		}
		
		return message; 
	}
	
//	public String[] BuildDataArray(String _data)
//	{
//		return _data.split(":;:");
//	}

}
