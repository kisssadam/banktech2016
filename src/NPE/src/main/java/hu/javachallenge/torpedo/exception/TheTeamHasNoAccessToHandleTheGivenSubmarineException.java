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
public class TheTeamHasNoAccessToHandleTheGivenSubmarineException extends Exception {

    /**
     * Creates a new instance of
     * <code>TheTeamHasNoAccessToHandleTheGivenSubmarineException</code> without
     * detail message.
     */
    public TheTeamHasNoAccessToHandleTheGivenSubmarineException() {
    }

    /**
     * Constructs an instance of
     * <code>TheTeamHasNoAccessToHandleTheGivenSubmarineException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public TheTeamHasNoAccessToHandleTheGivenSubmarineException(String msg) {
        super(msg);
    }
}
