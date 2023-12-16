package hw4;

import java.util.ArrayList;

import api.GameElement;

/*
 * An abstract class for the platform and elevator classes.
 * Holds all similar methods between the two to reduce duplicate code.
 */

public abstract class Parent extends MovingElement {
	
	private double min;
	private double max;
	private ArrayList<GameElement> childElements;

	public Parent(double x, double y, int width, int height) {
		super(x, y, width, height);
		min = Double.NEGATIVE_INFINITY;
		max = Double.POSITIVE_INFINITY;
		childElements = new ArrayList<> ();
	}
	
	public void addChild(Charm child) {
		childElements.add(child);
		child.setParent(this);
	}
	
	public void addChild(Lurker child) {
		childElements.add(child);
		child.setParent(this);
	}
	
	public void deleteMarkedChildren() {
		for(int i = 0; i < childElements.size(); i++) {
			if(childElements.get(i).isMarked()) {
				childElements.get(i).isMarked();
				childElements.remove(i);
			}
		}
	}
	
	public ArrayList<GameElement> getChildren() {
		return childElements;
	}
	
	public void setBounds(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	public double getMax() {
		return max;
	}
	
	public double getMin() {
		return min;
	}

	@Override
	public void setVelocity(double deltaX, double deltaY) {
		// TODO Auto-generated method stub
		super.setVelocity(deltaX, deltaY);
	}

	@Override
	public double getDeltaX() {
		// TODO Auto-generated method stub
		return super.getDeltaX();
	}

	@Override
	public double getDeltaY() {
		// TODO Auto-generated method stub
		return super.getDeltaY();
	}
	
	@Override
	public void update() {
		super.update();
	}

}
