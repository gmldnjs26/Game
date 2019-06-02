package shootingame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
// 파일 클래스 사용을 위한 임포트 추가
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import javafx.application.Platform;
import javafx.scene.paint.Color;

class MainFrame extends JFrame implements KeyListener, MouseListener {
	// 프레임 생성을 위한 JFrame 상속
	// 마우스 키보드 이벤트 처리를 위한 MouseListener, KeyListener를 상속

	int f_width;
	int f_height;

	int[] cx = { 10, 20, 50, 30, 15 }; // 배경 스크롤 속도 제어용 변수
	int by = -6660; // 전체 배경 스크롤 용 변수

	int x, y; // 캐릭터의 좌표 변수
	int player_Speed; // 유저의 캐릭터가 움직이는 속도를 조절할 변수
	int missile_Speed; // 미사일이 날라가는 속도 조절할 변수
	int fire_Speed; // 미사일 연사 속도 조절 변수
	int enemy_speed; // 적 이동 속도 설정
	int player_Status = 0; // 유저 캐릭터의 상태 체크 변수 0->평상시 1->미사일 발사 2->충돌
	int game_Score; // 게임 점수 계산
	int player_Hitpoint; // 플레이어 캐릭터의 체력
	int boss_Hitpoint;

	boolean KeyUp = false;
	boolean KeyDown = false;
	boolean KeyLeft = false;
	boolean KeyRight = false;
	boolean KeySpace = false; // 미사일 발사를 위한 키보드 스페이스키 입력 추가
	boolean KeySpace2 = true;

	int cnt; // 각종 타이밍 조절을 위해 무한루프를 카운터할 변수

	Toolkit tk = Toolkit.getDefaultToolkit();// 이미지를 불러오기 위한 툴킷
	// Image[] Player_img; // 플레이어 애니메이션 표현을 위해 이미지를 배열로 받음
	Image BackGround_img; // 배경화면 이미지
	Image[] Cloud_img; // 움직이는 배경용 이미지 배열
	Image[] Explo_img; // 폭발 이펙트용 이미지 배열
	Image[] Player_img; // 플레이어 애니메이션 용

	private Image Missile_img = new ImageIcon(getClass().getResource("/shootingame/Missile.png")).getImage(); // 미사일
																												// 이미지
	private Image Enemy1_img = new ImageIcon(getClass().getResource("/shootingame/enemy1.png")).getImage();
	private Image Enemy2_img = new ImageIcon(getClass().getResource("/shootingame/enemy2.png")).getImage();
	private Image Enemy3_img = new ImageIcon(getClass().getResource("/shootingame/enemy3.png")).getImage();
	private Image Enemy4_img = new ImageIcon(getClass().getResource("/shootingame/enemy4.png")).getImage();

	private Image Boss_img = new ImageIcon(getClass().getResource("/shootingame/boss.png")).getImage();

	private Image Missile2_img = new ImageIcon(getClass().getResource("/shootingame/Missile2.png")).getImage();
	private Image button_start = new ImageIcon(getClass().getResource("/shootingame/button_start.png")).getImage();
	private Image button_start_pressed = new ImageIcon(getClass().getResource("/shootingame/button_start_pressed.png"))
			.getImage();
	private Image button_ranking = new ImageIcon(getClass().getResource("/shootingame/button_ranking.png")).getImage();
	private Image button_ranking_pressed = new ImageIcon(
			getClass().getResource("/shootingame/button_ranking_pressed.png")).getImage();
	private Image button_exit = new ImageIcon(getClass().getResource("/shootingame/button_exit.png")).getImage();
	private Image button_exit_pressed = new ImageIcon(getClass().getResource("/shootingame/button_exit_pressed.png"))
			.getImage();
	private Image button_main = new ImageIcon(getClass().getResource("/shootingame/button_main.png")).getImage();
	private Image button_main_pressed = new ImageIcon(getClass().getResource("/shootingame/button_main_pressed.png"))
			.getImage();

	ArrayList Missile_List = new ArrayList(); // 다수의 미사일을 관리하기 위한 리스트
	ArrayList Enemy_List = new ArrayList(); // 다수의 적을 관리하기 위해 만든 리스트
	ArrayList Explosion_List = new ArrayList(); // 다수의 폭발 이펙트를 처리하기 위한 배열

