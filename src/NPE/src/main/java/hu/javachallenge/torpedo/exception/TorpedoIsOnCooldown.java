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
public class TorpedoIsOnCooldown extends Exception {

    /**
     * Creates a new instance of <code>TorpedoIsOnCooldown</code> without detail
     * message.
     */
    public TorpedoIsOnCooldown() {
    }

    /**
     * Constructs an instance of <code>TorpedoIsOnCooldown</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TorpedoIsOnCooldown(String msg) {
        super(msg);
    }
}
