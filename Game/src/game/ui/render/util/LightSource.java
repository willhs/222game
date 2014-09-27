package game.ui.render.util;

import game.world.dimensions.Vector3D;

public class LightSource {
	private float intensity;
	private Vector3D direction;

	public LightSource(float intensity, Vector3D direction){
		this.intensity = intensity;
		this.direction = direction;
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
}
