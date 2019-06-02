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
// ���� Ŭ���� ����� ���� ����Ʈ �߰�
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
	// ������ ������ ���� JFrame ���
	// ���콺 Ű���� �̺�Ʈ ó���� ���� MouseListener, KeyListener�� ���

	int f_width;
	int f_height;

	int[] cx = { 10, 20, 50, 30, 15 }; // ��� ��ũ�� �ӵ� ����� ����
	int by = -6660; // ��ü ��� ��ũ�� �� ����

	int x, y; // ĳ������ ��ǥ ����
	int player_Speed; // ������ ĳ���Ͱ� �����̴� �ӵ��� ������ ����
	int missile_Speed; // �̻����� ���󰡴� �ӵ� ������ ����
	int fire_Speed; // �̻��� ���� �ӵ� ���� ����
	int enemy_speed; // �� �̵� �ӵ� ����
	int player_Status = 0; // ���� ĳ������ ���� üũ ���� 0->���� 1->�̻��� �߻� 2->�浹
	int game_Score; // ���� ���� ���
	int player_Hitpoint; // �÷��̾� ĳ������ ü��
	int boss_Hitpoint;

	boolean KeyUp = false;
	boolean KeyDown = false;
	boolean KeyLeft = false;
	boolean KeyRight = false;
	boolean KeySpace = false; // �̻��� �߻縦 ���� Ű���� �����̽�Ű �Է� �߰�
	boolean KeySpace2 = true;

	int cnt; // ���� Ÿ�̹� ������ ���� ���ѷ����� ī������ ����

	Toolkit tk = Toolkit.getDefaultToolkit();// �̹����� �ҷ����� ���� ��Ŷ
	// Image[] Player_img; // �÷��̾� �ִϸ��̼� ǥ���� ���� �̹����� �迭�� ����
	Image BackGround_img; // ���ȭ�� �̹���
	Image[] Cloud_img; // �����̴� ���� �̹��� �迭
	Image[] Explo_img; // ���� ����Ʈ�� �̹��� �迭
	Image[] Player_img; // �÷��̾� �ִϸ��̼� ��

	private Image Missile_img = new ImageIcon(getClass().getResource("/shootingame/Missile.png")).getImage(); // �̻���
																												// �̹���
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

	ArrayList Missile_List = new ArrayList(); // �ټ��� �̻����� �����ϱ� ���� ����Ʈ
	ArrayList Enemy_List = new ArrayList(); // �ټ��� ���� �����ϱ� ���� ���� ����Ʈ
	ArrayList Explosion_List = new ArrayList(); // �ټ��� ���� ����Ʈ�� ó���ϱ� ���� �迭

	Missile ms; // �̻��� Ŭ���� ����Ű
	Enemy en; // ���ʹ� Ŭ���� ����Ű
	Explosion ex; // ���� ����Ʈ�� Ŭ���� ����Ű

	// ȭ�� ��ȯ�� ���� �������ذ͵�
	private boolean Ranking = false; // ��ŷȭ��
	private boolean InGame = false; // ������ ȭ��
	private boolean gameOver = false; // ���ӿ��� ������ ȭ��
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

	MainFrame() { // ������
		init();
		start();

		setTitle("���ð��� �����");
		setSize(f_width, f_height);
		Dimension screen = tk.getScreenSize();
		// �������� �����쿡 ǥ�õɶ� ��ġ�� �����ϱ�����
		// ���� ������� �ػ� ���� �޾ƿɴϴ�.
		int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
		int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
		setLocation(f_xpos, f_ypos); // �Ѱ�� ��ġ ��Ű�� ���ؼ�
		setResizable(false);
		setVisible(true);
		createBufferStrategy(2); // ���� ���۸� ����� method
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
			// Loop ����true�� ������������ѹݺ���ŵ�ϴ�.
			// false�� �ѹ��������ŵ�ϴ�.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		f_width = 900; // �⺻Ʋ
		f_height = 1050;
		Player_img = new Image[3];
		for (int i = 0; i < Player_img.length; i++) {
			Player_img[i] = new ImageIcon(getClass().getResource("/shootingame/f1_" + i + ".png")).getImage();
		}
		Cloud_img = new Image[5];
		for (int i = 0; i < Cloud_img.length; i++) // ������ 3�� ���ÿ� �׸��µ� ���ǻ� �迭�� 3���� ���ÿ� �޴´�.
			Cloud_img[i] = new ImageIcon(getClass().getResource("/shootingame/" + "cloud_" + i + ".png")).getImage();

		x = f_width / 2 - (Player_img[0].getWidth(null) * 2); // ĳ������ ���� ��ǥ
		y = f_height - (Player_img[0].getHeight(null) * 2);
		// �÷��̾� �ִϸ��̼� ǥ���� ���� �����̸���
		// �ѹ����� ���� �迭�� ��´�.

		BackGround_img = new ImageIcon(getClass().getResource("/shootingame/background.jpg")).getImage();

		Explo_img = new Image[5];
		for (int i = 0; i < Explo_img.length; i++)
			Explo_img[i] = new ImageIcon(getClass().getResource("/shootingame/" + "explo_" + i + ".png")).getImage();
		// ���� �������̼� ǥ���� ���� �����̸��� �ѹ����� ���� �迭�� ��´�.
		// ��� �̹����� Swing�� ImageIcon���� �޾� �̹��� ����,���̰��� �ٷ� ���� �� �ְ� �Ѵ�.(ImageIcon���� ���� ���� ��
		// �ִٳ׿�.)

		game_Score = 0; // ���� ���ھ� �ʱ�ȭ
		player_Hitpoint = 1000; // ���� �÷��̾� ü��
		player_Speed = 7; // ���� ĳ���� �����̴� �ӵ� ����
		missile_Speed = 11; // �̻��� ������ �ӵ� ����
		fire_Speed = 15; // �̻��� ���� �ӵ� ����
		enemy_speed = 5; // ���� ������� �ӵ� ����
	}

	public void gameinit() {
		Sound("bg.wav", true);
		gameOver = false;
		boss_Hitpoint = 100;
		by = -6660;
		cnt = 0;
		x = f_width / 2 - (Player_img[0].getWidth(null) / 2); // ĳ������ ���� ��ǥ
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
		// ������ ������ ���� X��ư�� ��������
		// ���α׷��� ���������� �����ϰ� ����� �ݴϴ�.
		addKeyListener(this); // Ű���� �̺�Ʈ ����(����Ű ���� �����ϰ�)
		addMouseListener(this); // ���콺 �̺�Ʈ ����

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						KeyProcess(); // Ű���� �Է�ó���� �Ͽ� x,y ����
						MissileProcess(); // �̻��� ó��
						if (boss)
							BossProcess();
						else
							EnemyProcess(); // �� ������ ó���޼ҵ� ����
						GameOverProcess(); // ���ӿ��� ���� �Ǵ� �޼ҵ�
						ExplosionProcess(); // ����ó�� �޼ҵ� ����
						Thread.sleep(20);
						cnt++;
						if (cnt == 4000) {
							boss = true;
							Enemy_List.clear();
							Missile_List.clear();
						}
						if (cnt > 6000) // ������ �ٱ�..
							InGame = false;
					}
				} catch (Exception e) {
					System.out.println("���� ����:" + e);
				}
			}
		});
		Thread th2 = new Thread(new Runnable() { // �׸��׸��� Thread
			@Override
			public void run() {
				try {
					while (true) {
						repaint(); // ���ŵ� x,y������ �̹��� ���� �׸���
						Thread.sleep(20);
					}
				} catch (Exception e) {
					System.out.println("���� ����:" + e);
				}
			}
		});
		th.start();
		th2.start();
	}

	public void MissileProcess() {
		if (KeySpace && KeySpace2) { // �÷��̾� ����� �̻���
			try {
				ms = new Missile(x, y, missile_Speed, 0); // ��ǥ üũ�Ͽ� �ѱ��
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
				if (Crash(x, y, ms.x, ms.y, Player_img[0], Missile2_img) && ms.who >= 1) { // ���� �߻��Ѱ� �÷��̾� ���� ���
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
								// ���� ��ġ�� �ִ� ���� �߽� ��ǥ x,y ���� ���� ������ ���� �� (0�Ǵ� 1)�� �޽��ϴ�.
								// ���� ���� �� 0:����, 1:�ܼ� �ǰ�
								Explosion_List.add(ex);
								// �浹 �������� ����� ���� ��ġ�� ����Ʈ�� �߰��Ѵ�.
							} else {
								game_Score += 1000; // ���� ������ +10��
								Missile_List.remove(i);
								Enemy_List.remove(k);
							}
						}
					}
					if (Crash(ms.x, ms.y, en.x, en.y, Missile_img, Enemy1_img) && ms.who == 0) { // ���� ������⸦ �μ� ���
						Missile_List.remove(i);
						Enemy_List.remove(k);
						game_Score += 10; // ���� ������ +10��

						ex = new Explosion(en.x + Enemy1_img.getWidth(null) / 2, en.y + Enemy1_img.getHeight(null) / 2,
								0);
						// ���� ��ġ�� �ִ� ���� �߽� ��ǥ x,y ���� ���� ������ ���� �� (0�Ǵ� 1)�� �޽��ϴ�.
						// ���� ���� �� 0:����, 1:�ܼ� �ǰ�
						Explosion_List.add(ex);
						// �浹 �������� ����� ���� ��ġ�� ����Ʈ�� �߰��Ѵ�.
						Sound("explo.wav", true);
					}

				}
			}
		} catch (

		Exception e) {
			System.out.println(e);
		}
	}

	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) { // �浹 �Ǻ� �޼ҵ�
		// �̹��� ������ �ٷ� �޾� �ش� �̹����� ���� ���� ��� ����
		// �� �̹����� �߽ɻ����� �Ÿ��� �� �̹����� �ݹ� ���� ���Ѱ� �� �����ִ���.. �±�
		
		if (Math.abs((x1 + img1.getWidth(null) / 2) - (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2
				+ img1.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2)
						- (y2 + img2.getHeight(null) / 2)) < (img2.getHeight(null) / 2 + img1.getHeight(null) / 2)) {

			return true; // �� ���� true�� check�� true�� �����մϴ�.
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
			ex.effect(); // ����Ʈ ó�� �߰��� �߻��ϸ� �ش� �޼ҵ带 ȣ��(����Ʈ �ִϸ��̼��� ��Ÿ��������)
		}
	}

	public void BossProcess() { // ���� �ൿ ó��
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

	public void EnemyProcess() { // �� �ൿ ó�� �޼ҵ�
		for (int i = 0; i < Enemy_List.size(); i++) {
			en = (Enemy) (Enemy_List.get(i)); // �迭�� ���� �����Ǿ������� �ش�Ǵ� ���� �Ǻ�
			if (en.kind == 1)
				en.move();
			else if (en.kind == 2)
				en.move2();
			else if (en.kind == 3)
				en.move3();
			else if (en.kind == 4)
				en.move();
			else if (en.kind == 5)
				en.move4(x, y); // �� ����� ��ǥ�޾� ����
			if (en.y > 900) {
				ex = new Explosion(en.x + Enemy3_img.getWidth(null) / 2, en.y + Enemy3_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				if (en.kind == 4) {
					for (int r = 1; r <= 8; r++) {
						ms = new Missile(en.x, en.y, missile_Speed, 2); // ���� ����� �̻���
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
				// �÷��̾�� ���� �浹�� �����Ͽ�
				// boolean���� ���� �޾� true�� �Ʒ��� �����մϴ�.
				player_Hitpoint--; // �÷��̾� ü���� 1����ϴ�.
				Enemy_List.remove(i); // ���� �����մϴ�.
				game_Score += 10; // ���ŵ� ������ ���ӽ��ھ 10 ������ŵ�ϴ�.

				ex = new Explosion(en.x + Enemy1_img.getWidth(null) / 2, en.y + Enemy1_img.getHeight(null) / 2, 0);
				// ���� ��ġ���ִ� ���� �߽� ��ǥ x,y ���� ���� ������ ���� ��(0 �Ǵ� 1)�� �޽��ϴ�.
				// ���� ���� �� 0:���� 1:�ܼ� �ǰ�

				Explosion_List.add(ex);

				ex = new Explosion(x, y, 1); // ���� �ε����� �� ����⿡�� �������� �Դ°� ǥ��

				Explosion_List.add(ex);

			}
		}
		if (cnt < 1200) {
			if (cnt % 300 == 0) { // ���� ī��Ʈ 300ȸ ����
				en = new Enemy(80, 20, enemy_speed, 1);
				Enemy_List.add(en); // �� ��ǥ�� ���� ������ �� �迭�� �߰��Ѵ�.

				en = new Enemy(250, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(420, 20, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(590, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(760, 20, enemy_speed, 1);
				Enemy_List.add(en);
			} // �⺻ �����
			if (cnt % 350 == 0) {
				en = new Enemy(400, 20, enemy_speed+1, 5);
				Enemy_List.add(en);
			} // ���� �����
			if (cnt % 400 == 0) {
				en = new Enemy(10, 20, enemy_speed, 2);
				Enemy_List.add(en);
				en = new Enemy(890, 20, enemy_speed, 3);
				Enemy_List.add(en);
			} // 2��° �����
			if (cnt % 400 == 0) {
				en = new Enemy(390, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(440, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
			} // ���� �����

		} else if (cnt < 2400 && cnt >= 1400) {
			if (cnt % 100 == 0) {
				en = new Enemy(80, 20, enemy_speed, 1);
				Enemy_List.add(en); // �� ��ǥ�� ���� ������ �� �迭�� �߰��Ѵ�.

				en = new Enemy(250, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(420, 20, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(590, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(760, 20, enemy_speed, 1);
				Enemy_List.add(en);
				// �⺻ �����
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
				// 2��° �����
			}
		} else if (cnt < 3600 && cnt >= 2600) {
			if (cnt % 100 == 0) {
				en = new Enemy(80, 20, enemy_speed, 1);
				Enemy_List.add(en); // �� ��ǥ�� ���� ������ �� �迭�� �߰��Ѵ�.

				en = new Enemy(250, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(420, 20, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(590, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(760, 20, enemy_speed, 1);
				Enemy_List.add(en);
				// �⺻ �����
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
				// 2��° �����
			}
			if (cnt % 400 == 0) {
				en = new Enemy(390, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
				en = new Enemy(440, 20, enemy_speed + 2, 4);
				Enemy_List.add(en);
			} // ���� �����
		} else {
			if (cnt % 100 == 0) {
				en = new Enemy(80, 20, enemy_speed, 1);
				Enemy_List.add(en); // �� ��ǥ�� ���� ������ �� �迭�� �߰��Ѵ�.
				en = new Enemy(250, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(420, 20, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(590, 40, enemy_speed, 1);
				Enemy_List.add(en);
				en = new Enemy(760, 20, enemy_speed, 1);
				Enemy_List.add(en);
				// �⺻ �����
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
				// 2��° �����
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
			} // ���� �����
			if (cnt % 350 == 0) {
				en = new Enemy(100, 20, enemy_speed, 5);
				Enemy_List.add(en);
				en = new Enemy(300, 20, enemy_speed, 5);
				Enemy_List.add(en);
				en = new Enemy(500, 20, enemy_speed, 5);
				Enemy_List.add(en);
				en = new Enemy(800, 20, enemy_speed, 5);
				Enemy_List.add(en);
			} // ���� �����
		}
	}

	public void paint(Graphics g) {
		update(g);
	}

	public void update(Graphics g) {

		Graphics2D g2 = (Graphics2D) this.getBufferStrategy().getDrawGraphics();

		if (!InGame && !Ranking) { // �ΰ���,��ŷȭ���� �ƴ� ����ȭ��
			Draw_Main(g2);
		} else if (InGame) {
			if (gameOver) {
				Draw_gameOver(g2);
			} else {
				Draw_Background(g2); // ��� �̹��� �׸��� �޼ҵ� ����
				Draw_Player(g2); // ĳ���� �׸���
				Draw_Enemy(g2); // �� �׸���
				Draw_Missile(g2); // �̻��� �׸���
				Draw_Explosion(g2); // ���� ����Ʈ �׸��� �޼ҵ� ����
				Draw_StatusText(g2); // ���� ǥ�� �ؽ�Ʈ�� �׸��� �޼ҵ� ����

			}
		} else if (Ranking) {
			// ��ŷ ȭ��
		}
		this.getBufferStrategy().show(); // ���ۿ� �׷ȴ��� ȭ�鿡 �ű��
	}

	public void Draw_gameOver(Graphics2D g) {
		try {
			g.clearRect(0, 0, f_width, f_height);
			g.setFont(new Font("Default", Font.BOLD, 100));
			g.drawString("GAME OVER", 130, 300);
			g.drawString("���� : " + game_Score, 200, 500);
			if (!main_clicked) {
				g.drawImage(button_main, 200, 700, this);
			} else {
				g.drawImage(button_main_pressed, 200, 700, this);
			}

		} catch (Exception e) {
			System.out.println("��������: " + e);
		}

	}

	public void Draw_Main(Graphics2D g) {
		g.clearRect(0, 0, f_width, f_height);
		g.setFont(new Font("Default", Font.BOLD, 100));
		g.drawString("MAIN", 330, 300); // ���ھ� ǥ��
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
		// ���� ����Ʈ�� �׸��� �κ�
		for (int i = 0; i < Explosion_List.size(); i++) {
			ex = (Explosion) Explosion_List.get(i);
			// ���� ����Ʈ�� ���� ������ üũ
			if (ex.damage == 0) {
				// �������� 0�̸� ���߿� �̹��� �׸���
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
			} else if (ex.damage == 1) { // �������� 1�̸� �ܼ� �ǰݿ� �̹��� �׸���
				if (ex.ex_cnt < 7) {
					g.drawImage(Explo_img[0], ex.x + 60, ex.y + 8, this);
				} else if (ex.ex_cnt < 14) {
					g.drawImage(Explo_img[1], ex.x + 60, ex.y + 5, this);
				} else if (ex.ex_cnt < 21) {
					g.drawImage(Explo_img[0], ex.x + 60, ex.y + 8, this);
				} else if (ex.ex_cnt >= 21) {
					Explosion_List.remove(i);
					ex.ex_cnt = 0;
					// �ܼ� �ǰ� ���� ���������� �̹����� �׸�����
					// ������ ���� �ణ �ٸ� ������� �׸��ϴ�.
				}
			} else if (ex.damage == 2) { // �������� 2�̰� ���� �ǰ� ��
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

	public void Draw_StatusText(Graphics2D g) { // ���� üũ�� �ؽ�Ʈ�� �׸��ϴ�.
		g.setFont(new Font("DEFAULT", Font.BOLD, 25));
		g.drawString("SCORE : " + game_Score, 700, 70); // ���ھ� ǥ��
		g.drawString("HitPoint : " + player_Hitpoint, 700, 95); // �÷��̾� ü�� ǥ��
	}

	public void Draw_Background(Graphics2D g) {
		g.clearRect(0, 0, f_width, f_height); // ȭ�� ����� ���
		if (by < -1500) {
			g.drawImage(BackGround_img, 0, by, this);
			by += 1; // ��� �̹����� ��ǥ�� ���� ����
		} else
			by = -6660;
		;

		for (int i = 0; i < cx.length; ++i) {// cx.length�� 5

			if (cx[i] < 1400) {
				cx[i] += 5 + i * 3;
			} else {
				cx[i] = 0;
			}

			g.drawImage(Cloud_img[i], 50 + i * 200, cx[i], this);// ��

			// 5���� ���� �̹����� ���� �ٸ� �ӵ� ������ �������� ������.
		}
	}

	public void Draw_Player(Graphics2D g) { // �� ĳ���� �׸���
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

	public void Draw_Missile(Graphics2D g) { // �̻��� �׸��� �޼ҵ�
		for (int i = 0; i < Missile_List.size(); i++) { // �̻��� ���� ������ Ȯ���Ѵ�.
			ms = (Missile) (Missile_List.get(i)); // �̻��� ��ġ���� Ȯ��
			if (ms.who == 0)
				g.drawImage(Missile_img, ms.x + 20, ms.y, this); // ���� ��ǥ�� �̻��� �׸���
			if (ms.who >= 1)
				g.drawImage(Missile2_img, ms.x + 20, ms.y, this);
		}
	}

//	public void Draw_Boss(Graphics2D g) { // ���� �׸��� �޼ҵ�
//		for (int i = 0; i < Enemy_List.size(); i++) {
//			en = (Enemy) (Enemy_List.get(i));
//			g.drawImage(Boss_img, en.x, en.y, this); // �迭�� ������ �� ���� �Ǻ��Ͽ� �̹��� �׸���
//		}
//	}

	public void Draw_Enemy(Graphics2D g) { // �� ����� �׸��� �޼ҵ�
		for (int i = 0; i < Enemy_List.size(); i++) {
			if (en.kind == 1) {
				en = (Enemy) (Enemy_List.get(i));
				g.drawImage(Enemy1_img, en.x, en.y, this); // �迭�� ������ �� ���� �Ǻ��Ͽ� �̹��� �׸���
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
		// Ű���尡 ���������� �̺�Ʈ ó���ϴ� ��
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
		// Ű���尡 �������ٰ� ���������� �̺�Ʈ ó���ϴ°�
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
			if (gameOver) { // ���� ����
				if (x >= 200 && x <= 400 && y >= 700 && y <= 775) { // ����ȭ�� ��ư Ŭ����
					main_clicked = true;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();
		if (!Ranking) { // ��ŷ ȭ���� �ƴ� �� (����ȭ��)
			if (x >= btn_x && x <= btn_width + btn_x && y >= start_btn_y && y <= btn_height + start_btn_y
					&& start_clicked) {
				// ���� ����
				gameinit(); // �÷��̾� ���� �ʱ�ȭ
				start_clicked = false;
				InGame = true;
			} else if (x >= btn_x && x <= btn_width + btn_x && y >= ranking_btn_y && y <= btn_height + ranking_btn_y
					&& ranking_clicked) {
				// ��ŷ
				new RankingFrame();
				Ranking = true;
			} else if (x >= btn_x && x <= btn_width + btn_x && y >= exit_btn_y && y <= btn_height + exit_btn_y
					&& exit_clicked) {
				// ���� ����
				System.exit(0);
			}
			start_clicked = false;
			ranking_clicked = false;
			exit_clicked = false;
			main_clicked = false;
		}
		if (InGame) {
			if (gameOver) { // ���� ����
				if (x >= 200 && x <= 400 && y >= 700 && y <= 775) { // ����ȭ�� ��ư Ŭ����
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

//	public int ImageWidthValue(String file) { // �̹��� ���� ũ�� �� ���
//		int x = 0;
//		try {
//			File f = new File(file);
//			BufferedImage bi = ImageIO.read(f); // ���� ������ �̹����� �о� ���Դϴ�.
//			x = bi.getWidth(); // �̹����� ���� ���� �޽��ϴ�.
//			System.out.println(x);
//		} catch (Exception e) {
//		}
//		return x;
//	}
//
//	public int ImageHeightValue(String file) { // �̹��� ���� ũ�� �� ���
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