	Missile ms; // 미사일 클래스 접근키
	Enemy en; // 에너미 클래스 접근키
	Explosion ex; // 폭발 이펙트용 클래스 접근키

	// 화면 전환을 위해 선언해준것들
	private boolean Ranking = false; // 랭킹화면
	private boolean InGame = false; // 게임중 화면
	private boolean gameOver = false; // 게임오버 됬을시 화면
	private boolean start_clicked = false;
	private boolean exit_clicked = false;
	private boolean ranking_clicked = false;
	private boolean main_clicked = false;
	private boolean boss = false;

	private final int btn_width = 200;
	private final int btn_height = 75;
	private final int btn_x = 360;
	private final int start_btn_y = 450;
	private final int ranking_btn_y = 550;
	private final int exit_btn_y = 650;

	MainFrame() { // 생성자
		init();
		start();

		setTitle("슈팅게임 만들기");
		setSize(f_width, f_height);
		Dimension screen = tk.getScreenSize();
		// 프레임이 윈도우에 표시될때 위치를 세팅하기위해
		// 현재 모니터의 해상도 값을 받아옵니다.
		int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
		int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
		setLocation(f_xpos, f_ypos); // 한가운데 위치 시키기 위해서
		setResizable(false);
		setVisible(true);
		createBufferStrategy(2); // 더블 버퍼를 만드는 method
	}

	public void Sound(String file, boolean Loop) {
		Clip clip;
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			if (Loop)
				clip.loop(-1);
			else
				clip.stop();
			// Loop 값이true면 사운드재생을무한반복시킵니다.
			// false면 한번만재생시킵니다.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		f_width = 900; // 기본틀
		f_height = 1050;
		Player_img = new Image[3];
		for (int i = 0; i < Player_img.length; i++) {
			Player_img[i] = new ImageIcon(getClass().getResource("/shootingame/f1_" + i + ".png")).getImage();
		}
		Cloud_img = new Image[5];
		for (int i = 0; i < Cloud_img.length; i++) // 구름을 3개 동시에 그리는데 편의상 배열로 3개를 동시에 받는다.
			Cloud_img[i] = new ImageIcon(getClass().getResource("/shootingame/" + "cloud_" + i + ".png")).getImage();

		x = f_width / 2 - (Player_img[0].getWidth(null) * 2); // 캐릭터의 최초 좌표
		y = f_height - (Player_img[0].getHeight(null) * 2);
		// 플레이어 애니메이션 표현을 위해 파일이름을
		// 넘버마다 나눠 배열로 담는다.

		BackGround_img = new ImageIcon(getClass().getResource("/shootingame/background.jpg")).getImage();

		Explo_img = new Image[5];
		for (int i = 0; i < Explo_img.length; i++)
			Explo_img[i] = new ImageIcon(getClass().getResource("/shootingame/" + "explo_" + i + ".png")).getImage();
		// 폭발 에내메이션 표현을 위해 파일이름을 넘버마다 나눠 배열로 담는다.
		// 모든 이미지는 Swing의 ImageIcon으로 받아 이미지 넓이,높이값을 바로 얻을 수 있게 한다.(ImageIcon으로 쉽게 얻을 수
		// 있다네요.)

		game_Score = 0; // 게임 스코어 초기화
		player_Hitpoint = 1000; // 최초 플레이어 체력
		player_Speed = 7; // 유저 캐릭터 움직이는 속도 설정
		missile_Speed = 11; // 미사일 움직임 속도 설정
		fire_Speed = 15; // 미사일 연사 속도 설정
		enemy_speed = 5; // 적이 날라오는 속도 설정
	}

	public void gameinit() {
		Sound("bg.wav", true);
		gameOver = false;
		boss_Hitpoint = 100;
		by = -6660;
		cnt = 0;
		x = f_width / 2 - (Player_img[0].getWidth(null) / 2); // 캐릭터의 최초 좌표
		y = f_height - (Player_img[0].getHeight(null) * 2);
		game_Score = 0;
		player_Hitpoint = 1000;
		Missile_List.clear();
		Enemy_List.clear();
		Explosion_List.clear();
	}

