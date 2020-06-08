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
				}
				break;
			default:
				return;
		}
	}
	//client �׷����� ����
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
	}
	private void gameOver(CMUserEvent use){
		if(use.getEventField(CMInfo.CM_STR, "winner").equals(client.player.getM_name())){
			client.GameOver(0);
		}else{
			client.GameOver(1);
		}

	}
	private void updateMove(CMUserEvent use){
		if(use.getEventField(CMInfo.CM_STR, "name").equals(client.player.getM_name())){	//����������
			client.player.setM_playerPosX(Integer.parseInt(use.getEventField(CMInfo.CM_INT, "posX")));
		}
		else{	//��������
			client.otherPlayer.setM_playerPosX(Integer.parseInt(use.getEventField(CMInfo.CM_INT, "posX")));
		}

	}
	private void processDummyEvent(CMEvent cme)
	{
		CMDummyEvent due = (CMDummyEvent) cme;
		System.out.println("[Client] �����κ��� ������ �޾ҽ��ϴ� : "+due.getDummyInfo());
		String gm = due.getDummyInfo();
		if (gm.equals("start"))
			client.Game();
		return;
	}
}