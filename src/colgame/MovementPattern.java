package colgame;

import javax.vecmath.*;

public class MovementPattern {
	
	//whether the pattern is looping or not
	private boolean looping;
	
	//whether the pattern is linear or not (quadratic, cubic, ...)
	private boolean linear;
	
	//(points relative to the origin)
	
	//for linear movements these are stopping points
	//for bezier curves, these are control points 
	private Vector2f[] RefPoints;
	
	//for bezier curves, the start/endpoint
	private Vector2f startPoint, endPoint;
	
	//constructor for linear patterns
	public MovementPattern(boolean looping, Vector2f[] points) {
		setLooping(looping);
		setLinear(true);
		
		setRefPoints(points);
	}
	
	//constructor for curved patterns
	public MovementPattern(boolean looping, Vector2f[] points, Vector2f endpoint) {
		setLooping(looping);
		setLinear(false);
		
		setRefPoints(points);
		setEndPoint(endpoint);
	}

	public boolean isLooping() {
		return looping;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	public boolean isLinear() {
		return linear;
	}

	public void setLinear(boolean linear) {
		this.linear = linear;
	}

	public Vector2f[] getRefPoints() {
		return RefPoints;
	}

	public void setRefPoints(Vector2f[] refPoints) {
		RefPoints = refPoints;
	}

	public Vector2f getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Vector2f endPoint) {
		this.endPoint = endPoint;
	}

	public Vector2f getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector2f startPoint) {
		this.startPoint = startPoint;
	}


}
