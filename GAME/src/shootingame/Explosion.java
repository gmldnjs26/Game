package shootingame;

public class Explosion {
// �������� ���� �̹����� �׸��� ���� Ŭ������ �߰��Ͽ� ��ü ����
	
	int x; // �̹����� �׸� x��ǥ
	int y; // �̹����� �׸� y��ǥ
	int ex_cnt; // �̹����� ���������� �׸��� ���� ī����
	int damage; // �̹��� ������ �����ϱ� ���� ������
	
	Explosion(int x,int y,int damage){
		this.x = x;
		this.y = y;
		this.damage = damage;
		ex_cnt = 0;
	}
	public void effect() {
		ex_cnt++ ; // �ش� �޼ҵ� ȣ�� �� ī���͸� +1 ��Ų��.
	}
}
