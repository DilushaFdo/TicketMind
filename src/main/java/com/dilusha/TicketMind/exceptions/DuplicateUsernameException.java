package com.dilusha.TicketMind.exceptions;

public class DuplicateUsernameException extends RuntimeException{

    public DuplicateUsernameException(String message){
        super(message);
    }
}
