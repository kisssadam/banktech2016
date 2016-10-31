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
public class GameIsInProgressException extends Exception {

    /**
     * Creates a new instance of <code>GameIsInProgress</code> without detail
     * message.
     */
    public GameIsInProgressException() {
    }

    /**
     * Constructs an instance of <code>GameIsInProgress</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GameIsInProgressException(String msg) {
        super(msg);
    }
}
