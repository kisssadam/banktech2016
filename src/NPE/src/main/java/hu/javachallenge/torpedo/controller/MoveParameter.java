/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.javachallenge.torpedo.controller;

/**
 *
 * @author Czuczi
 */
public class MoveParameter {

	private double acceleration;
	private double steering;

	public MoveParameter(double speed, double angle) {
		this.acceleration = speed;
		this.steering = angle;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getSteering() {
		return steering;
	}

	public void setSteering(double steering) {
		this.steering = steering;
	}
	
}
