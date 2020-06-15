import java.util.ArrayList;

import kr.ac.konkuk.ccslab.cm.sns.CMSNSUserAccessSimulator;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class Server{
   
   private CMServerStub m_serverStub;
   private CMServerEventHandler m_eventHandler;
   private boolean m_bRun;
   private int playerCount;
   private ArrayList<GameManager> GM;
   
   public Server()
   {
      m_serverStub = new CMServerStub();
      GM = new ArrayList<GameManager>(10);
      playerCount = 0;
      m_eventHandler = new CMServerEventHandler(m_serverStub, GM, playerCount, this);
      m_bRun = true;

   }
   public void plusPlayerCount(){
      this.playerCount++;
   }
   public void minusPlayerCount() { this.playerCount--; }
   public int getPlayerCount(){
      return this.playerCount;
   }
   public void CreateGM() {
      
   }
   public void DeleteGM() {
      
   }
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      Server server = new Server();
      server.m_serverStub.setAppEventHandler(server.m_eventHandler);
      server.m_serverStub.startCM();

   }
}