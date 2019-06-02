package shootingame;

import java.util.Random;

public class Enemy { // �� ��ġ �ľ� �� �̵��� ���� Ŭ����
	int x;
	int y;
	int speed;
	int kind; // ����� ����

	Enemy(int x, int y, int speed, int kind) { // �� ��ǥ�� �޾� ��üȭ ��Ű�� ���� �޼ҵ�
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.kind = kind;
	}
 
	public void move() { // x��ǥ -3��ŭ �̵� ��Ű�� ��� �޼ҵ�
		y += speed;
	}

	public void move2() { // 2��° ����� ������
		y += 3;
		x += 3;
	}

	public void move3() { // 3��° ����� ������
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