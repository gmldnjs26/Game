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
	JButton btnNext = new JButton(); // ���� ȸ�� �Ѿ��(Ŭ���)
	JButton btnCl = new JButton(); // ���� ��ư
	TextArea ta = new TextArea(); // ��� ��� �����
	JTextField win_tf = new JTextField(); // ��÷ ��ȣ �ؽ�Ʈ �ʵ� 
	JTextField b_tf = new JTextField(); // ���ʽ� ��ȣ �ؽ�Ʈ �ʵ�
	JTextField cl_tf = new JTextField(); // ���� ��ȣ �ؽ�Ʈ �ʵ�
	JLabel lb = new JLabel(); // ȸ���� ��
	
	int i = 0; //ȸ��
	int []p_arr = new int[6]; // ���Թ�ȣ
	int b_num; //  ���ʽ� ��ȣ
	
	
	RankingFrame() {
		setTitle("�ζ� ���� �ϱ�");
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout()); // Layout �������� �ʰڴٴ°�.
		
		lb.setText(String.valueOf(i)+"ȸ�� ��÷��ȣ: ");
		lb.setSize(120,20);
		lb.setLocation(25, 20);
		contentPane.add(lb);
		
		win_tf.setText("                                              Ŭ��->>");
		win_tf.setSize(200,20);
		win_tf.setLocation(130, 20);
		contentPane.add(win_tf);
		
		btnNext.setText("����ȸ��");
		btnNext.setSize(88, 20);
		btnNext.setLocation(350, 20);
		btnNext.addActionListener(new btnNextListener());
		contentPane.add(btnNext);
		
		JLabel b_lb = new JLabel("���ʽ� ��ȣ: ");
		b_lb.setSize(90,20);
		b_lb.setLocation(25, 55);
		contentPane.add(b_lb);
		
		b_tf.setText("���ʽ�");
		b_tf.setSize(40, 20);
		b_tf.setLocation(100, 55);
		contentPane.add(b_tf);
		
		JLabel c_lb = new JLabel("���� ��ȣ: ");
		c_lb.setSize(90,20);
		c_lb.setLocation(25, 90);
		contentPane.add(c_lb);
		
		cl_tf.setText("���� ����x");
		cl_tf.setSize(120,20);
		cl_tf.setLocation(95, 90);
		contentPane.add(cl_tf);
		
		btnCl.setText("����!");
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
	class btnClListener implements ActionListener{ // ���� �̺�Ʈ
		@Override
		public void actionPerformed(ActionEvent e) {
			String str = "";
			int []arr = new int[6]; // ���� ���� ��ȣ
			int num = 0;
			int rank = 0; // ���� check
			while(true) { // �������� ���� ��ȣ �����ϱ�
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
			Arrays.sort(arr); // �������� ����
			for(int i = 0 ; i<arr.length ; i++) {
				str += String.valueOf(arr[i]) + " ";
			}
			str += "\n";
			cl_tf.setText(str); // ���
			
			int i=0 , j=0;
			while(true) { // �迭 �ΰ��� �������� �����̶�� �����Ͽ� �迭 �ΰ� ���ϸ鼭 ���� �ű��
				if(p_arr[i] > arr[j]) j++;
				else if(p_arr[i] == arr[j]) {
					rank++;
					i++; 
					j++;
				}
				else i++;
				if(i==6 || j==6) break;
			}
			if(rank == 6) ta.append("��� ���� �帳�ϴ�~1�� �Դϴ�.\n" );
			else if(rank==5) { // 2���� Ư���� ��� ���ʽ� ��ȣ�� ���� ��
				for(int k=0; k < arr.length; k++) {
					if(arr[k] == b_num) {
						ta.append("���� �帳�ϴ�~2�� �Դϴ�.\n" );
						break;
					}
					ta.append("���� �帳�ϴ�~3�� �Դϴ�.\n" );
				}
			}
			else if(rank == 4) ta.append("���� �帳�ϴ�~4�� �Դϴ�.\n" );
			else if(rank == 3) ta.append("���� �帳�ϴ�~5�� �Դϴ�.\n" );
			else ta.append("�Τ�.\n");
		}
		
	}
	class btnNextListener implements ActionListener{ // ���� ȸ�� �Ѿ�� 
		@Override
		public void actionPerformed(ActionEvent e) {
			i++; // ȸ�� ����
			ta.setText("---------------"+String.valueOf(i)+"ȸ�� ����"+"---------------\n");
			lb.setText(String.valueOf(i)+"ȸ�� ��÷��ȣ: ");
			String str = "";
			int []arr = new int[6]; // ȸ������ ��ȣ�� �޶����� ���⼭ ���� �������ֱ����� ������� �迭
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
			System.arraycopy(arr, 0, p_arr, 0, p_arr.length); // �迭 ����
			for(int i = 0 ; i<arr.length ; i++) {
				str += String.valueOf(arr[i]) + " ";
			}
			str += "\n";
			win_tf.setText(str);
			b_tf.setText(String.valueOf(b_num));
		}	
	}
}
