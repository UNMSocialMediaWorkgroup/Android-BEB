package com.example.sm_bebapp;

import java.util.Locale;

public class ResponseTreeBuilder {

	DatabaseHandler db; 
	
	
	public ResponseTreeBuilder(DatabaseHandler _db)
	{
		db = _db; 
	}
	
	public boolean PopulateResponses(String _treeID)
    {
    	if(_treeID.toLowerCase(Locale.getDefault()).contains("beb"))
    	{
    		PopulateBEBResponses(); 
    		return true; 
    	}
    	else if(_treeID.toLowerCase(Locale.getDefault()).contains("test"))
    	{
    		PopulateTestResponses(); 
    		return true; 
    	}
    	else 
    	{
    		return false; 
    	}
    	
    
    	
    }  
    
	public void PopulateBEBResponses()
    {
		AddResponse( "-1","a 1930", "none", "none", "none", "0", "0",		"BEB> Game started");
		//AddResponse( "-1","0", "none", "none", "none", "END", "END",		"BEB> Game started");
		
		AddResponse( "0","a 1930", "none", "1n", "1m", "1a", "0b1",		"What year is it?");
    	
		AddResponse( "0b1","a 1930", "none", "1n", "1m", "1a", "0b2",		"");
		AddResponse( "0b2","a 1930", "none", "1n", "1m", "1a", "1nr",		"");
		
    	AddResponse( "1a",	"0", "none", "none", "none", "2a", "1b1",		"Perfect, my calulations are spot on. I am in great need of your help! What is your name?");
    	AddResponse( "1n",	"0", "none", "none", "none", "2a", "1b1",		"Don't be funny! I am in great need of your help! What is your name?");
    	AddResponse( "1m",	"0", "none", "none", "none", "2a", "1b1",		"Well I'm sure my calulations are spot on. I am in great need of your help! What is your name?");
    	AddResponse( "1nr",	"0", "none", "none", "none", "2a", "1b1",	 	"I am in great need of your help! What is your name?");
    	
    	AddResponse( "1b1",	"0", "none", "none", "none", "2a", "1b2",	 	"");
    	AddResponse( "1b2",	"0", "none", "none", "none", "2a", "2nr",	 	"");
    	
    	AddResponse( "2a", "0", "none", "none", "none", "2.5a", "2.5a",		"Nice to meet you ;name;. You can call me Y. ");	
    	AddResponse( "2nr","0", "none", "none", "none", "2.5a", "2.5a",		"OK, I understand you need to protect your privacy, I will call you X and you can call me Y.");
  	
    	AddResponse( "2.5a", "0", "2.75a", "2.75a", "2.75a", "2.75a", "2.75a", 		"Time is limited so I can't explain everything, but in short, I am a member of BEB. <br> We're trying to get a picture of a time before the world started fermenting.");
    		
    	
    	AddResponse( "2.75a", "0", "3y", "3n", "3n", "3n", "3n", 		";year; is a critical year in the past and I need you to help me! Can I trust you? ");
    	
    	AddResponse( "3y","0", "none", "none", "none", "4a", "4a",		"Good to hear. Quantum tunnelers do not miss. I have faith in your abilities, ;name;. <br> We all have a part to play no matter what time period we are from.");
    	AddResponse( "3n","0", "none", "none", "none", "4a", "4a",		"Not to worry. I was like you once, but quantum tunnelers do not miss.  I have faith in your abilities, ;name;. <br> We all have an important part to play no matter what time period we are from.");
    	
    	AddResponse( "4a","0", "4.5y", "4.5n", "5.5m", "4.5a", "4.5a",	"Ok if you are to help me, First I will need to grow your brain. <br> Yeast cells cluster when they grow during the making of beer, so you need to get social. <br> I'm not asking much, just that you ask two people what it is they value most? I will follow up later.");
    	
    	AddResponse( "4.5y","0", "none", "none", "none", "5a", "5a",     "Thanks! Try to do this casually without raising suspicion.");
    	AddResponse( "4.5n","0", "none", "none", "none", "5a", "5a",     "You are raising too much suspicion. Just do it casually");
    	AddResponse( "4.5m","0", "none", "none", "none", "5a", "5a",     "The fate of the future depends on you. Just do it casually");
    	AddResponse( "4.5a","0", "none", "none", "none", "5a", "5a",     "Try to do this casually without raising suspicion.");
    	
    	
    	AddResponse( "5a","0", "5.5y", "5.5n", "5.5m", "5.5a", "5.5nr",		";name;, did it go well?  I hope you learned something about values in your era.");
    	
    	AddResponse( "5.5y","0", "none", "none", "none", "5.75a", "5.75nr",		"Excellent! This information is very important. <br> We're trying to use this information to assemble a fermentation molecular sequence we can use to keep beer-drinking a viable option in our future. ");
    	AddResponse( "5.5n","0", "none", "none", "none", "5.75a", "5.75nr",		"Keep trying. This information is very important. <br> We're trying to use this information to assemble a fermentation molecular sequence we can use to keep beer-drinking a viable option in our future. ");
    	AddResponse( "5.5m","0", "none", "none", "none", "5.75a", "5.75nr",		"I hope this works. Keep trying. This information is very important. <br> We're trying to use this information to assemble a fermentation molecular sequence we can use to keep beer-drinking a viable option in our future. ");
    	AddResponse( "5.5a","0", "none", "none", "none", "5.75a", "5.75nr",		"We're trying to use this information to assemble a fermentation molecular sequence we can use to keep beer-drinking a viable option in our future. ");
    	AddResponse( "5.5nr","0", "none", "none", "none", "5.75a", "5.75nr",	"Are you there? This information is very important. <br> We're trying to use this information to assemble a fermentation molecular sequence we can use to keep beer-drinking a viable option in our future. ");
    			    	
    	AddResponse( "5.75a","0", "none", "none", "none", "6a", "5.75b1",		"Please send me one or two words that represent the values of the people you spoke to.");
    	AddResponse( "5.75nr","0", "none", "none", "none", "6a", "5.75b1",		"If you talk to anyone please send me one or two words that represent the values of the people you spoke to.");

    	AddResponse( "5.75b1","0", "none", "none", "none", "6a", "5.75b2",	"");
    	AddResponse( "5.75b2","0", "none", "none", "none", "6a", "6nr",		"");
    	
    	
    	AddResponse( "6a","0", "none", "none", "none", "7a", "7a",		";name;, you are a great help. Now I feel I can tell you about the Biomes, they are a group of beings whose purpose it is to make things split for expansion. <br> Biomes aren't exactly good or bad, but they do sometimes make things dissolve... not good for those of us who are trying to cluster! ");
    	AddResponse( "6nr","0", "none", "none", "none", "7a", "7a",		";name;, There's more work to do. Let me tell you about the Biomes, they are a group of beings whose purpose it is to make things split for expansion. <br> Biomes aren't exactly good or bad, but they do sometimes make things dissolve... not good for those of us who are trying to cluster! ");
        
    	AddResponse( "7a","0", "none", "none", "none", "8a", "8a",		"Biomes are everywhere and look like normal people.  I need you to help me find them. <br> Tonight while you are out, someone is going to say something very strange. Listen carefully for someone to say something about waste and consumption.");
    	
    	AddResponse( "8a","0", "9y", "9n", "9m", "9a", "9a",		";name;,it's Y, things are not going well. The biomes seem to be keeping us apart. The future is fermenting, drowning in its own consumption. Can you hear it?");
    	
    	AddResponse( "9y","0", "10y", "10n", "10m", "10a", "10nr",	"Good! Keep listening! <br> With one center of attention, things can group. But there seems to be so many locuses of focuses. Are the Biomes winning?");
    	AddResponse( "9n","0", "10y", "10n", "10m", "10a", "10nr",	"Keep listening!  <br> You must understand, with one center of attention, things can group. But there seems to be so many locuses of focuses. Are the Biomes winning?");
    	AddResponse( "9m","0", "10y", "10n", "10m", "10a", "10nr",	"Its hard to tell, but you will understand.  <br> With one center of attention, things can group. But there seems to be so many locuses of focuses. Are the Biomes winning?");
    	AddResponse( "9a","0", "10y", "10n", "10m", "10a", "10nr",	"With one center of attention, things can group. But there seems to be so many locuses of focuses. Are the Biomes winning?");
    	AddResponse( "9nr","0", "10y", "10n", "10m", "10a", "10nr",	"With one center of attention, things can group. I hope you are not lost in the sea of focuses. Are the Biomes winning?");
    	
    	AddResponse( "10y","0", "none", "none", "none", "10.5a", "10.5a",	"Oh God! ;name;, it's crazy over here, the biomes are everywhere. We must act soon!");
    	AddResponse( "10n","0", "none", "none", "none", "10.5a", "10.5a",	"Maybe not yet, but God, ;name;! it's crazy over here, the biomes are everywhere. We must act soon!");
    	AddResponse( "10m","0", "none", "none", "none", "10.5a", "10.5a",	"True Boimes are very clever. God, ;name;, it's crazy over here, the biomes are everywhere!");
    	AddResponse( "10a","0", "none", "none", "none", "10.5a", "10.5a",	"God, ;name;, it's crazy over here, the biomes are everywhere. We must act soon!");
    	AddResponse( "10nr","0", "none", "none", "none", "10.5a", "10.5a",	"God, ;name;, it's crazy over here, the biomes are everywhere. We must act soon!");
    		
    	AddResponse("10.5a", "0", "none", "none", "none", "11a", "10.5b1", 	"What have you heard about waste and consumption?  I need you to send me the exact words right away.");
    			
    	AddResponse("10.5b1", "0", "none", "none", "none", "11a", "10.5b2", "");
    	AddResponse("10.5b2", "0", "none", "none", "none", "11a", "11nr", 	"");
    	
    	AddResponse( "11a","0", "none", "none", "none", "12a", "12a",		"Oh ;name;, I thought I would never hear from you! You don't know how important your last text has been to our task.");
    	AddResponse( "11nr","0", "none", "none", "none", "12nr", "12nr",	"Oh ;name;, I'm so worried! You don't know how important you are to our task.  We'll make it through tonight and hope for a reply from you tomorrow");
    	
    	AddResponse( "12a","0", "none", "none", "none", "12br", "12br",		"Meet our comrades at Jimmy's No.43 to collect a reward. 43 E 7th St, New York, NY");
    	AddResponse( "12nr","0", "none", "none", "none", "12br", "12br",	"");
    	
    	AddResponse( "12br","a 1900", "none", "none", "none", "13a", "13a",		"");
    	// Day 2
    	AddResponse( "13a","a 1900", "none", "none", "none", "14a", "14nr",	";name;, it's Y, I haven't heard from you in a while, what happened?  The Biomes did more damage than I thought. <br>  Pls txt me and let me know u are alright.");
    	
    	AddResponse( "14a","0", "none", "none", "none", "15a", "15a",	"Good to hear from you ;name;. Your body can suffer but your spirit can be free.  I think we both need another beer.  It's the yeast we could do.");	
    	AddResponse( "14nr","0", "none", "none", "none", "15a", "15a",	"I don't know if you are listening ;name;, but your body can suffer but your spirit can be free.  I think we both need another beer.  It's the yeast we could do.");
    	
    	AddResponse( "15a","0", "none", "none", "none", "16a", "16a",	"Stop, feel the direction of the wind. Stand for at least a minute and feel the direction change. Sooner or later, the tanks will appear. - Y");
    	
    	AddResponse( "16a","0", "none", "none", "none", "day2", "day2",	";name; ru here?  I need to tell u who I relly am, I'm not Y, Im -       ...");
    	// Day 2.5
    	AddResponse( "day2","a 2100", "none", "none", "none", "17a", "17a",	"");
    	
    	AddResponse( "17a","0", "18y", "18a", "18a", "18a", "17.5nr",	"Hello is this the number of ;name;?");
    	
    	AddResponse( "17.5nr","0", "18y", "18a", "18a", "18a", "18nr",	"");
   
    	AddResponse( "18y","0", "19y", "19n", "19m", "19a", "19nr",		"Our name is Seed. ;words1;. Poisoned in waste. <br> We need to assess your involvement. Did you know Y's real identity?");
    	AddResponse( "18a","0", "19y", "19n", "19m", "19a", "19nr",		"Our name is Seed. ;words1;. Poisoned in waste. <br> We need to assess your involvement. Did you know Y's real identity?");
    	AddResponse( "18nr","0", "19y", "19n", "19m", "19a", "19nr",	"Our name is Seed. We found this number on a phone we confiscated from someone who called themselves Y who is now dead. Poisoned in waste. <br> We need to assess your involvement. Did you know Y's real identity?");
    	
    	AddResponse( "19y","0", "none", "none", "none", "20a", "20nr",	"Thank you for your cooperation, ;name;. Y's been talking to a lot of people. Stand by for further notification.");
    	AddResponse( "19n","0", "none", "none", "none", "20a", "20nr",	"That may be true ;name; but we question your cognition. Further assessment is need to ascertain the validity of your answer. <br> Stand by for notification.");
    	AddResponse( "19m","0", "none", "none", "none", "20a", "20nr",	"Further assessment is need to ascertain the validity of your answer. <br> Stand by for notification.");
    	AddResponse( "19a","0", "none", "none", "none", "20a", "20nr",	"Further assessment is need to ascertain the subject of your answer. <br> Stand by for notification.");
    	AddResponse( "19nr","0", "none", "none", "none", "20a", "20nr",	"You can't ignore us for ever. You will hear from us again. <br> Stand by for further notification.");
    	
    	
    	AddResponse( "20a","0", "none", "none", "none", "21a", "21a",	";name;, It's Seed from Biome. We don't believe that you were involved inanything directly.");
    	AddResponse( "20nr","0", "none", "none", "none", "21a", "21a",	";name;, It's Seed from Biome. We don't believe that you were involved in anything directly, so there's no reason to be non-cooperative.");
    	
    	AddResponse( "21a","0", "none", "none", "none", "22a", "22a",	"(We know that you are motivated by drinking local, activating your imagination and being social.) <br> You need to wake up to reality, ;name;, we're not what Y said we are. We actually help people like you break out of the prison of their minds.");
    	
    	AddResponse( "22a","0", "none", "none", "none", "23a", "23a",		"Remembrance restores possibility to the past, making whathappened incomplete and completing what never was. <br> Tonight I need you to teach someone a song from your childhood, sing it together, and ask thatperson to do the same for you. <br> Both of you call us and sing us the songs together on our voicemeil and we will distribute the information. <br> Then, meet us at Jimmy's for a toast to the future. 43 E 7th St, New York, NY");
    	
    	AddResponse( "23a","0", "END", "END", "END", "END", "END",			"");
		
    	AddResponse( "END","0", "END", "END", "END", "END", "END",			"");
    }
	
