import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LeitorArquivo{
  //Classe leitora do arquivo
  public InputStream is;
  public LeitorArquivo(String Arq){
    try{
      //Cria a classe leitora para o arquivo passado na string Arq
      is = new FileInputStream(Arq);
    }catch(FileNotFoundException e){
      //Erro caso arquivo não seja encontrado
      e.printStackTrace();
    }
  }

  //Função que le o proximo caracter no arquivo
  public int lerProximoCaracter(){
    //chara será igual a -1 caso o programa não consiga ler 
    int chara = -1;
    try{
      //Tenta ler o proximo caracter
      chara = is.read();
    }catch(IOException e){
      e.printStackTrace();
    }
    //retorna o proximo caracter no formato int
		return chara;
  }
}