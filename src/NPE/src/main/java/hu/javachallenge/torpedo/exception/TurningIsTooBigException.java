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
public class TurningIsTooBigException extends Exception {

    /**
     * Creates a new instance of <code>TurningIsTooBigException</code> without
     * detail message.
     */
    public TurningIsTooBigException() {
    }

    /**
     * Constructs an instance of <code>TurningIsTooBigException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TurningIsTooBigException(String msg) {
        super(msg);
    }
}
