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
public class TeamIsNotInvitedException extends Exception {

    /**
     * Creates a new instance of <code>TeamIsNotInvited</code> without detail
     * message.
     */
    public TeamIsNotInvitedException() {
    }

    /**
     * Constructs an instance of <code>TeamIsNotInvited</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TeamIsNotInvitedException(String msg) {
        super(msg);
    }
}
