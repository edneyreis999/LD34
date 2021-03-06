package com.distraction.ld34.farm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.ld34.tile.TileMap;
import com.distraction.ld34.util.Res;

public class Patch {
	
	/**
	 * starts at NORMAL.
	 * till to TILLED.
	 * water to WATERED.
	 * 
	 * can only water at TILLED
	 * can add seeds when TILLED or WATERED
	 * when patch has seed and is WATERED, eventually turns to crop
	 * harvest crop, turns back to NORMAL.
	 *
	 */
	
	public enum State {
		NORMAL(new TextureRegion(Res.i().getTexture("farmtiles"), 0, 0, 32, 32)),
		TILLED(new TextureRegion(Res.i().getTexture("farmtiles"), 32, 0, 32, 32)),
		WATERED(new TextureRegion(Res.i().getTexture("farmtiles"), 64, 0, 32, 32));
		
		public TextureRegion image;
		private State(TextureRegion image) {
			this.image = image;
		}
	};
	
	private State state;
	
	private float x;
	private float y;
	private float w;
	private float h;
	
	private Seed seed;
	private Crop crop;
	
	private TextureRegion image;
	private TextureRegion pixel;
	
	public Patch(TileMap tileMap, int row, int col) {
		state = State.NORMAL;
		int tileSize = tileMap.getTileSize();
		x = tileSize * (col + 0.5f);
		y = tileSize * (tileMap.getNumRows() - 1 - row + 0.5f);
		w = tileSize;
		h = tileSize;
		image = state.image;
		pixel = new TextureRegion(Res.i().getTexture("pixel"));
	}
	
	public boolean canTill() {
		return state == State.NORMAL && crop == null;
	}
	
	public void till() {
		if(canTill()) {
			state = State.TILLED;
			image = state.image;
		}
	}
	
	public boolean canWater() {
		return state == State.TILLED && crop == null;
	}
	
	public void water() {
		if(canWater()) {
			state = State.WATERED;
			image = state.image;
			if(seed != null) {
				seed.setWatered();
			}
		}
	}
	
	public boolean hasSeed() {
		return seed != null;
	}
	
	public State getState() {
		return state;
	}
	
	public boolean canSeed() {
		return state != State.NORMAL && seed == null && crop == null;
	}
	
	public boolean seed(Seed seed) {
		if(canSeed()) {
			this.seed = seed;
			if(state == State.WATERED) {
				seed.setWatered();
			}
			return true;
		}
		return false;
	}
	
	public boolean canCrop() {
		return seed != null;
	}
	
	private void crop() {
		if(canCrop()) {
			state = State.NORMAL;
			image = state.image;
			crop = seed.getCrop();
			seed = null;
		}
	}
	
	public boolean canHarvest() {
		return crop != null;
	}
	
	public Crop harvest() {
		Crop ret = null;
		if(canHarvest()) {
			image = state.image;
			ret = crop;
			crop = null;
		}
		return ret;
	}
	
	public void update(float dt) {
		if(seed != null) {
			seed.update(dt);
			if(seed.isFinished()) {
				crop();
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		sb.draw(image, x - w / 2, y - h / 2);
		if(seed != null) {
			seed.render(sb);
		}
		if(crop != null) {
			crop.render(sb);
		}
	}
	
	public void renderHighlight(SpriteBatch sb) {
		sb.setColor(Color.BLACK);
		sb.draw(pixel, x - w / 2, y - h / 2, w, 1);
		sb.draw(pixel, x - w / 2, y - h / 2, 1, h);
		sb.draw(pixel, x - w / 2, y + h / 2, w, 1);
		sb.draw(pixel, x + w / 2, y - h / 2, 1, h);
	}
	
}
