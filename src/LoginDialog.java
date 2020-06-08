import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;

public class LoginDialog extends JFrame implements ActionListener {
	// Swing
	Client client;
	MatchDialog matchDlg = null;
	int screenWidth;
	int screenHeigth;
	JButton Btn1;
	JButton Btn2;
	JButton Btn3;
	JButton Btn4;
	JPanel panel1, panel2, panel3;
	JLabel label;
	JTextField nameText;
	public JRadioButtonMenuItem rb1, rb2;
	LoginDialog(Client c)
	{
		client = c;
		Container cont = this.getContentPane();
		Toolkit kit = this.getToolkit();
		ImageIcon image1 = new ImageIcon("img/weapon1.png");
		ImageIcon image2 = new ImageIcon("img/weapon2.png");
		ImageIcon logo = new ImageIcon("img/logo.png");
		panel1 = new JPanel();
		label = new JLabel(logo);
		panel1.add(label);
		
		panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER,500,15));
		ButtonGroup g = new ButtonGroup(); // 버튼 그룹 객체 생성
		rb1 = new JRadioButtonMenuItem("라이플", image1, true);
		rb2 = new JRadioButtonMenuItem("스나이프", image2, false);
		g.add(rb1);
		g.add(rb2);
	
		Btn1 = new JButton("LOGIN");
		Btn1.setPreferredSize(new Dimension(100, 40));
		Btn1.addActionListener(this);
	
		panel2.add(rb1);
		panel2.add(rb2);
		
		panel3 = new JPanel();
		nameText = new JTextField(10);
		panel3.add(new JLabel("이름 "));
		panel3.add(nameText);		
		panel2.add(panel3);
		
		panel2.add(Btn1);
		
		cont.add(panel1, BorderLayout.NORTH);
		cont.add(panel2, BorderLayout.CENTER);
				
		Dimension screenSize = kit.getScreenSize();
		this.screenWidth = screenSize.width;
		this.screenHeigth = screenSize.height;
		this.setSize(400, 500);
		this.setLocation(screenWidth/2 -200 , screenHeigth/2 -250);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 종료시 프로그램 종료
		this.setTitle("CMAttack Login");
		
		this.setResizable(false); // 창 크기 조절 못하게
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == Btn1)
		{
			matchDlg = new MatchDialog(this,"CMAttack", false, client);
		}
	}
}
