package game.ui.render.util;

import game.world.dimensions.Vector3D;

import java.util.Set;

public class GraphicsOptions {


	/**
	 * specified as how much to rotate the graphics pane from the default position (isometric view).
	 */
	private Vector3D viewerDirection;
	private Set<LightSource> lights;
	private float ambientLight;

	public void setViewerDirection(Vector3D viewerDirection){
		this.viewerDirection = viewerDirection;
	}
	public Vector3D getViewerDirection(){
		return viewerDirection;
	}

	public void setAmbientLight(float ambientLight){
		this.ambientLight = ambientLight;
	}
	public float getAmbientLight(){
		return ambientLight;
	}

	public void clearLightSources(){
		this.lights.clear();
	}
	public void addLightSource(LightSource light){
		this.lights.add(light);
	}


}
