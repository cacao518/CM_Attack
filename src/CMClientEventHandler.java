import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;

public class CMClientEventHandler implements CMAppEventHandler {

	Client client;

	public CMClientEventHandler(Client client) {
		super();
		this.client = client;
	}

	@Override
	public void processEvent(CMEvent cme) {
		// TODO Auto-generated method stub
		switch(cme.getType())
		{
			case CMInfo.CM_DUMMY_EVENT:
				processDummyEvent(cme);
				break;
			case CMInfo.CM_USER_EVENT:
				CMUserEvent use = (CMUserEvent) cme;
				switch (use.getStringID()) {
					case "joinComplete":
						updateGroupInfo(use);
						break;
					case "hit":
						updateHPInfo(use);
						break;
					case "gameover":
						gameOver(use);
						break;
					case "move":
						updateMove(use);
						break;
					case "attackSuccess":
						attackSuceess(use);
						break;
				}
				break;
			default:
				return;
		}
	}
	private void attackSuceess(CMUserEvent use) {
		// TODO Auto-generated method stub
		Play("sound/enemyAttack.wav");
		client.player.attackSuccessTime = 70;
		client.player.attackSuccessDamage = Integer.parseInt(use.getEventField(CMInfo.CM_INT, "damage"));
	}

	//client 그룹정보 갱신
	private void updateGroupInfo(CMUserEvent use){
		int group = Integer.parseInt(use.getEventField(CMInfo.CM_INT,"group"));
		client.player.m_group = group;
		String ip = use.getEventField(CMInfo.CM_STR, "ip");
		String name = use.getEventField(CMInfo.CM_STR, "name");
		int gunType = Integer.parseInt(use.getEventField(CMInfo.CM_INT, "guntype"));
		client.otherPlayer = new Player(ip,name,group,gunType);
		client.Game();
	}
	private void updateHPInfo(CMUserEvent use){
		int damage = Integer.parseInt(use.getEventField(CMInfo.CM_INT, "damage"));
		client.player.m_hp -= damage;
		client.player.indicatorTime = 80;
		Play("sound/myAttack.wav");
	}
	private void gameOver(CMUserEvent use){
		if(use.getEventField(CMInfo.CM_STR, "winner").equals(client.player.getM_name())){
			client.GameOver(0);
		}else{
			client.GameOver(1);
		}

	}
	private void updateMove(CMUserEvent use){
		if(use.getEventField(CMInfo.CM_STR, "name").equals(client.player.getM_name())){	//내가움직임
			client.player.setM_playerPosX(Integer.parseInt(use.getEventField(CMInfo.CM_INT, "posX")));
		}
		else{	//상대움직임
			client.otherPlayer.setM_playerPosX(Integer.parseInt(use.getEventField(CMInfo.CM_INT, "posX")));
		}

	}
	private void processDummyEvent(CMEvent cme)
	{
		CMDummyEvent due = (CMDummyEvent) cme;
		System.out.println("[Client] 서버로부터 답장을 받았습니다 : "+due.getDummyInfo());
		String gm = due.getDummyInfo();
		if (gm.equals("start"))
			client.Game();
		return;
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
}