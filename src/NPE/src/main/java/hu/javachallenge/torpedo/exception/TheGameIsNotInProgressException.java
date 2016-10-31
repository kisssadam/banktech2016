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
public class TheGameIsNotInProgressException extends Exception {

    /**
     * Creates a new instance of <code>TheGameIsNotInProgressException</code>
     * without detail message.
     */
    public TheGameIsNotInProgressException() {
    }

    /**
     * Constructs an instance of <code>TheGameIsNotInProgressException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public TheGameIsNotInProgressException(String msg) {
        super(msg);
    }
}
