import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Rui on 26/10/2015.
 */
public class Login extends Request {
    public String username = "";
    public String password = "";


    public Login(){
        super("Login");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //add username;
        System.out.println("Insert username: ");
        try{
            username = reader.readLine();
        }
        catch (Exception e) {
            System.out.println("Erro de escrita.");
        }
        //add password;
        System.out.println("Insert password: ");
        try{
            password = reader.readLine();
        }
        catch (Exception e) {
            System.out.println("Erro de escrita.");
        }
    }

}
