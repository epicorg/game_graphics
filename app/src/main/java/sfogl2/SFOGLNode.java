package sfogl2;

import java.util.LinkedList;

import shadow.math.SFTransform3f;

public class SFOGLNode {
	private SFTransform3f transform = new SFTransform3f();
	private SFTransform3f effectiveTransform = new SFTransform3f();
	private SFOGLNode father = null;
	//Really?
	private LinkedList<SFOGLNode> sons = new LinkedList<SFOGLNode>();

	public void update() {
		if (getFather() == null) {
			getEffectiveTransform().set(getTransform());
			sonsUpdate();
		} else {
			sonUpdate();
		}
	}

	protected void sonUpdate() {
		getEffectiveTransform().set(getFather().getEffectiveTransform());
		getEffectiveTransform().mult(this.getTransform());
		//System.out.println("Transform "+transform);
		//System.out.println("Effective Transform "+effectiveTransform);
		sonsUpdate();
	}

	private void sonsUpdate() {
		for (SFOGLNode son : getSons()) {
			son.sonUpdate();
		}
	}

	public void attach(SFOGLNode father) {
		
		if (this.getFather() != null) {		
			this.getFather().getSons().remove(this);
		}
		this.setFather(father);
		this.getFather().getSons().add(this);
		update();
	}

	public LinkedList<SFOGLNode> getSons() {
		return sons;
	}

	public void setSons(LinkedList<SFOGLNode> sons) {
		this.sons = sons;
	}

	public SFTransform3f getEffectiveTransform() {
		return effectiveTransform;
	}

	public void setEffectiveTransform(SFTransform3f effectiveTransform) {
		this.effectiveTransform = effectiveTransform;
	}

	public SFTransform3f getTransform() {
		return transform;
	}

	public void setTransform(SFTransform3f transform) {
		this.transform = transform;
	}

	public SFOGLNode getFather() {
		return father;
	}

	public void setFather(SFOGLNode father) {
		this.father = father;
	}
}
