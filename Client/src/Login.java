/**
 * Created by Rui on 26/10/2015.
 */
public class Login extends Request {
    String username = "";
    String password = "";


    Login(){
        //add username;
        System.out.println("Insira o Username: ");
        try{
            username = reader.readLine();
        }
        catch (Exception e) {
            System.out.println("Erro de escrita.");
        }
        //add password;
        System.out.println("Insira a Password: ");
        try{
            password = reader.readLine();
        }
        catch (Exception e) {
            System.out.println("Erro de escrita.");
        }
    }

}
