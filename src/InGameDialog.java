import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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

public class InGameDialog  extends JFrame implements ActionListener, Runnable {

	Client client;
	
	Toolkit kit;
	Container cont;
	int screenWidth;
	int screenHeigth;
	JButton Btn1;
	JButton Btn2;
	JButton Btn3;
	JButton Btn4;
	JPanel panel1;
	JLabel label;
	JTextField nameText;

	Image cursorimage;
	Image BGimg, FPGunimg1, OtherPlayer_idle_img, indicator_img;
	Image buffImage;
	Graphics buffg;
	Cursor cursor;
	Point point;
	Thread Game = null;
    Graphics bufferGraphics;

    int Bullet = 30;
	int BGposX = -695;
	int OPposHelper = 0;
	long millis = System.currentTimeMillis();
	int FireMotionTime = 0;
	String attackDamage = "";
	InGameDialog (Client c)
	{
		client = c;
		
		BGimg = new ImageIcon("img/backGround.png").getImage();
		FPGunimg1 = new ImageIcon("img/FirstPersonGun1.png").getImage();
		OtherPlayer_idle_img = new ImageIcon("img/OtherPlayer_idle.png").getImage();
		cont = this.getContentPane();
		kit = this.getToolkit();
		panel1 = new JPanel();
		
		Dimension screenSize = kit.getScreenSize();
		this.screenWidth = screenSize.width;
		this.screenHeigth = screenSize.height;
		this.setSize(800, 600);
		this.setLocation(screenWidth/2 -400 , screenHeigth/2 -300);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 종료시 프로그램 종료
		this.setTitle("CMAttack");
		
		this.setResizable(false); // 창 크기 조절 못하게
		
		cursorimage=kit.getImage("img/CrossHair.png");//커서로 사용할 이미지
		point=new Point(15,15);
		cursor=kit.createCustomCursor(cursorimage, point, "haha");
		cont.setCursor(cursor); 
		
		addKeyListener(new KeyListener());
		addMouseListener(new MouseListener());
		
		DrawStart();
	}
	
	public void DrawStart()
    {
		if(Game == null)
        {
            Game = new Thread(this);
            Game.start();
            
        	buffImage = createImage(800, 600); 
    		buffg = buffImage.getGraphics();
        }
    }

    public void paint(Graphics g)
    {
		buffg.clearRect(0, 0, 800, 600);
		buffg.drawImage(BGimg, BGposX + client.player.m_playerPosX, 0, null);//background를 그려줌
		buffg.drawImage(FPGunimg1, 550, 370, null);
		buffg.drawImage(OtherPlayer_idle_img, client.otherPlayer.m_playerPosX + (client.player.m_playerPosX-403), 305, null);
		
		buffg.setFont(new Font("굴림", Font.BOLD, 13));
		buffg.setColor(new Color(0,0,0));
		buffg.drawString(client.otherPlayer.getM_name(), client.otherPlayer.m_playerPosX + (client.player.m_playerPosX-395) - (client.otherPlayer.getM_name().getBytes().length*2), 303);
		
		buffg.setFont(new Font("굴림", Font.BOLD, 15));
		buffg.setColor(new Color(255,0,0));
		buffg.drawString(attackDamage, client.otherPlayer.m_playerPosX + (client.player.m_playerPosX-370), 323);
		
		buffg.setColor(new Color(0,0,0));
		buffg.setFont(new Font("Defualt", Font.BOLD, 20));
		buffg.drawString("HP : " + client.player.getM_hp(), 40, 550);
		buffg.drawString("Bullet : " + Bullet, 40, 570);

		buffg.setFont(new Font("Defualt", Font.BOLD, 25));
		buffg.drawString(client.player.m_name, 35, 65);
		
		buffg.drawImage(indicator_img, 290, 100, null);
		
		g.drawImage(buffImage, 0, 0, this);
    }

	public void update(Graphics g)
    {
        paint(g);
    }

