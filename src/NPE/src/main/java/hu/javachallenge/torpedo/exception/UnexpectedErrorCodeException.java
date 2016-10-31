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
public class UnexpectedErrorCodeException extends Exception {

	/**
	 * Creates a new instance of <code>UnexpectedErrorCodeException</code>
	 * without detail message.
	 */
	public UnexpectedErrorCodeException() {
	}

	/**
	 * Constructs an instance of <code>UnexpectedErrorCodeException</code>
	 * with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public UnexpectedErrorCodeException(String msg) {
		super(msg);
	}
}
