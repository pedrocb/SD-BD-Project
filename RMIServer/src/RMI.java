/**
 * Created by mariobalca on 24-10-2015.
 */

import java.io.Serializable;
import java.rmi.*;
import java.sql.SQLException;
import java.util.ArrayList;

public interface RMI extends Remote, Serializable{

    // IDEMPOTENTES
    ArrayList<User> getUsers() throws java.rmi.RemoteException, SQLException;
    ArrayList<User> getAdmins(int projectId) throws java.rmi.RemoteException, SQLException;
    ArrayList<Project> getProjectsWithoutDetails() throws java.rmi.RemoteException, SQLException;
    ArrayList<Project> getProjects() throws java.rmi.RemoteException, SQLException;
    ArrayList<Project> getOlderProjects() throws java.rmi.RemoteException, SQLException;
    ArrayList<Project> getOwnedProjects(int projectId) throws java.rmi.RemoteException, SQLException;
    ArrayList<Project> getAdminProjects(int projectId) throws java.rmi.RemoteException, SQLException;
    ArrayList<Reward> getProjectRewards(int projectId) throws java.rmi.RemoteException, SQLException;
    ArrayList<Extra> getProjectExtraRewards(int projectId) throws java.rmi.RemoteException, SQLException;
    ArrayList<Path> getProjectPaths(int projectId) throws java.rmi.RemoteException, SQLException;
    ArrayList<Message> getProjectMessages(int projectId) throws java.rmi.RemoteException, SQLException;
    ArrayList<Reward> getUserRewards(int userId) throws java.rmi.RemoteException, SQLException;
    ArrayList<Extra> getUserExtraRewards(int userId) throws java.rmi.RemoteException, SQLException;
    public double getBalance(int userId) throws java.rmi.RemoteException, SQLException;
    public int[] loginUser(String username, String password) throws java.rmi.RemoteException, SQLException;

    // NÃO IDEMPOTENTES
    public int registerUser(String username, String password) throws java.rmi.RemoteException, SQLException;
    public boolean createProject(Project project, int requestId, int userId) throws java.rmi.RemoteException, SQLException;
    public boolean cancelProject(int projectId, int requestId, int userId) throws java.rmi.RemoteException, SQLException;
    public boolean endProject(int projectId) throws RemoteException, SQLException;
    public boolean financeProject(int projectId, int requestId, int userId, int pathId, double value) throws java.rmi.RemoteException, SQLException;

    public boolean addAdmin(int projectId, int requestId, int userId, int newAdminId) throws java.rmi.RemoteException, SQLException;
    public boolean removeAdmin(int projectId, int requestId, int userId, int newAdminId) throws java.rmi.RemoteException, SQLException;

    public boolean sendMessage(Message message, int projectId, int requestId) throws java.rmi.RemoteException, SQLException;
    public boolean answerMessage(int messageId, String response, int requestId, int userId) throws java.rmi.RemoteException, SQLException;


    public boolean createPath(Path path, int requestId, int userId, int projectId) throws java.rmi.RemoteException, SQLException;
    public boolean deletePath(int pathId, int requestId, int userId) throws java.rmi.RemoteException, SQLException;


    public boolean createReward(Reward reward, int requestId, int projectId, int userId) throws java.rmi.RemoteException, SQLException;
    public boolean removeReward(int rewardId, int requestId, int userId) throws java.rmi.RemoteException, SQLException;
    public boolean winReward(int rewardId, int requestId, int userId) throws java.rmi.RemoteException, SQLException;
    public boolean giveReward(int rewardId, int requestId, int giverUserId, int receiverUserId) throws java.rmi.RemoteException, SQLException;


    public boolean createExtraReward(Extra extra, int requestId, int projectId, int userId) throws java.rmi.RemoteException, SQLException;
    public boolean removeExtraReward(int extraId, int requestId, int userId) throws java.rmi.RemoteException, SQLException;
    public boolean winExtraReward(int  extraId, int requestId, int userId) throws java.rmi.RemoteException, SQLException;

}