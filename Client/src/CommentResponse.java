/**
 * Created by Rui on 27/10/2015.
 */
public class CommentResponse extends Request{
    CommentResponse(){
        //Perguntar o ID da mensagem a que estamos a responder
        System.out.println("Indique o ID da mensagem a que est� a responder: ");
        try{
            campos_string.add(reader.readLine());
        }
        catch (Exception e) {
        }

        //Escrever a resposta
        System.out.println("Escreva a resposta: ");
        try{
            campos_string.add(reader.readLine());
        }
        catch (Exception e) {
        }
    }
}
