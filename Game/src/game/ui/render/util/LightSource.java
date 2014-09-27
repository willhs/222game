package game.ui.render.util;

import game.world.dimensions.Vector3D;

import java.awt.Color;

public class LightSource implements Transformable{
	private float intensity;
	private Vector3D direction;
	private Color colour;

	public LightSource(float intensity, Vector3D direction, Color colour){
		this.intensity = intensity;
		this.direction = direction;
		this.colour = colour;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public Vector3D getDirection() {
		return direction;
	}

	public void setDirection(Vector3D direction) {
		this.direction = direction;
	}

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	@Override
	public void transform(Transform transform) {
		direction = transform.multiply(direction);
	}
}
