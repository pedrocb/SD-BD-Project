/**
 * Created by Rui on 26/10/2015.
 */
public class Register extends Request{
    Register(){
        //add username;
        System.out.println("Insira o Username: ");
        campos_string.add(reader.realLine());
        //add password;
        System.out.println("Insira a Password: ");
        campos_string.add(reader.realLine());
    }
}