	public void run() {
		while(true)
			try {
				this.revalidate();
				this.repaint();

				if(FireMotionTime > 0)
				{
					FireMotionTime--;
					if(client.player.getM_gunType() == 0)
						FPGunimg1 = new ImageIcon("img/FirstPersonGun2.png").getImage();
					else
						FPGunimg1 = new ImageIcon("img/FirstPersonGun4.png").getImage();
					cursorimage=kit.getImage("img/CrossHair_dist.png");
					point=new Point(15,15);
					cursor=kit.createCustomCursor(cursorimage, point, "haha");
					cont.setCursor(cursor);
				}
				else
				{
					FireMotionTime = 0;
					if(client.player.getM_gunType() == 0)
						FPGunimg1 = new ImageIcon("img/FirstPersonGun1.png").getImage();
					else
						FPGunimg1 = new ImageIcon("img/FirstPersonGun3.png").getImage();
					cursorimage=kit.getImage("img/CrossHair.png");
					point=new Point(15,15);
					cursor=kit.createCustomCursor(cursorimage, point, "haha");
					cont.setCursor(cursor);
				}
				if(client.player.indicatorTime > 0)
				{
					indicator_img = new ImageIcon("img/damage_Indicator.png").getImage();
					client.player.indicatorTime--;
				}
				else
					indicator_img = null;
				
				if(client.player.attackSuccessTime > 0)
				{
					attackDamage = "-"+String.valueOf(client.player.attackSuccessDamage);
					client.player.attackSuccessTime--;
				}
				else
					attackDamage = "";
				
				Game.sleep(5);

			} catch (InterruptedException e) {
				e.printStackTrace();

			}

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == Btn1)
		{

		}
	}
	public class KeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyChar()) { // 입력된 키 문자
				case 'a': // <Enter> 키 입력
				case 'A':
				case 'ㅁ':
					if(client.player.getM_playerPosX() < 430)
					{
						System.out.println("pressA");
						sendKey("a");
					}
					break;
				case 'd':
				case 'D':
				case 'ㅇ':
					if(client.player.getM_playerPosX() > 370)
					{
						System.out.println("pressD");
						sendKey("d");
					}
					break;
			}
		}
	}

	public class MouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int speed ;
			if(client.player.getM_gunType()==0)
				speed = 150;
			else
				speed = 3000;
	        long time = System.currentTimeMillis();
			long subs = time - millis;
			if(subs>speed && Bullet > 0) {
	            int x = e.getX()-15;
	            int y = e.getY();
	            double dValue1 = Math.random();
	            double dValue2 = Math.random();
	             int dx = (int)(dValue1 * 10)-5;
	             int dy = (int)(dValue2 * 10)-5;
	            System.out.println(x);
	            System.out.println(y);
	            System.out.println(dx);
	            System.out.println(dy);
	            sendmouse(x + dx ,y + dy);
	            millis = System.currentTimeMillis();
	            Play("sound/gun.wav");
	            FireMotionTime = 9;
	            Bullet-=1;

			}

		}
	}
	public void Play(String fileName){
		try{
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
			Clip clip = AudioSystem.getClip();
			clip.stop();
			clip.open(ais);
			clip.start();
		}
		catch (Exception ex){}
	}

	public void sendmouse(int x, int y) {
      CMUserEvent ue = new CMUserEvent();
      ue.setStringID("click");
      ue.setEventField(CMInfo.CM_STR, "name", client.player.getM_name());
      ue.setEventField(CMInfo.CM_INT, "group", String.valueOf(client.player.getM_group()));
      ue.setEventField(CMInfo.CM_INT, "aimX", String.valueOf(x));
      ue.setEventField(CMInfo.CM_INT, "aimY", String.valueOf(y));
      client.m_clientStub.send(ue,"SERVER");
   }
   public void sendKey(String x) {
      CMUserEvent ue = new CMUserEvent();
      ue.setStringID("move");
      ue.setEventField(CMInfo.CM_STR, "name", client.player.getM_name());
      ue.setEventField(CMInfo.CM_INT, "group", String.valueOf(client.player.getM_group()));
      ue.setEventField(CMInfo.CM_STR, "x", x);
      client.m_clientStub.send(ue,"SERVER");
   }
}