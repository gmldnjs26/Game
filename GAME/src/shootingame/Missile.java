package shootingame;

import java.awt.Point;

public class Missile {
	int x;
	int y;
	int angle;
	int speed;
	int who; // 누가 미사일 발사했는가

	Missile(int x, int y, int speed, int who) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.who = who;
	}

	public void move() {
		if (this.who == 0) {
			y -= speed;
		}
		else if (this.who == 1)
			y += speed;
		else {
			x += Math.cos(Math.toRadians(angle)) * speed;
			y += Math.sin(Math.toRadians(angle)) * speed;
		}
	}
}
