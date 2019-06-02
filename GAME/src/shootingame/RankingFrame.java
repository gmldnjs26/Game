package shootingame;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RankingFrame extends JFrame{
	Container contentPane;
	JButton btnNext = new JButton(); // 다음 회차 넘어가기(클리어도)
	JButton btnCl = new JButton(); // 도전 버튼
	TextArea ta = new TextArea(); // 등수 출력 에어리어
	JTextField win_tf = new JTextField(); // 당첨 번호 텍스트 필드 
	JTextField b_tf = new JTextField(); // 보너스 번호 텍스트 필드
	JTextField cl_tf = new JTextField(); // 도전 번호 텍스트 필드
	JLabel lb = new JLabel(); // 회차별 라벨
	
	int i = 0; //회차
	int []p_arr = new int[6]; // 정규번호
	int b_num; //  보너스 번호
	
	
	RankingFrame() {
		setTitle("로또 도전 하기");
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout()); // Layout 설정하지 않겠다는것.
		
		lb.setText(String.valueOf(i)+"회차 당첨번호: ");
		lb.setSize(120,20);
		lb.setLocation(25, 20);
		contentPane.add(lb);
		
		win_tf.setText("                                              클릭->>");
		win_tf.setSize(200,20);
		win_tf.setLocation(130, 20);
		contentPane.add(win_tf);
		
		btnNext.setText("다음회차");
		btnNext.setSize(88, 20);
		btnNext.setLocation(350, 20);
		btnNext.addActionListener(new btnNextListener());
		contentPane.add(btnNext);
		
		JLabel b_lb = new JLabel("보너스 번호: ");
		b_lb.setSize(90,20);
		b_lb.setLocation(25, 55);
		contentPane.add(b_lb);
		
		b_tf.setText("보너스");
		b_tf.setSize(40, 20);
		b_tf.setLocation(100, 55);
		contentPane.add(b_tf);
		
		JLabel c_lb = new JLabel("도전 번호: ");
		c_lb.setSize(90,20);
		c_lb.setLocation(25, 90);
		contentPane.add(c_lb);
		
		cl_tf.setText("아직 시작x");
		cl_tf.setSize(120,20);
		cl_tf.setLocation(95, 90);
		contentPane.add(cl_tf);
		
		btnCl.setText("도전!");
		btnCl.setSize(70,20);
		btnCl.setLocation(240,90);
		btnCl.addActionListener(new btnClListener());
		contentPane.add(btnCl);
		
		ta.setSize(400, 250);
		ta.setLocation(25, 120);
		contentPane.add(ta);
		
		setSize(500,500);
		setVisible(true);
	}
	class btnClListener implements ActionListener{ // 도전 이벤트
		@Override
		public void actionPerformed(ActionEvent e) {
			String str = "";
			int []arr = new int[6]; // 나의 도전 번호
			int num = 0;
			int rank = 0; // 순위 check
			while(true) { // 랜덤으로 도전 번호 설정하기
				if(num==6) break;
				int value = (int) Math.round((Math.random()*45+1));
				boolean flag = false;
				for(int i = 0 ; i<arr.length ; i++) {
					if(arr[i]==value) {
						flag = true;
						break;
					}
				}
				if(flag ==false) {
					arr[num] = value;
					num++;
				}
			}
			Arrays.sort(arr); // 오름차순 정렬
			for(int i = 0 ; i<arr.length ; i++) {
				str += String.valueOf(arr[i]) + " ";
			}
			str += "\n";
			cl_tf.setText(str); // 출력
			
			int i=0 , j=0;
			while(true) { // 배열 두개다 오름차순 정렬이라는 가정하에 배열 두개 비교하면서 순위 매기기
				if(p_arr[i] > arr[j]) j++;
				else if(p_arr[i] == arr[j]) {
					rank++;
					i++; 
					j++;
				}
				else i++;
				if(i==6 || j==6) break;
			}
			if(rank == 6) ta.append("대박 축하 드립니다~1등 입니다.\n" );
			else if(rank==5) { // 2등은 특수한 경우 보너스 번호를 따로 비교
				for(int k=0; k < arr.length; k++) {
					if(arr[k] == b_num) {
						ta.append("축하 드립니다~2등 입니다.\n" );
						break;
					}
					ta.append("축하 드립니다~3등 입니다.\n" );
				}
			}
			else if(rank == 4) ta.append("축하 드립니다~4등 입니다.\n" );
			else if(rank == 3) ta.append("축하 드립니다~5등 입니다.\n" );
			else ta.append("꽝ㅠ.\n");
		}
		
	}
	class btnNextListener implements ActionListener{ // 다음 회차 넘어가기 
		@Override
		public void actionPerformed(ActionEvent e) {
			i++; // 회차 증가
			ta.setText("---------------"+String.valueOf(i)+"회차 시작"+"---------------\n");
			lb.setText(String.valueOf(i)+"회차 당첨번호: ");
			String str = "";
			int []arr = new int[6]; // 회차별로 번호가 달라져서 여기서 만들어서 복사해주기위해 만들어준 배열
			int num = 0;
			while(true) {			
				int value = (int) Math.round((Math.random()*45+1));
				boolean flag = false;
				for(int i = 0 ; i<arr.length ; i++) {
					if(arr[i]==value) {
						flag = true;
						break;
					}
				}
				if(num==6 &&flag==false) {
					b_num = value;
					break;
				}
				
				if(flag ==false) {
					arr[num] = value;
					num++;
				}
			}
			Arrays.sort(arr);
			System.arraycopy(arr, 0, p_arr, 0, p_arr.length); // 배열 복사
			for(int i = 0 ; i<arr.length ; i++) {
				str += String.valueOf(arr[i]) + " ";
			}
			str += "\n";
			win_tf.setText(str);
			b_tf.setText(String.valueOf(b_num));
		}	
	}
}
