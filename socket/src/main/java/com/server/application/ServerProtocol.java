/*
 * Author: Adrian Machado
 * Program Name: ServerProtocol.java
 * Description: Handles communication between Server and Client
 */
package com.server.application;

public class ServerProtocol {
    public String processInput(String theInput) {
        String theOutput = null;
        if(theInput == null){//initial message
        	return "Enter G to get Download/Upload speed";
        }
        else if(theInput.equalsIgnoreCase("G")){
        	theOutput = "Processing, Enter G to continue"; 
        }
        else{//ends commubnication
        	theOutput = "Bye.";
        }
        return theOutput;
    }
}