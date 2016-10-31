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
public class TheGivenShipHasAlreadyMovedException extends Exception {

    /**
     * Creates a new instance of
     * <code>TheGivenShipHasAlreadyMovedException</code> without detail message.
     */
    public TheGivenShipHasAlreadyMovedException() {
    }

    /**
     * Constructs an instance of
     * <code>TheGivenShipHasAlreadyMovedException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public TheGivenShipHasAlreadyMovedException(String msg) {
        super(msg);
    }
}
