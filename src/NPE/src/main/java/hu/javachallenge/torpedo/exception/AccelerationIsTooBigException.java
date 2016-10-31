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
public class AccelerationIsTooBigException extends Exception {

    /**
     * Creates a new instance of <code>AccelerationIsTooBigException</code>
     * without detail message.
     */
    public AccelerationIsTooBigException() {
    }

    /**
     * Constructs an instance of <code>AccelerationIsTooBigException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AccelerationIsTooBigException(String msg) {
        super(msg);
    }
}
