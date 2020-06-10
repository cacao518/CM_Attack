import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class Client{

	public Player player;
	public Player otherPlayer;
	
	static Client client;
	static LoginDialog loginDlg;
	static InGameDialog inGameDlg;
	static ResultDialog resultDlg;
	public CMClientStub m_clientStub;
	public CMClientEventHandler m_eventHandler;
	private static boolean m_bRun;
	static Scanner m_scan = null;
	
	public Client()
	{
		m_clientStub = new CMClientStub();
		m_eventHandler = new CMClientEventHandler(this);
		m_bRun = true;
	}
	public void testDummyEvent()
	{
//		CMDummyEvent due = new CMDummyEvent();
//		due.setDummyInfo("CurrentTime");
//		m_clientStub.send(due, "SERVER");
//		due = null;
//		System.out.println("[Client] 서버로 현재시간을 요청했습니다. ");
	}
	public void Login()
	{
		loginDlg = new LoginDialog(client);
	}
	public void Game()
	{
		//게임 윈도우
		loginDlg.matchDlg.dispose();
		loginDlg.dispose();
		loginDlg.matchDlg = null;
		loginDlg = null;
		
		inGameDlg = new InGameDialog(client);
	}

	public void GameOver(int status)
	{
		resultDlg = new ResultDialog(client, status, inGameDlg);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		client = new Client();
		client.m_clientStub.setAppEventHandler(client.m_eventHandler);
		client.m_clientStub.startCM();
		client.Login();
	}


}
