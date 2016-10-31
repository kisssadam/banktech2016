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
public class CallBeforeRefillException extends Exception {

    /**
     * Creates a new instance of <code>CallBeforeRefillException</code> without
     * detail message.
     */
    public CallBeforeRefillException() {
    }

    /**
     * Constructs an instance of <code>CallBeforeRefillException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CallBeforeRefillException(String msg) {
        super(msg);
    }
}
