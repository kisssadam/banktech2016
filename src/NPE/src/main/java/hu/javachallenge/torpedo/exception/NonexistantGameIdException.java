/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.javachallenge.torpedo.exception;

/**
 *
 * @author Czuczi
 */
public class NonexistantGameIdException extends Exception {

    /**
     * Creates a new instance of <code>NonexistantGameId</code> without detail
     * message.
     */
    public NonexistantGameIdException() {
    }

    /**
     * Constructs an instance of <code>NonexistantGameId</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NonexistantGameIdException(String msg) {
        super(msg);
    }
}