	public void PopulateTestResponses()
    {
		AddResponse( "-1","0", "none", "none", "none", "p0", "p0",		"BEB> Game started");
		
		AddResponse( "p0","0", "none", "none", "none", "p1a", "p1nr",	"What is your name?");
		
		AddResponse( "p1a","0", "p2y", "p2n", "p2m", "p2a", "p2nr", 	"Nice to meet you ;name;! Do you like pizza?");			
    	AddResponse( "p1nr","0","p2y", "p2n", "p2m", "p2a", "p2nr", 	"I shall call you X. Do you like pizza?");
    	
    	AddResponse( "p2y","0", "none", "none", "none","p3a","p3nr",	"Me Too! Glad to see another pizza fan out there. What is your favorite kind of Pizza, ;name;?");
    	AddResponse( "p2n","0", "none", "none", "none","p3a","p3nr" , 	"To bad. More for me I guess. :) ;name;, If you had to pick a type of pizza what kind would it be?");
    	AddResponse( "p2m","0", "none", "none", "none","p3a","p3nr", 	"I guess I hate some kinds too. Like ones with fish. What is your favorite kind of Pizza, ;name;?");
    	AddResponse( "p2a","0","p2y", "p2n", "p2m", "p2a", "p2nr", 		"You didn't answer my question. Do you like pizza?");
    	AddResponse( "p2nr","0","none", "none", "none", "p0", "p0", 	"Are you dead?");
    	
    	AddResponse( "p3a","0","p5y", "p5n", "p5m", "p5a", "p5nr", 		"That is cool. I too am a fan of ;lastanswer;. Pizza also has lots of cheese. Are you Lactose intolerant?" );
    	AddResponse( "p3nr","0","0", "p4", "p4", "p4", "p4", 			"I suppose you are silent because you hate me. Pizza also has lots of cheese. Are you Lactose intolerant?");
    	
    	AddResponse( "p5y","0","p6y", "p6n", "p6m", "p6a", "p6nr", 		"I feel for you, ;name;, my brother was Lactose Intolerant. And he died in the great war. Milk sucks do you agree?" );
    	AddResponse( "p5n","0","p4", "p4", "p4", "p4", "p4", 			"Good, It would be a shame if you were. I don't know if I could be freinds with you. Don't make me choose between you and Icecream. I'll do it!" );
    	AddResponse( "p5m","0","p4", "p4", "p4", "p4", "p4", 			"Well there is only one way to find out my freind! And its a long and dangerous road. May the force be with you." );
    	AddResponse( "p5a","0","p4", "p4", "p4", "p4", "p4", 			"Your rants do not concern me. I am a free spirit!! You cannot keep me down, maaaaaaaaaaaaaaaan!!" );
    	AddResponse( "p5nr","0","p4", "p4", "p4", "p4", "p4", 			"I suppose you are silent because you hate me. Do you want to start the demo over?");
    	
    	AddResponse( "p6y","0","p4", "p4", "p4", "p4", "p4", 			"Yeah, cows are only good for beef.");
    	AddResponse( "p6n","0","p4", "p4", "p4", "p4", "p4", 			"How dare you mock me! I am the queen of Alpha centari 6!");
    	AddResponse( "p6m","0","p4", "p4", "p4", "p4", "p4", 			"Just agree with me. It will be much easier that way");
    	AddResponse( "p6a","0","p4", "p4", "p4", "p4", "p4", 			";lastanswer;, is not a good attitude to have, ;name;");
    	AddResponse( "p6nr","0","p4", "p4", "p4", "p4", "p4", 			"I suppose you are silent because you hate me. Do you want to start the demo over?");
  
    	AddResponse( "p4","0", "END", "END", "END", "END", "END", 		"BEB> Demo Finished. To resume, text BEB to this number. Thank you for playing.");		
    	
    	AddResponse( "END","0", "END", "END", "END", "END", "END", "");
    			
    }
	
    public void AddResponse(String _id, String _time, String _yesLink,String _noLink,String _maybeLink, String _anyLink, String _noRespLink,String  _text)
    {
    	Response response = new Response(); 
    	response.setId(				_id);
        response.setText(			_text);
        response.setTime(			_time);
        response.setYesLink(		_yesLink);
        response.setNoLink(			_noLink);
        response.setMaybeLink(		_maybeLink);
        response.setAnyLink(		_anyLink);
        response.setNoResponseLink(	_noRespLink);
        db.addResponse(response);
    }
	
	
}
