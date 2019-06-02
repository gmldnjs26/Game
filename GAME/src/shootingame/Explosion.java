package shootingame;

public class Explosion {
// 여러개의 폭발 이미지를 그리기 위해 클래스를 추가하여 객체 관리
	
	int x; // 이미지를 그릴 x좌표
	int y; // 이미지를 그릴 y좌표
	int ex_cnt; // 이미지를 순차적으로 그리기 위한 카운터
	int damage; // 이미지 종류를 구분하기 위한 변수값
	
	Explosion(int x,int y,int damage){
		this.x = x;
		this.y = y;
		this.damage = damage;
		ex_cnt = 0;
	}
	public void effect() {
		ex_cnt++ ; // 해당 메소드 호출 시 카운터를 +1 시킨다.
	}
}
