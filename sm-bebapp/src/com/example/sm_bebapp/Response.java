package com.example.sm_bebapp;

public class Response {
	
	String id = "";
	String text = ""; 
	String time = "";
	
	String yesLink = "";
	String noLink = "";
	String maybeLink = "";
	String anyLink = "";
	String noResponseLink = "";
	
	
	
	public Response()
	{
		
	}
	
	public Response(String _id, String _text)
	{
		id = _id; 
		text = _text; 
	}
	
	// getting id
    public String getId(){
        return this.id;
    }
    // setting id
    public void setId(String _id){
        this.id = _id;
    }
	
    // getting text
    public String getText(){
        return this.text;
    }
    // setting text
    public void setText(String _text){
        this.text = _text;
    }
    public String getTime()
    {
    	return this.time; 
    }
    public void setTime(String _time)
    {
    	this.time = _time;
    }
    
    
    // getting yesLink
    public String getYesLink(){
  
        return this.yesLink;	
    }
    // setting yesLink
    public void setYesLink(String _yesLink){
        this.yesLink = _yesLink;
    }
    // getting noLink
    public String getNoLink(){
    	return this.noLink;
    }
    // setting noLink
    public void setNoLink(String _noLink){
        this.noLink = _noLink;
    }
    // getting maybeLink
    public String getMaybeLink(){
    	return this.maybeLink;
    }
    // setting maybeLink
    public void setMaybeLink(String _maybeLink){
        this.maybeLink = _maybeLink;
    }
    // getting maybeLink
    public String getAnyLink(){
        return this.anyLink;
    }
    // setting anyLink
    public void setAnyLink(String _anyLink){
        this.anyLink = _anyLink;
    }
    // getting noResponseLink
    public String getNoResponseLink(){
        return this.noResponseLink;
    }
    // setting noResponseLink
    public void setNoResponseLink(String _noResponseLink){
        this.noResponseLink = _noResponseLink;
    }
    
    public String toShortString()
    {
    	return "["+id+"] ["+time+"] "+yesLink +" : " + noLink +" : "+ maybeLink + " : "+ anyLink + " : " + noResponseLink + " : " + "\"" + text +"\""; 
    }
}
