import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ResultDialog extends JDialog implements ActionListener{
	int screenWidth;
	int screenHeigth;

	Client client;
	InGameDialog parent;
	JPanel root;

	JPanel panel1, panel2;
	JButton Btn1;

	JButton Pbt;

	Font f1;
	JLabel label;

	String resultMessage = "aaa";


	public void init(int s) {
		if(s == 0)
			resultMessage = "�¸�";
		else
			resultMessage = "�й�";

		this.setLayout(new FlowLayout(FlowLayout.CENTER,500,15));
		panel1 = new JPanel();
		label = new JLabel(resultMessage);
		f1 = new Font("���� ����", Font.BOLD, 45);
		label.setFont(f1);
		panel1.add(label);

		panel2 = new JPanel();
		Btn1 = new JButton("Ȯ��");
		Btn1.addActionListener(this);
		panel2.add(Btn1);

		this.add(panel1);
		this.add(panel2);

		this.addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			super.windowClosing(e);
			dispose(); // �ڽŸ� �����
		}});
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == Btn1)
		{
			parent.dispose();
			dispose();
		}
	}
	ResultDialog(Client c, int status, InGameDialog dlg)
	{
		super();
		client = c;
		parent = dlg;
		init(status);

		Toolkit kit = this.getToolkit();
		Dimension screenSize = kit.getScreenSize();
		this.screenWidth = screenSize.width;
		this.screenHeigth = screenSize.height;
		this.setTitle("Result");
		this.setSize(300, 300);
		this.setLocation(screenWidth/2 -150 , screenHeigth/2 -155);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // x��ư ������ ���Ұž� (���̾˷α�â�� ������)
		this.setVisible(true);
		
		this.setResizable(false); // â ũ�� ���� ���ϰ�

	}

	
	

	

}