package com.distraction.ld34.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	
	private TextureRegion[] images;
	private int index;
	private float interval;
	private float time;
	
	private int timesPlayed;
	
	public void setImages(TextureRegion[] images) {
		this.images = images;
		timesPlayed = 0;
	}
	
	public void setInterval(float interval) {
		this.interval = interval;
	}
	
	public void update(float dt) {
		time += dt;
		while(time >= interval) {
			time -= interval;
			index++;
			if(index == images.length) {
				index = 0;
				timesPlayed++;
			}
		}
	}
	
	public TextureRegion getImage() {
		return images[index];
	}
	
	public boolean hasPlayedOnce() {
		return timesPlayed >= 1;
	}
	
}