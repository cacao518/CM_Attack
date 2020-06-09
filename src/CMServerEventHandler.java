import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.mysql.jdbc.log.Log;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.*;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMDBManager;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;
import kr.ac.konkuk.ccslab.cm.stub.CMStub;

public class CMServerEventHandler implements CMAppEventHandler {
	static int HIT_RANGE = 15;

	private Server m_server;
	private CMServerStub m_serverStub;
	private ArrayList<GameManager> GM;
	private int playerCount;
	public CMServerEventHandler(CMServerStub serverStub, ArrayList<GameManager> GM, int playerCount, Server server)
	{
		this.m_serverStub = serverStub;
		this.GM = GM;
		this.playerCount = playerCount;
		this.m_server = server;
	}

	@Override
	public void processEvent(CMEvent cme) {
		// TODO Auto-generated method stub
		switch(cme.getType())
		{
			case CMInfo.CM_SESSION_EVENT:
				CMSessionEvent cse = (CMSessionEvent) cme;
				if(cse.getID() == CMSessionEvent.LOGIN){
					m_server.plusPlayerCount();
					playerCount = m_server.getPlayerCount();
				}
				break;
			case CMInfo.CM_DUMMY_EVENT:
				processDummyEvent(cme);
				break;
			case CMInfo.CM_USER_EVENT:
				CMUserEvent ue = (CMUserEvent) cme;
				if (Integer.parseInt(ue.getEventField(CMInfo.CM_INT, "group")) == -1) {
					System.out.println("ㅁㄴ" + playerCount);
					joinPlayer(cme);
				}
				else {
					playGame(cme);
					break;
				}
			default:
		}
	}
	private void joinPlayer(CMEvent cme) {
		System.out.println("joined");
		CMUserEvent ue = (CMUserEvent) cme;
		int GMIndex = (playerCount - 1) / 2;
		int PMIndex = playerCount % 2;
		//System.out.println(GMIndex);
		String ip = ue.getEventField(CMInfo.CM_STR, "ip");
		String name = ue.getEventField(CMInfo.CM_STR, "name");
		int gunType = Integer.parseInt(ue.getEventField(CMInfo.CM_INT, "GunType"));
		if(PMIndex == 1){
			GM.add(GMIndex, new GameManager());
		}
		PlayerManager[] pm = GM.get(GMIndex).getPM();
		pm[PMIndex].setProperty(ip, name, GMIndex, gunType);

		if(PMIndex == 0) {
			GM.get(GMIndex).m_gameStatus = 1;
			CMUserEvent use = new CMUserEvent();
			use.setStringID("joinComplete");
			use.setEventField(CMInfo.CM_INT,"group",String.valueOf(GMIndex));
			use.setEventField(CMInfo.CM_STR,"ip",ip);
			use.setEventField(CMInfo.CM_STR,"name",name);
			use.setEventField(CMInfo.CM_INT,"guntype",String.valueOf(gunType));
			m_serverStub.send(use, GM.get(GMIndex).PM[0].m_name);
			m_serverStub.send(use, GM.get(GMIndex).PM[1].m_name);
		}
	}
	private void playGame(CMEvent cme){
		CMUserEvent ue = (CMUserEvent) cme;
		int gIndex = Integer.parseInt(ue.getEventField(CMInfo.CM_INT,"group"));
		String userName = ue.getEventField(CMInfo.CM_STR,"name");
		System.out.println("aㅁㄴㅇㅋㅌㅊㅋㅌㅂuserNAME: "+userName);
		int pIndex1;
		int pIndex2;

		GameManager gm = GM.get(gIndex);
		if(gm.PM[0].getM_name().equals(userName)){
			pIndex1=0;
			pIndex2=1;
		}
		else{
			pIndex1=1;
			pIndex2=0;
		}
		System.out.println("ㅁㄴㅋㅌㅊㅂㅈㄷㅋㅌㅊpindex2: "+pIndex2);
		if(ue.getStringID().equals("click")){
			//총쏘는 액션
			int gunType = gm.PM[pIndex1].getM_gunType();
			System.out.println("ㅁㄴㅇㅋㅌㅊㅁㄴㅇㅈㅂㅇㅈㅂ:gunType : " + gunType);
			int aimX = Integer.parseInt(ue.getEventField(CMInfo.CM_INT,"aimX"));
			int aimY = Integer.parseInt(ue.getEventField(CMInfo.CM_INT,"aimY"));
			CMUserEvent use = new CMUserEvent();
			//TODO x,y좌표 계산하여 히트되었는지 체크 후 hp변환정보 send
			if(aimX <= gm.PM[pIndex2].m_playerPosX + (gm.PM[pIndex1].m_playerPosX-403) + HIT_RANGE && aimX >= gm.PM[pIndex2].m_playerPosX + (gm.PM[pIndex1].m_playerPosX-403) - HIT_RANGE){
				if(aimY <= gm.PM[pIndex2].m_playerPosY + HIT_RANGE && aimY >= gm.PM[pIndex2].m_playerPosY - HIT_RANGE) {
					System.out.println("ㅁㄴㅊㅋㅌㅊㄴㅁㅇㅁ맞았다!");
					use.setStringID("hit");
					switch (gunType){
						case 0:	//라플
							int dmg1 = (int)(Math.random() * 20) + 10;
							System.out.println("ㅁㄴㅊㅋㅌㅊㄴㅁㅇㅁDAMAGE: "+dmg1);
							use.setEventField(CMInfo.CM_INT,"damage",String.valueOf(dmg1));
							gm.PM[pIndex2].setM_hp(gm.PM[pIndex2].getM_hp()-dmg1);
							use.setEventField(CMInfo.CM_INT, "hp", String.valueOf(gm.PM[pIndex2].getM_hp()));
							break;
						case 1:	//스나이퍼
							int dmg2 = (int)(Math.random() * 40)+60;
							use.setEventField(CMInfo.CM_INT,"damage",String.valueOf(dmg2));
							gm.PM[pIndex2].setM_hp(gm.PM[pIndex2].getM_hp()-dmg2);
							break;
					}
				}
			}
			CMUserEvent use2 = new CMUserEvent();
			use2.setStringID("attackSuccess");
			m_serverStub.send(use2, gm.PM[pIndex1].getM_name());
			
			System.out.println("ㅁㄴㅇㅁㄴㅁㄴㅇㅌㅋㅊㅋㅌㅊ"+gm.PM[pIndex2].getM_hp());
			m_serverStub.send(use, gm.PM[pIndex2].getM_name());
			if(gm.PM[pIndex2].getM_hp() <= 0){
				endGame(gm.PM[pIndex2].m_name, gm.PM[pIndex1].m_name, gIndex);
			}
		}
		else if(ue.getStringID().equals("move")){
			//움직이는 액션
			boolean LR = true;
			if(ue.getEventField(CMInfo.CM_STR, "x").equals("a"))	//a키 입력은 true
				LR = true;
			else if(ue.getEventField(CMInfo.CM_STR, "x").equals("d"))		//d키 입력은 false
				LR = false;
			if(LR) {
				//if(gm.PM[pIndex1].m_playerPosX - 8 >= 0)
					gm.PM[pIndex1].setM_playerPosX(gm.PM[pIndex1].getM_playerPosX() + 8);

			}
			else {
				//if(gm.PM[pIndex1].m_playerPosX + 8 <= 600)
					gm.PM[pIndex1].setM_playerPosX(gm.PM[pIndex1].getM_playerPosX() - 8);

			}
			CMUserEvent userEvent = new CMUserEvent();
			userEvent.setStringID("move");
			userEvent.setEventField(CMInfo.CM_STR, "name", gm.PM[pIndex1].m_name);
			userEvent.setEventField(CMInfo.CM_INT, "posX", String.valueOf(gm.PM[pIndex1].m_playerPosX));
			m_serverStub.send(userEvent, gm.PM[pIndex1].m_name);
			m_serverStub.send(userEvent, gm.PM[pIndex2].m_name);
		}
	}
	private void endGame(String loserName, String winner, int index){
		CMUserEvent use = new CMUserEvent();
		use.setStringID("gameover");
		use.setEventField(CMInfo.CM_STR,"winner", winner);
		use.setEventField(CMInfo.CM_STR,"loser", loserName);
		m_serverStub.send(use,loserName);
		m_serverStub.send(use,winner);
		//GM.get(index).removeGM();
	}
	private void processDummyEvent(CMEvent cme)
	{
		CMDummyEvent due = (CMDummyEvent) cme;
		System.out.println("[Server] 클라이언트에게 요청이 도착했습니다 : "+due.getDummyInfo());

		if(due.getDummyInfo().equals("CurrentTime"))
		{
			Date time = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat();
			CMDummyEvent due2 = new CMDummyEvent();
			due2.setDummyInfo("현재시간->" + format1.format(time));
			m_serverStub.send(due2, due.getSender());
			System.out.println("[Server] 클라이언트에게 메세지를 전송했습니다 : "+due2.getDummyInfo());

		}
		return;
	}

}