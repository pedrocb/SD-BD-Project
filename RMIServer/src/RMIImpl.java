/**
 * Created by mariobalca on 24-10-2015.
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class RMIImpl extends UnicastRemoteObject implements RMI  {
    static Connection connection;
    static Statement statement;
    protected RMIImpl(Connection connection) throws RemoteException, SQLException {
        super();
        this.connection = connection;
        this.statement = connection.createStatement();
        this.statement.execute("PRAGMA foreign_keys = ON");
    }

    public ArrayList<User> getUsers() throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("Select * from Users");
        ArrayList<User> users = new ArrayList<User>();
        while(result.next())
        {
            users.add(new User(result.getInt(1), result.getString(2), result.getString(3), result.getDouble(4)));
        }
        System.out.println("Get Users executed");
        return users;
    }

    public ArrayList<User> getAdmins(int projectId) throws java.rmi.RemoteException, SQLException{
        ResultSet result = statement.executeQuery("Select * from Administrators where projectId = " + projectId);
        ArrayList<User> users = new ArrayList<User>();
        while(result.next())
        {
            users.add(new User(result.getInt(1), result.getString(2), result.getString(3), result.getDouble(4)));
        }
        System.out.println("Get Admins executed");
        return users;
    }

    public ArrayList<Project> getProjects() throws java.rmi.RemoteException, SQLException{
        ResultSet result = statement.executeQuery("Select * from Projects");
        ArrayList<Project> projects = new ArrayList<Project>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while(result.next())
        {
            try {
                Date projectDate = format.parse(result.getString(3));
                if(new Date().before(projectDate))
                    projects.add(new Project(result.getInt(1), result.getString(2),  format.parse(result.getString(3)), result.getDouble(4), result.getString(5)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Get Projects executed");
        return projects;
    }

    public ArrayList<Project> getOlderProjects() throws java.rmi.RemoteException, SQLException{
        ResultSet result = statement.executeQuery("Select * from Projects");
        ArrayList<Project> projects = new ArrayList<Project>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while(result.next())
        {
            try {
                Date projectDate = format.parse(result.getString(3));
                if(new Date().after(projectDate))
                    projects.add(new Project(result.getInt(1), result.getString(2), projectDate  , result.getDouble(4), result.getString(5)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Get Older Projects executed");
        return projects;
    }

    public ArrayList<Project> getUserProjects(int userId) throws java.rmi.RemoteException, SQLException{
        ResultSet result = statement.executeQuery("Select * from Projects where OwnerUserId = " + userId);
        ArrayList<Project> projects = new ArrayList<Project>();;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while(result.next())
        {
            try {
                projects.add(new Project(result.getInt(1), result.getString(2),  format.parse(result.getString(3)), result.getDouble(4), result.getString(5)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Get User Projects executed");
        return projects;
    }

    public ArrayList<Reward> getRewards(int userId) throws java.rmi.RemoteException, SQLException{
        ResultSet result = statement.executeQuery("Select * from Rewards_Users where OwnerUserId = " + userId);
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        while(result.next()){
            rewards.add(new Reward(result.getInt(1), result.getDouble(2), result.getString(3), result.getString(4)));
        }
        System.out.println("Get Rewards executed");
        return rewards;
    }

    public ArrayList<Extra> getExtraRewards(int userId) throws java.rmi.RemoteException, SQLException{
        ResultSet result = statement.executeQuery("Select * from Extras_Users where OwnerUserId = " + userId);
        ArrayList<Extra> extraRewards = new ArrayList<Extra>();
        while(result.next())
        {
            extraRewards.add(new Extra(result.getInt(1), result.getDouble(2), result.getString(3), result.getString(4)));
        }
        System.out.println("Get Extras executed");
        return extraRewards;
    }

    public ArrayList<Message> getMessages(int projectId) throws java.rmi.RemoteException, SQLException{
        ResultSet result = statement.executeQuery("select * from messages");
        ArrayList<Message> messages = new ArrayList<Message>();
        while(result.next())
        {
            ResultSet fromUserResult = statement.executeQuery("select * from Users where fromUserId = " + result.getInt(5));
            User fromUser = new User(fromUserResult.getInt(1), fromUserResult.getString(2), fromUserResult.getString(3), fromUserResult.getDouble(4));

            messages.add(new Message(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), fromUser));
        }
        System.out.println("Get Messages executed");
        return messages;
    }

    public double getBalance(int userId) throws java.rmi.RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select Balance from Users where id = " + userId);
        double balance = result.getDouble(1);

        System.out.println("Get Balance executed");
        return balance;
    }

    public boolean createProject(Project project, int requestId, int userId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into projects (Name, Deadline, Objective, Description, OwnerUserId) values (\"" + project.getName() + "\", \"" + dateFormat.format(project.getDeadline()) + "\"," + project.getObjective() + ", \"" + project.getDescription() + "\", " + userId + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");

            return true;
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean removeProject(int projectId, int requestId, int userId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("delete from projects where id = " + projectId);
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");

            return true;
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean financeProject(int projectId, int requestId, int userId, int pathId, double value) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into transactions (UserId, ProjectId, Value) values (" + userId + ", " + projectId + ","  + ", " + value + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");

            return true;
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean sendMessage(Message message, int projectId, int requestId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + message.getSender().getId());
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into messages (ProjectId, Subject, Question, Response, FromUserId) values (" + projectId + ",\"" + message.getSubject() + "\",\"" + message.getQuestion() + "\", \"\", " + message.getSender().getId() + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + message.getSender().getId() + ", " + requestId + ", 1)");

            return true;
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + message.getSender().getId());
            return result.getBoolean(1);
        }
    }

    public boolean answerMessage(int messageId, String response, int requestId, int userId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("update messages set response = " + response + " where id = " + messageId);
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");

            return true;
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean createPath(Path path, int requestId, int userId, int projectId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into paths (Name, Description, ProjectId) values (\"" + path.getName() + "\", \"" + path.getDescription() + "\", " + projectId + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");

            return true;
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean deletePath(int pathId, int requestId, int userId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("delete from paths where id = " + requestId);
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");

            return true;
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }
/*
    public boolean addReward(Reward reward, int requestId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into projects (Name, Deadline, Objective, Description, OwnerUserId) values (\"" + project.getName() + "\", \"" + dateFormat.format(project.getDeadline()) + "\"," + project.getObjective() + ", \"" project.getDescription() + "\", " + userId + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean removeReward(int rewardId, int requestId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("delete from rewards where id = " + rewardId);
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean winReward(int rewardId, int requestId, int userId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into projects (Name, Deadline, Objective, Description, OwnerUserId) values (\"" + project.getName() + "\", \"" + dateFormat.format(project.getDeadline()) + "\"," + project.getObjective() + ", \"" project.getDescription() + "\", " + userId + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean giveReward(int rewardId, int requestId, int giverUserId, int receiverUserId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into projects (Name, Deadline, Objective, Description, OwnerUserId) values (\"" + project.getName() + "\", \"" + dateFormat.format(project.getDeadline()) + "\"," + project.getObjective() + ", \"" project.getDescription() + "\", " + userId + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }



    public boolean addExtraReward(Extra extra, int requestId, int userId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into projects (Name, Deadline, Objective, Description, OwnerUserId) values (\"" + project.getName() + "\", \"" + dateFormat.format(project.getDeadline()) + "\"," + project.getObjective() + ", \"" project.getDescription() + "\", " + userId + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean removeExtraReward(int extraId, int requestId, int userId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("delete from extras where id = " + extraId);
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }

    public boolean winExtraReward(int extraId, int requestId, int userId) throws RemoteException, SQLException {
        ResultSet result = statement.executeQuery("select count(*) from logs where requestId = " + requestId + " and userId = " + userId);
        if(result.getInt(1) == 0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement.executeQuery("insert into projects (Name, Deadline, Objective, Description, OwnerUserId) values (\"" + project.getName() + "\", \"" + dateFormat.format(project.getDeadline()) + "\"," + project.getObjective() + ", \"" project.getDescription() + "\", " + userId + ")");
            statement.executeQuery("insert into logs (UserId, RequestId, Response) values (" + userId + ", " + requestId + ", 1)");
        }
        else{
            result = statement.executeQuery("select response from logs where requestId = " + requestId + " and userId = " + userId);
            return result.getBoolean(1);
        }
    }*/
}
