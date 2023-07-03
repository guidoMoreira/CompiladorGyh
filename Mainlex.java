// Bruno Keller Margaritelli
// Guido Margonar Moreira
// Link: https://youtu.be/ZdG73d0wUFo

import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
class Mainlex {
  
  public static void main(String[] args) {
    //Vetor de tokens
    ArrayList<Token> VTokens = new ArrayList<Token>();  
    
    //Imprime argumento String passado
    System.out.println("=Começando a compilar arquivo \""+args[0]+"\"==");

    //Cria classe gylex passando o nome do arquivo como parametro
    Gyhlex lex = new Gyhlex(args[0]);

    //Executa a função que le o proximo lexema e retorna o token
    Token aux = lex.proximoToken();
    VTokens.add(aux);


    System.out.println("\n\n==Iniciando Análise Léxica==");
    int i = 0;
    while(!aux.lexema.equals("CABO")){
     
     if(VTokens.get(i).nome == Tipotoken.BrL){
       System.out.print("\n");
     } else{System.out.print(VTokens.get(i).toString()+" ");}
      aux = lex.proximoToken();
      if(aux.lexema == "Erro"){
        System.out.println("Ocorreu um erro\n");
      }
      VTokens.add(aux);
      i++;
    }
     System.out.println("\n\n==Análise Léxica concluida==");

    // Análise sintática
    System.out.println("\n\n==Iniciando Análise Sintática==");
    
    //Inicializa o analisador sintático com os valores do vetor
    LeitorTokens AnalisadorSintatico = new LeitorTokens(VTokens,args[1]);

    //Realiza a análise sintática
    AnalisadorSintatico.Programa();

    //Verificando se chegou ao final do arquivo
    if(AnalisadorSintatico.VetToken.get(0).nome == Tipotoken.EOF){System.out.println("\n\n==Análise Sintática concluida==");}
    else{
      System.out.println("\n\n==Erro na Análise Sintática==");
    }

    //Mostra o que sobrou no vetor
    System.out.println(AnalisadorSintatico.VetToken.toString());



    //Cria analisador semantico com dados do sintatico
    AnalisadorSemantico ASem = new AnalisadorSemantico(AnalisadorSintatico);

    //Executa analise do Semantico
    ASem.Analise();

    //ap4
    //Converte codigo para C e cria arquivo
    AnalisadorSintatico.Comp();

    try{//Cria executavel apartir do arquivo .c caso ele exista sem erros
      Runtime.getRuntime().exec("gcc -o "+args[1]+" "+args[1]+".c");
      
	  }catch (IOException e) {
		e.printStackTrace();
	  } 
  }
}