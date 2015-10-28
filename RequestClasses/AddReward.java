import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Rui on 26/10/2015.
 */
public class AddReward extends Request{


    private int proj;
    private double valor;
    private String nome = "";
    private String descricao = "";

    public AddReward(){
        super("AddReward");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //Definir o projeto a que adicionar a reward
        System.out.println("Indique o ID do projeto: ");
        try{
            proj = Integer.parseInt(reader.readLine());
        }
        catch (Exception e) {
            System.out.println("Erro de escrita.");
        }

        //Definir o valor m�nimo da reward
        System.out.println("Indique o valor m�nimo da reward: ");
        try{
            valor = Double.parseDouble(reader.readLine());
        }
        catch (Exception e) {
            System.out.println("Erro de escrita do double.");
        }

        //Definir o nome da reward
        System.out.println("Indique o nome da reward: ");
        try{
           nome = (reader.readLine());
        }
        catch (Exception e) {
            System.out.println("Erro de escrita.");
        }

        //Definir a descri��o da reward
        System.out.println("Indique a descri��o para a reward: ");
        try{
           descricao = (reader.readLine());
        }
        catch (Exception e) {
            System.out.println("Erro de escrita.");
        }
        
    }
}