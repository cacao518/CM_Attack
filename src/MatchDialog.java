
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;

public class MatchDialog extends JDialog implements ActionListener{
   int screenWidth;
   int screenHeigth;
   
   Client client;
   LoginDialog parent;
   JPanel root;
   
   JPanel panel1, panel2;
   JButton Btn1;

   JButton Pbt;

   Font f1;
   JLabel label;
   
   public void init() {
      this.setLayout(new FlowLayout(FlowLayout.CENTER,500,15));
      panel1 = new JPanel();
      label = new JLabel("로그인 중입니다...");
      f1 = new Font("맑은 고딕", Font.BOLD, 17);
      label.setFont(f1);
      panel1.add(label);

      panel2 = new JPanel();
      Btn1 = new JButton("취소");
      Btn1.addActionListener(this);
      panel2.add(Btn1);
      
      this.add(panel1);
      this.add(panel2);
      
      this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
         // TODO Auto-generated method stub
         super.windowClosing(e);
         parent.dispose();
         dispose();
         client.m_clientStub.logoutCM();
      }});
   }
   
   MatchDialog(LoginDialog loginDlg, String title, boolean modal, Client c)
   {
      super(loginDlg, title, modal);
      client = c;
      init();
      parent = loginDlg;
      
      Toolkit kit = this.getToolkit();
      Dimension screenSize = kit.getScreenSize();
      this.screenWidth = screenSize.width;
      this.screenHeigth = screenSize.height;
      this.setTitle(title);
      this.setSize(200, 150);
      this.setLocation(screenWidth/2 -100 , screenHeigth/2 -75);
      this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // x버튼 누르면 머할거야 (다이알로그창만 닫을래)
      this.setVisible(true);
      
      this.setResizable(false); // 창 크기 조절 못하게

      Login();
   }
   
   
   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      if(e.getSource()== Btn1 )
      {
         parent.dispose();
         dispose();
         client.m_clientStub.logoutCM();
      }
   }
   
   public void Login()
   {
      client.m_clientStub.loginCM(parent.nameText.getText(), "1234");
      CMUserEvent ue = new CMUserEvent();
      //String ip = m_clientStub.getMyself().getHost();
      String ip = "ip";
      String name = parent.nameText.getText();
      int GunType = 0; // 0:라이플, 1:스나이프
      if(parent.rb1.isSelected() == true) GunType = 0; else GunType = 1;
      int groupNum = -1;
      ue.setEventField(CMInfo.CM_STR, "ip", ip);
      ue.setEventField(CMInfo.CM_STR, "name", parent.nameText.getText());
      ue.setEventField(CMInfo.CM_INT, "GunType", String.valueOf(GunType));
      ue.setEventField(CMInfo.CM_INT, "group", String.valueOf(groupNum));
      client.m_clientStub.send(ue,"SERVER");
      client.player = new Player(ip, name, groupNum, GunType);
   }
   
}