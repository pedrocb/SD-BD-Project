import sun.rmi.runtime.Log;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class Connection extends Thread{
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    Socket socket;
    RMI rmi;
    public Connection(Socket socket){
        this.socket = socket;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            rmi = (RMI) LocateRegistry.getRegistry(Server.RMI_ADDRESS, 7000).lookup("rmi");
            System.out.println("Connected to RMI and Client");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        start();
    }

    public void run(){
        try{
            while(true){
                //an echo server
                Request data = (Request)inputStream.readObject();
                Response resposta;
                if(!data.getTipo().equals("Ping")) {
                    System.out.println(data.getTipo());
                    resposta = execute(data);
                }
                else{
                    resposta = new Response("Ping");
                }
                outputStream.writeObject(resposta);
            }
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Response execute(Request request) throws RemoteException, SQLException {
        switch (request.tipo){
            case "Login":
                Login aux = (Login)request;
                int[] login = rmi.loginUser(aux.username,aux.password);
                return new IntResponse("Login",login);

            case "Register":
                Register aux2 = (Register)request;
                int auth2 = rmi.registerUser(aux2.username,aux2.password);
                return new IntResponse("Register",new int[]{auth2});

            case "ListActualProj":
                ProjectListResponse response = new ProjectListResponse("ListActualProj");
                ArrayList<Project> projects = rmi.getProjects();
                for(Project item : projects){
                    response.projects.add(item);
                }
                return response;
            case "ListOlderProj":
                ProjectListResponse response2 = new ProjectListResponse("ListOlderProj");
                ArrayList<Project> projects2 = rmi.getOlderProjects();
                for(Project item : projects2){
                    response2.projects.add(item);
                }
                return response2;
            case "ConsultProj":
                ProjectListResponse response1 = new ProjectListResponse("ConsultProj");
                return response1;
            case "CreateProj":
                CreateProj aux3 = (CreateProj)request;
                String dataAux = aux3.year + "-" + aux3.mon + "-" + aux3.day + " " + aux3.hour + ":" + aux3.min;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date date = dateFormat.parse(dataAux);
                    Project project = new Project(aux3.name,date,aux3.goal,aux3.description,true);
                    project.setRewards(((CreateProj) request).rewards);
                    project.setPaths(((CreateProj) request).paths);
                    return new BooleanResponse("CreateProj",rmi.createProject(project,((CreateProj) request).requestId,aux3.userId));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            case "CheckBalance":
                double value = rmi.getBalance(request.userId);
                return new DoubleResponse("CheckBalance",value);
            case "PledgeProj":
                PledgeProj proj = (PledgeProj)request;
                return new BooleanResponse("PledgeProj",rmi.financeProject(proj.proj,proj.requestId,proj.userId,proj.path,proj.valor));
            case "CheckRewards":
                ArrayList<Reward> rewardlist = rmi.getUserRewards(request.userId);
                System.out.println(rewardlist.size());
                return new RewardsResponse("CheckRewards",rewardlist);
            case "ListMyProj":
                ProjectListResponse projectListResponse = new ProjectListResponse("ListMyProj");
                ArrayList<Project> arrayList = rmi.getAdminProjects(1);
                for(Project p:arrayList){
                    projectListResponse.projects.add(p);
                }
                return projectListResponse;
            case "GiveReward":
                GiveReward reward = (GiveReward)request;
                return new BooleanResponse("GiveReward",rmi.giveReward(reward.rewardId,reward.requestId,reward.userId,reward.username,0));
            case "DeleteProj":
                return new BooleanResponse("DeleteProj",rmi.cancelProject(((DeleteProj)request).proj,((DeleteProj) request).requestId,request.userId));

        }
        return null;
    }
}