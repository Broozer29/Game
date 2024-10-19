package net.riezebos.bruus.tbd.game.movement;

import java.util.Objects;

public class Point {
	private int x;
	private int y;
	
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(float x, float y){
		this.x = Math.round(x);
		this.y = Math.round(y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setX(float x){
		this.x = Math.round(x);
	}

	public void setY(float y){
		this.y = Math.round(y);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Point point = (Point) o;
		return x == point.x && y == point.y;
	}

	@Override
	public int hashCode () {
		return Objects.hash(x, y);
	}
}
