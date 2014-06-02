package com.example.sm_bebapp;

public class Player {
	 
    //private variables
    String name;
    String phone_number;
    String last_answer; 
    String story_location; 
    String old_story_location; 
    String state; //Are they playing, afk, or paused. 
    String year; //  :;:year:;:other responses
    String words1; 
    String words2;
    // Empty constructor
    public Player(){
 
    }
    // constructor
    public Player(String name, String _phone_number){
        this.name = name;
        this.phone_number = _phone_number;
    }
 
    // getting name
    public String getName(){
        return this.name;
    }
    // setting name
    public void setName(String _name){
        this.name = _name;
    }
 
    // getting phone number
    public String getPhoneNumber(){
        return this.phone_number;
    }
    // setting phone number
    public void setPhoneNumber(String _phone_number){
        this.phone_number = _phone_number;
    }
    
    //getting last answer
    public String getLastAnswer(){
        return this.last_answer;
    }
    // setting last answer
    public void setLastAnswer(String _last_answer){
        this.last_answer = _last_answer;
    }
    
    //getting story_location
    public String getStoryLocation(){
        return this.story_location;
    }
    // setting story_location
    public void setStoryLocation(String _story_location){
        this.story_location = _story_location;
    }
    
    //getting old_story_location
    public String getOldStoryLocation(){
        return this.old_story_location;
    }
    // setting story_location
    public void setOldStoryLocation(String _old_story_location){
        this.old_story_location = _old_story_location;
    }
    
    //getting last state
    public String getState(){
        return this.state;
    }
    // setting last state
    public void setState(String _state){
        this.state = _state;
    }
    
    //getting year
    public String getYear(){
        return this.year;
    }
    // setting last state
    public void setYear(String _year){
        this.year = _year;
    }
    
    //getting words1
    public String getWords1(){
        return this.words1;
    }
    // setting words1
    public void setWords1(String _words1){
        this.words1 = _words1;
    }
    //getting words2
    public String getWords2(){
        return this.words2;
    }
    // setting words2
    public void setWords2(String _words2){
        this.words2 = _words2;
    }
    
    public String toShortString()
    {
    	return getName() + " " + getPhoneNumber() + " [" + getStoryLocation() +"][" + getOldStoryLocation() +"] <"+ getState()+">\r\n\"" + getLastAnswer() + "\"  \""+getYear()+"\"  \""+getWords1()+"\"  \""+getWords2()+"\"";
    	
    }
    
}
