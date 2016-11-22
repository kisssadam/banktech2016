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
public class MoveParameterWithDistance extends MoveParameter{
	private Double distance;

	public MoveParameterWithDistance(Double distance, double speed, double angle) {
		super(speed, angle);
		this.distance = distance;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	
}