	public void mainInit() {
		start_clicked = false;
		exit_clicked = false;
		ranking_clicked = false;
		Ranking = false;
	}

	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 프레임 오른쪽 위에 X버튼을 눌렀을때
		// 프로그램이 정상적으로 종료하게 만들어 줍니다.
		addKeyListener(this); // 키보드 이벤트 실행(방향키 반응 가능하게)
		addMouseListener(this); // 마우스 이벤트 실행

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						KeyProcess(); // 키보드 입력처리를 하여 x,y 갱신
						MissileProcess(); // 미사일 처리
						if (boss)
							BossProcess();
						else
							EnemyProcess(); // 적 움직임 처리메소드 실행
						GameOverProcess(); // 게임오버 유무 판단 메소드
						ExplosionProcess(); // 폭파처리 메소드 실행
						Thread.sleep(20);
						cnt++;
						if (cnt == 4000) {
							boss = true;
							Enemy_List.clear();
							Missile_List.clear();
						}
						if (cnt > 6000) // 끝까지 다깸..
							InGame = false;
					}
				} catch (Exception e) {
					System.out.println("오류 내용:" + e);
				}
			}
		});
		Thread th2 = new Thread(new Runnable() { // 그림그리기 Thread
			@Override
			public void run() {
				try {
					while (true) {
						repaint(); // 갱신된 x,y값으로 이미지 새로 그리기
						Thread.sleep(20);
					}
				} catch (Exception e) {
					System.out.println("오류 내용:" + e);
				}
			}
		});
		th.start();
		th2.start();
	}

	public void MissileProcess() {
		if (KeySpace && KeySpace2) { // 플레이어 비행기 미사일
			try {
				ms = new Missile(x, y, missile_Speed, 0); // 좌표 체크하여 넘기기
				Missile_List.add(ms);
				Sound("mfire.wav", true);
				ms = new Missile(x + Player_img[0].getWidth(null) - 47, y, missile_Speed, 0);
				Missile_List.add(ms);
				KeySpace2 = false;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		try {
			for (int i = 0; i < Missile_List.size(); i++) {
				ms = (Missile) Missile_List.get(i);
				ms.move();
				if (ms.y < 20) {
					Missile_List.remove(i);
				}
				if (Crash(x, y, ms.x, ms.y, Player_img[0], Missile2_img) && ms.who >= 1) { // 적이 발사한거 플레이어 맞을 경우
					player_Hitpoint--;
					ex = new Explosion(x, y, 1);
					Explosion_List.add(ex);
					Missile_List.remove(i);
				}

				for (int k = 0; k < Enemy_List.size(); k++) {
					en = (Enemy) Enemy_List.get(k);
					if (en.kind == 6) {
						if (Crash(ms.x, ms.y, en.x, en.y, Missile_img, Boss_img) && ms.who == 0) {
							if (boss_Hitpoint > 0) {
								boss_Hitpoint--;
								Missile_List.remove(i);
								ex = new Explosion(en.x + Boss_img.getWidth(null) / 2,
										en.y + Boss_img.getHeight(null) / 2, 2);
								// 적이 위치해 있는 곳의 중심 좌표 x,y 값과 폭발 설정을 받은 값 (0또는 1)을 받습니다.
								// 폭발 설정 값 0:폭발, 1:단순 피격
								Explosion_List.add(ex);
								// 충돌 판정으로 사라진 적의 위치에 이펙트를 추가한다.
							} else {
								game_Score += 1000; // 게임 점수를 +10점
								Missile_List.remove(i);
								Enemy_List.remove(k);
							}
						}
					}
					if (Crash(ms.x, ms.y, en.x, en.y, Missile_img, Enemy1_img) && ms.who == 0) { // 내가 적비행기를 부술 경우
						Missile_List.remove(i);
						Enemy_List.remove(k);
						game_Score += 10; // 게임 점수를 +10점

						ex = new Explosion(en.x + Enemy1_img.getWidth(null) / 2, en.y + Enemy1_img.getHeight(null) / 2,
								0);
						// 적이 위치해 있는 곳의 중심 좌표 x,y 값과 폭발 설정을 받은 값 (0또는 1)을 받습니다.
						// 폭발 설정 값 0:폭발, 1:단순 피격
						Explosion_List.add(ex);
						// 충돌 판정으로 사라진 적의 위치에 이펙트를 추가한다.
						Sound("explo.wav", true);
					}

				}
			}
		} catch (

		Exception e) {
			System.out.println(e);
		}
	}

	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) { // 충돌 판별 메소드
		// 이미지 변수를 바로 받아 해당 이미지의 넓이 높이 계산 가능
		// 두 이미지의 중심사이의 거리와 두 이미지의 반반 길이 더한것 비교 겹쳐있는지.. 굿굿
		
		if (Math.abs((x1 + img1.getWidth(null) / 2) - (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2
				+ img1.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2)
						- (y2 + img2.getHeight(null) / 2)) < (img2.getHeight(null) / 2 + img1.getHeight(null) / 2)) {

			return true; // 위 값이 true면 check에 true를 전달합니다.
		} else
			return false;
	}

	public void GameOverProcess() {
		if (InGame) {
			if (player_Hitpoint <= 0) {
				Missile_List.clear();
				Enemy_List.clear();
				gameOver = true;
			}
		}
	}

	public void ExplosionProcess() {
		for (int i = 0; i < Explosion_List.size(); i++) {
			ex = (Explosion) Explosion_List.get(i);
			ex.effect(); // 이펙트 처리 추가가 발생하면 해당 메소드를 호출(이펙트 애니메이션을 나타내기위해)
		}
	}

	public void BossProcess() { // 보스 행동 처리
		if (cnt == 4000) {
			Enemy boss = new Enemy(220, 150, 1, 6);
			Enemy_List.add(boss);
		}
		if (boss_Hitpoint >= 0) {
			if (cnt % 30 == 0) {
				for (int i = 1; i <= 7; i++) {
					ms = new Missile(420, 350, missile_Speed, 2);
					ms.angle = 20 * i;
					Missile_List.add(ms);
				}
			}
			if (cnt % 50 == 0) {
				for (int i = 1; i <= 7; i++) {
					ms = new Missile(370, 350, missile_Speed, 2);
					ms.angle = 20 * i;
					Missile_List.add(ms);
				}
				for (int i = 1; i <= 7; i++) {
					ms = new Missile(470, 350, missile_Speed, 2);
					ms.angle = 20 * i;
					Missile_List.add(ms);
				}
			}
		}

	}

	public void EnemyProcess() { // 적 행동 처리 메소드
		for (int i = 0; i < Enemy_List.size(); i++) {
			en = (Enemy) (Enemy_List.get(i)); // 배열에 적이 생성되어있을때 해당되는 적을 판별
			if (en.kind == 1)
				en.move();
			else if (en.kind == 2)
				en.move2();
			else if (en.kind == 3)
				en.move3();
			else if (en.kind == 4)
				en.move();
			else if (en.kind == 5)
				en.move4(x, y); // 내 비행기 좌표받아 따라감
			if (en.y > 900) {
				ex = new Explosion(en.x + Enemy3_img.getWidth(null) / 2, en.y + Enemy3_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				if (en.kind == 4) {
					for (int r = 1; r <= 8; r++) {
						ms = new Missile(en.x, en.y, missile_Speed, 2); // 자폭 비행기 미사일
						ms.angle = 45 * r;
						Missile_List.add(ms);
					}
				}
				Enemy_List.remove(i);
			}
			if (en.x < 0 || en.x > 900)
				Enemy_List.remove(i);
			if (en.kind == 2 && cnt % 40 == 0) {
				ms = new Missile(en.x, en.y + 25, missile_Speed, 1);
				Missile_List.add(ms);
			}
			if (en.kind == 3 && cnt % 40 == 0) {
				ms = new Missile(en.x, en.y + 25, missile_Speed, 1);
				Missile_List.add(ms);
			}

			if (Crash(x, y, en.x, en.y, Player_img[0], Enemy1_img)) {
				// 플레이어와 적의 충돌을 판정하여
				// boolean값을 리턴 받아 true면 아래를 실행합니다.
				player_Hitpoint--; // 플레이어 체력을 1깍습니다.
				Enemy_List.remove(i); // 적을 제거합니다.
				game_Score += 10; // 제거된 적으로 게임스코어를 10 증가시킵니다.

				ex = new Explosion(en.x + Enemy1_img.getWidth(null) / 2, en.y + Enemy1_img.getHeight(null) / 2, 0);
				// 적이 위치해있는 곳의 중심 좌표 x,y 값과 폭발 설정을 받은 값(0 또는 1)을 받습니다.
				// 폭발 설정 값 0:폭발 1:단순 피격

				Explosion_List.add(ex);

				ex = new Explosion(x, y, 1); // 적과 부딪히면 내 비행기에도 데미지가 입는걸 표현

				Explosion_List.add(ex);

			}
		}
		if (cnt < 1200) {
			if (cnt % 300 == 0) { // 루프 카운트 300회 마다
				en = new Enemy(80, 20, enemy_speed, 1);
				Enemy_List.add(en); // 각 좌표로 적을 생성한 후 배열에 추가한다.

				en = new Enemy(250, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(420, 20, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(590, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(760, 20, enemy_speed, 1);
				Enemy_List.add(en);
			} // 기본 비행기
			if (cnt % 350 == 0) {
				en = new Enemy(400, 20, enemy_speed+1, 5);
				Enemy_List.add(en);
			} // 유도 비행기
			if (cnt % 400 == 0) {
				en = new Enemy(10, 20, enemy_speed, 2);
				Enemy_List.add(en);
				en = new Enemy(890, 20, enemy_speed, 3);
				Enemy_List.add(en);
			} // 2번째 비행기
			if (cnt % 400 == 0) {
				en = new Enemy(390, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(440, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
			} // 자폭 비행기

		} else if (cnt < 2400 && cnt >= 1400) {
			if (cnt % 100 == 0) {
				en = new Enemy(80, 20, enemy_speed, 1);
				Enemy_List.add(en); // 각 좌표로 적을 생성한 후 배열에 추가한다.

				en = new Enemy(250, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(420, 20, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(590, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(760, 20, enemy_speed, 1);
				Enemy_List.add(en);
				// 기본 비행기
			}
			if (cnt % 300 == 0) {
				en = new Enemy(10, 20, enemy_speed + 1, 2);
				Enemy_List.add(en);
				en = new Enemy(200, 20, enemy_speed + 1, 2);
				Enemy_List.add(en);
				en = new Enemy(700, 20, enemy_speed + 1, 3);
				Enemy_List.add(en);
				en = new Enemy(890, 20, enemy_speed + 1, 3);
				Enemy_List.add(en);
				// 2번째 비행기
			}
		} else if (cnt < 3600 && cnt >= 2600) {
			if (cnt % 100 == 0) {
				en = new Enemy(80, 20, enemy_speed, 1);
				Enemy_List.add(en); // 각 좌표로 적을 생성한 후 배열에 추가한다.

				en = new Enemy(250, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(420, 20, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(590, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(760, 20, enemy_speed, 1);
				Enemy_List.add(en);
				// 기본 비행기
			}
			if (cnt % 300 == 0) {
				en = new Enemy(10, 20, enemy_speed + 1, 2);
				Enemy_List.add(en);
				en = new Enemy(200, 20, enemy_speed + 1, 2);
				Enemy_List.add(en);
				en = new Enemy(700, 20, enemy_speed + 1, 3);
				Enemy_List.add(en);
				en = new Enemy(890, 20, enemy_speed + 1, 3);
				Enemy_List.add(en);
				// 2번째 비행기
			}
			if (cnt % 400 == 0) {
				en = new Enemy(390, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(440, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
			} // 자폭 비행기
		} else {
			if (cnt % 100 == 0) {
				en = new Enemy(80, 20, enemy_speed, 1);
				Enemy_List.add(en); // 각 좌표로 적을 생성한 후 배열에 추가한다.
				en = new Enemy(250, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(420, 20, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(590, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(760, 20, enemy_speed, 1);
				Enemy_List.add(en);
				// 기본 비행기
			}
			if (cnt % 250 == 0) {
				en = new Enemy(10, 20, enemy_speed + 1, 2);
				Enemy_List.add(en);
				en = new Enemy(200, 20, enemy_speed + 1, 2);
				Enemy_List.add(en);
				en = new Enemy(350, 20, enemy_speed + 1, 2);
				Enemy_List.add(en);
				en = new Enemy(580, 20, enemy_speed + 1, 3);
				Enemy_List.add(en);
				en = new Enemy(700, 20, enemy_speed + 1, 3);
				Enemy_List.add(en);
				en = new Enemy(890, 20, enemy_speed + 1, 3);
				Enemy_List.add(en);
				// 2번째 비행기
			}
			if (cnt % 400 == 0) {
				en = new Enemy(190, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(290, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(400, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(510, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(740, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(840, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
			} // 자폭 비행기
			if (cnt % 350 == 0) {
				en = new Enemy(100, 20, enemy_speed, 5);
				Enemy_List.add(en);
				en = new Enemy(300, 20, enemy_speed, 5);
				Enemy_List.add(en);
				en = new Enemy(500, 20, enemy_speed, 5);
				Enemy_List.add(en);
				en = new Enemy(800, 20, enemy_speed, 5);
				Enemy_List.add(en);
			} // 유도 비행기
		}
	}

	public void paint(Graphics g) {
		update(g);
	}

	public void update(Graphics g) {

		Graphics2D g2 = (Graphics2D) this.getBufferStrategy().getDrawGraphics();

		if (!InGame && !Ranking) { // 인게임,랭킹화면이 아닌 메인화면
			Draw_Main(g2);
		} else if (InGame) {
			if (gameOver) {
				Draw_gameOver(g2);
			} else {
				Draw_Background(g2); // 배경 이미지 그리기 메소드 실행
				Draw_Player(g2); // 캐릭터 그리기
				Draw_Enemy(g2); // 적 그리기
				Draw_Missile(g2); // 미사일 그리기
				Draw_Explosion(g2); // 폭발 이펙트 그리기 메소드 실행
				Draw_StatusText(g2); // 상태 표시 텍스트를 그리는 메소드 실행

			}
		} else if (Ranking) {
			// 랭킹 화면
		}
		this.getBufferStrategy().show(); // 버퍼에 그렸던거 화면에 옮기기
	}

	public void Draw_gameOver(Graphics2D g) {
		try {
			g.clearRect(0, 0, f_width, f_height);
			g.setFont(new Font("Default", Font.BOLD, 100));
			g.drawString("GAME OVER", 130, 300);
			g.drawString("점수 : " + game_Score, 200, 500);
			if (!main_clicked) {
				g.drawImage(button_main, 200, 700, this);
			} else {
				g.drawImage(button_main_pressed, 200, 700, this);
			}

		} catch (Exception e) {
			System.out.println("오류내용: " + e);
		}

	}

	public void Draw_Main(Graphics2D g) {
		g.clearRect(0, 0, f_width, f_height);
		g.setFont(new Font("Default", Font.BOLD, 100));
		g.drawString("MAIN", 330, 300); // 스코어 표시
		if (start_clicked) {
			g.drawImage(button_start_pressed, btn_x, start_btn_y, this);
		} else {
			g.drawImage(button_start, btn_x, start_btn_y, this);
		}
		if (ranking_clicked) {
			g.drawImage(button_ranking_pressed, btn_x, ranking_btn_y, this);
		} else {
			g.drawImage(button_ranking, btn_x, ranking_btn_y, this);
		}
		if (exit_clicked) {
			g.drawImage(button_exit_pressed, btn_x, exit_btn_y, this);
		} else {
			g.drawImage(button_exit, btn_x, exit_btn_y, this);
		}
	}

	public void Draw_Explosion(Graphics2D g) {
		// 폭발 이펙트를 그리는 부분
		for (int i = 0; i < Explosion_List.size(); i++) {
			ex = (Explosion) Explosion_List.get(i);
			// 폭발 이펙트의 존재 유무를 체크
			if (ex.damage == 0) {
				// 설정값이 0이면 폭발용 이미지 그리기
				if (ex.ex_cnt < 7) {
					g.drawImage(Explo_img[0], ex.x - Explo_img[0].getWidth(null) / 2,
							ex.y - Explo_img[0].getHeight(null) / 2, this);
				} else if (ex.ex_cnt < 14) {
					g.drawImage(Explo_img[1], ex.x - Explo_img[1].getWidth(null) / 2,
							ex.y - Explo_img[1].getHeight(null) / 2, this);
				} else if (ex.ex_cnt < 21) {
					g.drawImage(Explo_img[2], ex.x - Explo_img[2].getWidth(null) / 2,
							ex.y - Explo_img[2].getHeight(null) / 2, this);
				} else if (ex.ex_cnt >= 21) {
					Explosion_List.remove(i);
					ex.ex_cnt = 0;
				}
			} else if (ex.damage == 1) { // 설정값이 1이면 단순 피격용 이미지 그리기
				if (ex.ex_cnt < 7) {
					g.drawImage(Explo_img[0], ex.x + 60, ex.y + 8, this);
				} else if (ex.ex_cnt < 14) {
					g.drawImage(Explo_img[1], ex.x + 60, ex.y + 5, this);
				} else if (ex.ex_cnt < 21) {
					g.drawImage(Explo_img[0], ex.x + 60, ex.y + 8, this);
				} else if (ex.ex_cnt >= 21) {
					Explosion_List.remove(i);
					ex.ex_cnt = 0;
					// 단순 피격 또한 순차적으로 이미지를 그리지만
					// 구분을 위해 약간 다른 방식으로 그립니다.
				}
			} else if (ex.damage == 2) { // 설정값이 2이고 보스 피격 용
				if (ex.ex_cnt < 7) {
					g.drawImage(Explo_img[3], 200, 135, this);
				} else if (ex.ex_cnt < 14) {
					g.drawImage(Explo_img[4], 200, 135, this);
				} else if (ex.ex_cnt < 21) {
					g.drawImage(Explo_img[3], 200, 135, this);
				} else if (ex.ex_cnt >= 21) {
					Explosion_List.remove(i);
					ex.ex_cnt = 0;
				}
			}
		}
	}

	public void Draw_StatusText(Graphics2D g) { // 상태 체크용 텍스트를 그립니다.
		g.setFont(new Font("DEFAULT", Font.BOLD, 25));
		g.drawString("SCORE : " + game_Score, 700, 70); // 스코어 표시
		g.drawString("HitPoint : " + player_Hitpoint, 700, 95); // 플레이어 체력 표시
	}

	public void Draw_Background(Graphics2D g) {
		g.clearRect(0, 0, f_width, f_height); // 화면 지우기 명령
		if (by < -1500) {
			g.drawImage(BackGround_img, 0, by, this);
			by += 1; // 배경 이미지의 좌표를 점점 위로
		} else
			by = -6660;
		;

		for (int i = 0; i < cx.length; ++i) {// cx.length는 5

			if (cx[i] < 1400) {
				cx[i] += 5 + i * 3;
			} else {
				cx[i] = 0;
			}

			g.drawImage(Cloud_img[i], 50 + i * 200, cx[i], this);// ○

			// 5개의 구름 이미지를 각기 다른 속도 값으로 좌측으로 움직임.
		}
	}

	public void Draw_Player(Graphics2D g) { // 내 캐릭터 그리기
		switch (player_Status) {
		case 0:
			g.drawImage(Player_img[0], x, y, this);
			break;
		case 1:
			g.drawImage(Player_img[1], x, y, this);
			player_Status = 0;
			break;
		case 2:
			g.drawImage(Player_img[2], x, y, this);
			player_Status = 0;
			break;

		}
	}

	public void Draw_Missile(Graphics2D g) { // 미사일 그리는 메소드
		for (int i = 0; i < Missile_List.size(); i++) { // 미사일 존재 유무를 확인한다.
			ms = (Missile) (Missile_List.get(i)); // 미사일 위치값을 확인
			if (ms.who == 0)
				g.drawImage(Missile_img, ms.x + 20, ms.y, this); // 현재 좌표에 미사일 그리기
			if (ms.who >= 1)
				g.drawImage(Missile2_img, ms.x + 20, ms.y, this);
		}
	}

//	public void Draw_Boss(Graphics2D g) { // 보스 그리는 메소드
//		for (int i = 0; i < Enemy_List.size(); i++) {
//			en = (Enemy) (Enemy_List.get(i));
//			g.drawImage(Boss_img, en.x, en.y, this); // 배열에 생성된 각 적을 판별하여 이미지 그리기
//		}
//	}

	public void Draw_Enemy(Graphics2D g) { // 적 비행기 그리는 메소드
		for (int i = 0; i < Enemy_List.size(); i++) {
			if (en.kind == 1) {
				en = (Enemy) (Enemy_List.get(i));
				g.drawImage(Enemy1_img, en.x, en.y, this); // 배열에 생성된 각 적을 판별하여 이미지 그리기
			} else if (en.kind == 2) {
				en = (Enemy) (Enemy_List.get(i));
				g.drawImage(Enemy2_img, en.x, en.y, this);
			} else if (en.kind == 3) {
				en = (Enemy) (Enemy_List.get(i));
				g.drawImage(Enemy2_img, en.x, en.y, this);
			} else if (en.kind == 4) {
				en = (Enemy) (Enemy_List.get(i));
				g.drawImage(Enemy3_img, en.x, en.y, this);
			} else if (en.kind == 5) {
				en = (Enemy) (Enemy_List.get(i));
				g.drawImage(Enemy4_img, en.x, en.y, this);
			} else if (en.kind == 6) {
				en = (Enemy) (Enemy_List.get(i));
				g.drawImage(Boss_img, en.x, en.y, this);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// 키보드가 눌러졌을때 이벤트 처리하는 곳
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = true;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = true;
			break;
		case KeyEvent.VK_LEFT:
			KeyLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			KeyRight = true;
			break;
		case KeyEvent.VK_SPACE:
			KeySpace = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// 키보드가 눌러졌다가 때어졌을때 이벤트 처리하는곳
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = false;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = false;
			break;
		case KeyEvent.VK_LEFT:
			KeyLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			KeyRight = false;
			break;
		case KeyEvent.VK_SPACE:
			KeySpace = false;
			KeySpace2 = true;
			break;
		}
	}

	public void KeyProcess() {
		if (KeyUp == true) {
			y -= player_Speed;
			player_Status = 0;
		}

		if (KeyDown == true) {
			y += player_Speed;
			player_Status = 0;
		}
		if (KeyLeft == true) {
			x -= player_Speed;
			player_Status = 2;
		}
		if (KeyRight == true) {
			x += player_Speed;
			player_Status = 1;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();
		if (!Ranking && !InGame) {
			if (x >= btn_x && x <= btn_width + btn_x && y >= start_btn_y && y <= btn_height + start_btn_y) {
				start_clicked = true;
			} else if (x >= btn_x && x <= btn_width + btn_x && y >= ranking_btn_y && y <= btn_height + ranking_btn_y) {
				ranking_clicked = true;
			} else if (x >= btn_x && x <= btn_width + btn_x && y >= exit_btn_y && y <= btn_height + exit_btn_y) {
				exit_clicked = true;
			} else {
				start_clicked = false;
				ranking_clicked = false;
				exit_clicked = false;
			}
		}
		if (InGame) {
			if (gameOver) { // 게임 종료
				if (x >= 200 && x <= 400 && y >= 700 && y <= 775) { // 메인화면 버튼 클릭시
					main_clicked = true;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();
		if (!Ranking) { // 랭킹 화면이 아닐 때 (메인화면)
			if (x >= btn_x && x <= btn_width + btn_x && y >= start_btn_y && y <= btn_height + start_btn_y
					&& start_clicked) {
				// 게임 시작
				gameinit(); // 플레이어 상태 초기화
				start_clicked = false;
				InGame = true;
			} else if (x >= btn_x && x <= btn_width + btn_x && y >= ranking_btn_y && y <= btn_height + ranking_btn_y
					&& ranking_clicked) {
				// 랭킹
				new RankingFrame();
				Ranking = true;
			} else if (x >= btn_x && x <= btn_width + btn_x && y >= exit_btn_y && y <= btn_height + exit_btn_y
					&& exit_clicked) {
				// 게임 종료
				System.exit(0);
			}
			start_clicked = false;
			ranking_clicked = false;
			exit_clicked = false;
			main_clicked = false;
		}
		if (InGame) {
			if (gameOver) { // 게임 종료
				if (x >= 200 && x <= 400 && y >= 700 && y <= 775) { // 메인화면 버튼 클릭시
					InGame = false;
					gameOver = false;
					mainInit();
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

//	public int ImageWidthValue(String file) { // 이미지 넓이 크기 값 계산
//		int x = 0;
//		try {
//			File f = new File(file);
//			BufferedImage bi = ImageIO.read(f); // 받을 파일을 이미지로 읽어 들입니다.
//			x = bi.getWidth(); // 이미지의 넓이 값을 받습니다.
//			System.out.println(x);
//		} catch (Exception e) {
//		}
//		return x;
//	}
//
//	public int ImageHeightValue(String file) { // 이미지 높이 크기 값 계산
//		int y = 0;
//		try {
//			File f = new File(file);
//			BufferedImage bi = ImageIO.read(f);
//			y = bi.getHeight();
//		} catch (Exception e) {
//		}
//		return y;
//	}
}
