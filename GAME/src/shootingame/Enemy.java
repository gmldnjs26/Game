package shootingame;

import java.util.Random;

public class Enemy { // 적 위치 파악 및 이동을 위한 클래스
	int x;
	int y;
	int speed;
	int kind; // 비행기 종류

	Enemy(int x, int y, int speed, int kind) { // 적 좌표를 받아 객체화 시키기 위한 메소드
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.kind = kind;
	}
 
	public void move() { // x좌표 -3만큼 이동 시키는 명령 메소드
		y += speed;
	}

	public void move2() { // 2번째 비행기 움직임
		y += 3;
		x += 3;
	}

	public void move3() { // 3번째 비행기 움직임
		y += 3;
		x -= 3;
	}

	public void move4(int p_x, int p_y) {
		if (((p_x - x) + (p_y - y)) > 1500) {
			x = x + (p_x - x) / 210;
			y = y + (p_y - y) / 210;
		} else if (((p_x - x) + (p_y - y)) <= 1500 & ((p_x - x) + (p_y - y)) > 800) {
			x = x + (p_x - x) / 140;
			y = y + (p_y - y) / 140;
		} else if (((p_x - x) + (p_y - y)) <= 800 && ((p_x - x) + (p_y - y)) > 300) {
			x = x + (p_x - x) / 90;
			y = y + (p_y - y) / 90;
		} else {
			x = x + (p_x - x) / 40;
			y = y + (p_y - y) / 40;
		}
	}
}