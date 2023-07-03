import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
// Classe leitora do arquivo
public class LeitorTokens {
  // Copia da lista de tokens
  public ArrayList<Token> VetToken;

  // Contador de tokens que deram match com sucesso
  public int i;

  // Atribuição completo //Ap3
  public boolean DecComplete;

  // Tokens de variaveis declaradas
  public ArrayList<Token> VarD;
  public ArrayList<Token> TipVarD;

  // Tokens de variaveis usadas na execução
  public ArrayList<Token> VarE;

  // Numero Inteiros e Reais
  public ArrayList<Token> numsInt;
  public ArrayList<Token> numsReal;

  // Variavel para dizer se o comando sendo lido é de atribuição
  public boolean AtribEx;
  public ArrayList<Tipotoken> VarAtrib;
  public ArrayList<Tipotoken> TipVarAtrib;
  public ArrayList<Token> nomVarAtrib;

  //ap4
  public StringBuilder buf;
  public ArrayList<String> comandos;

  public int inside;

  public String Filen;
  
  //ap4
  public void Comp() {
    File file = new File(Filen+".c");

    try{
    FileOutputStream code = new FileOutputStream(Filen+".c", false);
      try {
    for (String str : comandos) {
      byte b[] = str.getBytes();
      
      code.write(b);
      System.out.print(str);
    }
    code.close();
    }catch(IOException ex){}}
    catch (FileNotFoundException ex){
    }
  }

  // Inicialização do Objeto desta classe
  public LeitorTokens(ArrayList<Token> vt,String fn) {
    Filen = fn;
    VetToken = vt;
    VarD = new ArrayList<Token>();
    TipVarD = new ArrayList<Token>();
    VarE = new ArrayList<Token>();
    numsInt = new ArrayList<Token>();
    numsReal = new ArrayList<Token>();
    VarAtrib = new ArrayList<Tipotoken>();
    TipVarAtrib = new ArrayList<Tipotoken>();
    nomVarAtrib = new ArrayList<Token>();
    AtribEx = false;
    DecComplete = false;
    i = 0;

    buf = new StringBuilder();
    buf.setLength(0);

    comandos = new ArrayList<String>();
    comandos.add("#include <stdio.h>\n#include <stdlib.h>\n\nint main(void) {\n");
    inside = 0;
  }

  // Checa se o token atual é o Token Requisistado na analise e printa um erro
  // caso não seja o token requisitado
  private boolean match(Tipotoken Compara, String er) {
    // Checa se o token é o requisitado
    if (VetToken.get(i).nome == Compara) {
      i++;
      return true;
    } else if (VetToken.get(i).nome == Tipotoken.Erro) {// Token lido é um token de erro Lexico
      ErroL(er);
      i++;
      return false;
    } else {
      // Não é o Token requisitado
      Erro(er);
      return false;
    }
  }

  // segue a mesma lógica do outro match mas só printa um erro caso seja um token
  // com erro Lexico
  private boolean match(Tipotoken Compara) {
    if (VetToken.get(i).nome == Compara) {
      i++;
      return true;
    } else if (VetToken.get(i).nome == Tipotoken.Erro) {
      i++;
      ErroL("Palavra reservada");
      return false;
    } else {
      return false;
    }
  }

  // Remove Dados que deram match com sucesso
  private void matchAll() {
    // Ap3
    // Salvar Dados da Atribuição
    if (AtribEx) {
      // Checa todas as variaveis declaradas
      for (int h = 0; h < VarD.size(); h++) {
        // Comparar declaradas com variavel da atribuição
        if (VetToken.get(0).lexema.equals(VarD.get(h).lexema)) {

          if (TipVarD.get(h).nome == Tipotoken.PCInt) {
            VarAtrib.add(Tipotoken.NumInt);
          }
          if (TipVarD.get(h).nome == Tipotoken.PCReal) {
            VarAtrib.add(Tipotoken.NumReal);
          }
          break;
        }
      }
      nomVarAtrib.add(VetToken.get(0));

      int tip = 0;
      // Passa por operação matematca
      for (int j = 2; j < i && tip != 2; j++) {

        if (VetToken.get(j).nome == Tipotoken.Var) {
          for (int h = 0; h < VarD.size(); h++) {
            if (VetToken.get(j).lexema.equals(VarD.get(h).lexema)) {
              if (TipVarD.get(h).nome == Tipotoken.NumInt) {
                tip = 1;
              }
              if (TipVarD.get(h).nome == Tipotoken.NumReal) {
                tip = 2;
              }
              break;
            }
          }

        }
        if (tip != 2) {
          if (VetToken.get(j).nome == Tipotoken.NumInt) {
            tip = 1;
          }
          if (VetToken.get(j).nome == Tipotoken.NumReal) {
            tip = 2;
          }
        }
      }

      if (tip == 1) {
        TipVarAtrib.add(Tipotoken.NumInt);
      }
      if (tip == 2) {
        TipVarAtrib.add(Tipotoken.NumReal);
      }
      AtribEx = false;
    }

    for (int j = 0; j < i; j++) {

      // Printa comandos que deram match com sucesso
      if (VetToken.get(0).nome != Tipotoken.BrL) {
        System.out.print(VetToken.get(0).lexema + " ");
      }

      // Guarda variaveis mencionadas na execução do programa //Ap3
      if (VetToken.get(0).nome == Tipotoken.Var && DecComplete) {
        VarE.add(VetToken.get(0));
      }

      // Salva tokens dos numeros inteiros e reais//Ap3
      if (VetToken.get(0).nome == Tipotoken.NumInt) {
        numsInt.add(VetToken.get(0));
      }
      if (VetToken.get(0).nome == Tipotoken.NumReal) {
        numsReal.add(VetToken.get(0));
      }

      // Remove Dados que deram match com sucesso
      VetToken.remove(0);
    }
    i = 0;// Reseta Indicede matchs com sucesso
    // System.out.println("\n");
  }

  // Printa erro Sintatico
  private void Erro(String e) {
    System.out.print("\nErro sintatico: esperado \"" + e + "\"\n");
  }

  // Printa erro Lexico
  private void ErroL(String e) {
    System.out.println("Erro Léxico: esperado " + e);
  }

  // Analisa tokens do programa
  public void Programa() {

    // Checa se existe ":DEC" no começo do programa
    while (match(Tipotoken.BrL)) {
      matchAll();
    }
    match(Tipotoken.Delim, "Delim");
    match(Tipotoken.PCDec, "DEC");
    match(Tipotoken.BrL, "Nova Linha");

    matchAll();

    // Chama checagem da lista de declarações
    ListaDeclaracoes();

    while (match(Tipotoken.BrL)) {
      matchAll();
    }

    DecComplete = true;// Ap3
    // Checa se existe ":PROG" depois da lista de declarações
    match(Tipotoken.Delim, "Delim");
    match(Tipotoken.PCProg, "PROG");
    match(Tipotoken.BrL, "Nova Linha");
    matchAll();

    // Chama chegagem da lista de declarações
    ListaComandos();

    comandos.add("return 0;\n}\n");
  }

  // Checa Lista de declarações
  private void ListaDeclaracoes() {
    // Checa se não é um \n removendo até encontrar o primeiro token pra checar
    while (VetToken.get(i).nome == Tipotoken.BrL) {
      match(Tipotoken.BrL);
    }

    // Checa se não chegou a ":Prog" e que não é o final do arquivo
    if ((!(VetToken.get(i).nome == Tipotoken.Delim && VetToken.get(i + 1).nome == Tipotoken.PCProg)
        && VetToken.get(i).nome != Tipotoken.EOF)) {

      // Chama checagem de declaração //Ap3
      if (Declaracao()) {
        VarD.add(VetToken.get(i - 3));
        TipVarD.add(VetToken.get(i - 1));
        if (VetToken.get(i - 1).lexema.equals("INT")) {
          buf.append("int ");
        } else {
          buf.append("float ");
        }
        buf.append(VetToken.get(i - 3).lexema + ";\n");
        comandos.add(buf.toString());
        buf.setLength(0);
      }

      // Checa se tem uma quebra de linha entre as declarações
      match(Tipotoken.BrL, "Nova Linha");

      // Remove comandos bem seucedidos no match
      matchAll();

      // Para Organização no console
      System.out.println("\n");

      // Chama por mais Listas de declarações, será equivalente a um vazio caso chegue
      // na confição do if
      ListaDeclaracoes();
    }
  }

  // Checa pelo comando de declaração de Variavel
  private boolean Declaracao() {
    System.out.print("==Declaração Variavel==\n");
    int co = 0;

    // Da match no nome da Variavel
    if (match(Tipotoken.Var, "Variavel")) {
      co++;
    }

    // Caso o proximo valor não seja PROG ele da match no DELIM para declaração
    if (VetToken.get(i + 1).nome != Tipotoken.PCProg) {
      if (match(Tipotoken.Delim, "Delim")) {
        co++;
      }
    }

    // Chama checagem de Tipo de variavel
    if (TipoVar()) {
      co++;
    }

    if (co == 3) {
      return true;
    }
    return false;
  }

  // Checa tipo de Variavel
  private boolean TipoVar() {
    // Checa se é Inteiro
    if (!match(Tipotoken.PCInt)) {

      // Checa se se é real e retorna erro para ambos os tipos caso falhe
      if (!match(Tipotoken.PCReal, "Tipo de váriavel PCInt ou PCReal")) {
        return false;
      }
    }
    return true;
  }

  // Checa lista de Comandos
  private void ListaComandos() {
    // Caso primeiro token seja nova linha ele le todas as nova linhas até encontrar
    // primeiro potencial comando
    while (VetToken.get(i).nome == Tipotoken.BrL) {
      match(Tipotoken.BrL);
    }
    // Remove tokens Br
    matchAll();

    // Checa se chegou no final do Arquivo onde a lista de comandos seria então
    // vazia
    if ((VetToken.get(i).nome != Tipotoken.EOF)) {
      // Chama função para checar comando
      Comando();
      // Deleta tokens do comando lido
      matchAll();

      // Checa se tem mais uma lista de comandos depois comandos
      ListaComandos();
    }
  }

  // Checa Lista de Comandos até o token passado ser encontrado (feito para
  // subalgoritmo)
  private void ListaComandos(Tipotoken t) {
    // Tira Line breaks antes do comando
    while (VetToken.get(i).nome == Tipotoken.BrL) {
      match(Tipotoken.BrL);
    }
    matchAll();

    // Checa se arquivo acabou ou se chegou ao token parametro de parada
    if ((VetToken.get(i).nome != Tipotoken.EOF && VetToken.get(i).nome != t)) {
      for(int i = 0; i < inside;i++){
        buf.append("\t");
      }
      comandos.add(buf.toString());
      buf.setLength(0);
      // Chama checagem de comando
      Comando();

      // Chama a lista de comandos com condição de parada
      ListaComandos(t);
    }
  }

  // Checa por comando
  private boolean Comando() {
    // Ignora Line breaks antes de comando
    while (VetToken.get(i).nome == Tipotoken.BrL) {
      match(Tipotoken.BrL);
    }
    matchAll();
    // Chama checagems para os varios tipos de comando caso o anterior retorne falso
    i = 0; buf.setLength(0);buf.append("\t");
    if (!ComandoCondicao()) {
      i = 0; buf.setLength(0);buf.append("\t");
      if (!ComandoEntrada()) {
        i = 0; buf.setLength(0);buf.append("\t");
        if (!ComandoSaida()) {
          i = 0; buf.setLength(0);buf.append("\t");
          if (!ComandoAtribuicao()) {
            i = 0; buf.setLength(0);buf.append("\t");
            if (!ComandoRepeticao()) {
              i = 0; buf.setLength(0);
              if (!SubAlgoritmo()) {
                // Caso os tokens não condizam com nenhum comando

                // Zera indice
                i = 0;

                // Exibe erro
                System.out.print("==Erro Comando não reconhecido==\n==< ");

                // Remve e msotra todos os tokens dessa linha de comando desconhecida
                while (VetToken.get(i).nome != Tipotoken.BrL) {
                  System.out.print(VetToken.get(i).lexema + " ");
                  i++;
                }
                System.out.print(" >==\n");

                // Retorna que comando não existe
                return false;
              } else {// Sub algoritmo foi completo com sucesso
                inside = 0;
                System.out.println("==Fim Sub Algoritmo==");
              }
            } else {// Comando Repetição
              buf.append("\t}\n");
              comandos.add(buf.toString());
              buf.setLength(0);
            }
          } else {// Comando atribuição
            AtribEx = true;
            buf.append(";\n");
            comandos.add(buf.toString());
            buf.setLength(0);
          }
        } else {// Comando Saida
          if (VetToken.get(i - 1).nome.equals(Tipotoken.Cadeia)) {
            buf.append("printf(\"%s\\n\","+ VetToken.get(i - 1).lexema + ");\n");
          }else{
          int j = 0;
        for(j = 0; j < VarD.size();j++){
          if(VetToken.get(i - 1).lexema.equals(VarD.get(j).lexema)){
            break;
          }
        }
        // if int
        if(TipVarD.get(j).lexema.equals("INT")){
          buf.append("printf(\"%i\\n\"," + VetToken.get(i - 1).lexema + ");\n");
        } 
        else{
          buf.append("printf(\"%f\\n\","+VetToken.get(i-1).lexema+");\n");
          }
          }
          comandos.add(buf.toString());
          buf.setLength(0);
        }
      } else {// Comando Entrada
        int j = 0;
        for(j = 0; j < VarD.size();j++){
          if(VetToken.get(i - 1).lexema.equals(VarD.get(j).lexema)){
            break;
          }
        }
        // if int
        if(TipVarD.get(j).lexema.equals("INT")){
          buf.append("scanf(\"%i\",&" + VetToken.get(i - 1).lexema + ");\n");
        }else{buf.append("scanf(\"%f\",&"+VetToken.get(i-1).lexema+");\n");}
        // if float 
        comandos.add(buf.toString());
        buf.setLength(0);
      }
    } else {// Comando Condicao
      buf.append("\t}\n");
      comandos.add(buf.toString());
      buf.setLength(0);
    }
    // Remove todos os tokens do comando completo
    matchAll();

    // Para melhorar vizualização
    System.out.print("\n");

    // Retorna true pois algum comando foi identificado
    return true;
  }

  // Checa comando de atribuição
  private boolean ComandoAtribuicao() {
    // Inicializa variaveis para teste de existencia
    boolean exist = false;
    boolean aux1, aux2;

    // Checa se existem "Var" e ":=" guardando o resultado nas variaveis auxiliares
    aux1 = match(Tipotoken.Var);
    aux2 = match(Tipotoken.Atrib);

    // Caso algum deles exista o comando de atribuição existe
    if (aux1 || aux2) {
      exist = true;
      buf.append(VetToken.get(i-2).lexema);
      buf.append(" = ");
      if (!aux2) {
        // Mostra erro caso não tenha ":="
        Erro("Atribuição");
      } else if (!aux1) {
        // Mostra erro caso não tenha uma variável antes de ":="
        Erro("Variavel para receber valor da atribuição");
      }
    }

    // Se ele existe
    if (exist) {
      System.out.println("==Comando Atribuição==");

      // Checa se tem um expressão aritimética como valor sendo atribuido
      if (!ExpressaoAritmetica()) {
        Erro("Expressao Aritmetica para atribuição");
      }
    }
    // retorna se existe
    return exist;
  }

  // Checa comando de entrada
  private boolean ComandoEntrada() {
    // Checa se o primeiro token do comando é LER que significaria que o comando
    // atual é de leitura
    if (match(Tipotoken.PCLer)) {
      System.out.println("==Comando Entrada==");

      // Checa se foi passada uma variavel para leitura printando erro se não
      // encontrar
      match(Tipotoken.Var, "Variavel para leitura");
      return true;
    }

    // Comando não é de leitura
    return false;
  }

  // Checa se é comando de saída
  private boolean ComandoSaida() {
    // Checa se o primeiro token do comando é IMPRIMIR que significaria que o
    // comando atual é de Saida
    if (match(Tipotoken.PCImprimir)) {
      System.out.println("==Comando Saida==");

      // Checa se foi passada uma variável ou cadeia para impressão printando erro
      // caso contrario
      if (!match(Tipotoken.Var)) {
        match(Tipotoken.Cadeia, "Variavel ou Cadeia para saída");
      }
      return true;
    }
    // não é comando de saida
    return false;
  }

  // Checa se é comando de condição
  private boolean ComandoCondicao() {
    // Inicializa variaveis para teste de existencia
    boolean exist = false;
    boolean aux1, aux2, aux3;

    // Checa pela existencia de "SE" "Expressao relacional" e "ENTAO" guardando os
    // resultados nas variaveis auxiliares
    aux1 = match(Tipotoken.PCSe);
    buf.append("if(");
    aux2 = ExpressaoRelacional();
    aux3 = match(Tipotoken.PCEntao);

    // Caso SE ou ENTAO sejam detectados então é um comando de condição
    if (aux1 || aux3) {
      buf.append("){\n");
      System.out.println("==Comando Condicao==");
      exist = true;
      
      // Caso ENTAO não for encontrado exibe erro e considera que comando não pode ser
      // terminado
      if (!aux3) {
        Erro("ENTAO");
        return false;

        // Caso "SE" não seja encontrado retorna erro
      } else if (!aux1) {
        Erro("SE");
      }
    }

    // Caso comando existir
    if (exist) {
      // Retorna erro se a expressão relacional não for encontrada
      if (!aux2) {
        Erro("Expressao Relacional para o Se");
      }
      // Tira Tokens lidos com sucesso
      matchAll();

      System.out.println("\n");
      // Testa se comando existe depois de ENTAO exibindo erro
      comandos.add(buf.toString());
      buf.setLength(0);
      if (!Comando()) {
        Erro("Comando do condicional");
      }

      // Checa se SENAO existe
      if (match(Tipotoken.PCSeNao)) {
        // Tira SENAO da lista de tokens
        matchAll();

        System.out.println("\n");
        // Testa se comando existe depois de SENAO exibindo erro
        if (!Comando()) {
          Erro("Comando do senão");
        }
      }
    }
    // Retorna se existe
    return exist;
  }

  // Checa Expressão relacional
  private boolean ExpressaoRelacional() {
    // Checa se não checou a um Então e se tem termo relacional
    if ((VetToken.get(i).nome != Tipotoken.PCEntao) && TermoRelacional()) {

      // Chama checagem por termo booleano
      OperadorBooleano();

      // Chama checagem por expressão relacional
      ExpressaoRelacional();
      return true;

    }
    
    // Expressão relacional não existe
    return false;
  }

  // Checa por operador booleano
  private void OperadorBooleano() {
    // Checa se tem "E"ou "OU"
    if (!match(Tipotoken.OpBoolE)) {
      if(match(Tipotoken.OpBoolOu)){
        buf.append(" || ");}
    }else{buf.append(" && ");}
  }

  // Checa por Termo relacional
  private boolean TermoRelacional() {

    // Checa se começa com parenteses
    if (match(Tipotoken.AbrePar)) {
      buf.append("(");
      // Testa por outros termos relacionais
      TermoRelacional();

      // Checa por fim do parenteses
      if(match(Tipotoken.FechaPar, "Parenteses para fechar")){
        buf.append(")");
        return true;
      }
      return false;
    }

    // inicializa variaveis para teste de existencia
    boolean exist = false;
    boolean aux1, aux2, aux3;

    // Checa por "expressão aritmética", "Relacional" e "Expressao aritmética" e
    // armazena resultado nas variaveis
    aux1 = ExpressaoAritmetica();
    aux2 = Relacional();
    aux3 = ExpressaoAritmetica();

    // Caso algum deles exista significa que o Termo relacional existe
    if (aux1 || aux2 || aux3) {
      exist = true;

      // Falta expressão antes
      if (!aux1 && aux2) {
        Erro("Expressao Aritmetica antes do relacional");
      }

      // Falta relacional entre expressões
      if (!aux2 && aux1 && aux3) {
        Erro("Relacional entre expressões aritmeticas");
      }

      // falta expressão depois
      if (!aux3 && aux2) {
        Erro("Expressao Aritmetica Depois do Relacional");
      }
    }

    // retorna existencia
    return exist;
  }

  // Checa Relacional
  private boolean Relacional() {
    // Caso exista "==", "<", "<=", ">", ">=" ou "!=" retorna true
    if (match(Tipotoken.OpRelIgual) || match(Tipotoken.OpRelMenor) || match(Tipotoken.OpRelMenorIgual)
        || match(Tipotoken.OpRelMaior) || match(Tipotoken.OpRelMaiorIgual) || match(Tipotoken.OpRelDif)) {
      buf.append(VetToken.get(i-1).lexema);
      return true;
    }

    // Não existe
    return false;
  }

  // Checa comando de repetição
  private boolean ComandoRepeticao() {
    // Caso encontre ENQTO é um comando de repetição
    if (match(Tipotoken.PCEnqto)) {
      buf.append("while(");
      System.out.println("==Comando Repetição==");

      // Checa por expressão relacional exibindo erro se não existir
      if (!ExpressaoRelacional()) {
        
        Erro("Expressão relacional");
      }
      buf.append("){\n");
      comandos.add(buf.toString());
      buf.setLength(0);
      
      System.out.println("\n");
      // Testa se comando existe depois de ENTAO exibindo erro
      if (!Comando()) {
        Erro("Comando do condicional");
      }
      return true;
    }

    // não é comando de execução
    return false;
  }

  // Checa por subalgoritmo
  private boolean SubAlgoritmo() {
    
    // Checa por INI que determina começo de subalgoritmo
    if (match(Tipotoken.PCIni)) {
      inside = 1;
      System.out.print("\n==Inicio SubAlgoritmo==\n");
      matchAll();

      System.out.println("\n");
      // Procura por lista de comandos usando como condicional o token FIM
      ListaComandos(Tipotoken.PCFim);

      // da match no token FIM exibindo possivel erro
      match(Tipotoken.PCFim, "PCFim");
      return true;
    }

    // não é subalgoritmo
    return false;
  }

  // Checa expressão Aritmética
  private boolean ExpressaoAritmetica() {
    // Checa se existe um termo
    if (TermoAritmetico()) {
      // checa se por "+" ou "-"
      if (!match(Tipotoken.OpAritSoma)) {
        if (match(Tipotoken.OpAritSub)) {
          buf.append(" - ");
          ExpressaoAritmetica();
        }
      } else {
        buf.append(" + ");
        ExpressaoAritmetica();
      }
      // chama outra expressão Aritmética ate que não existam mais termos

      return true;
    }

    // Não identificou exoressão aritmética
    return false;
  }

  // Checa por termo aritmérica
  private boolean TermoAritmetico() {

    // Se existir um fator Aritmético o termo existe
    if (FatorAritmetico()) {

      // Checa por multiplicação ou divisão procurando outro termo Aritmético se um
      // existir
      if (!match(Tipotoken.OpAritMult)) {
        if (match(Tipotoken.OpAritDiv)) {
          buf.append(" / ");
          TermoAritmetico();
        }
      } else {
        buf.append(" * ");
        TermoAritmetico();
      }
      return true;
    }

    // Termo Aritmético não existe
    return false;
  }

  // Checa por Fator Aritmético
  private boolean FatorAritmetico() {

    // Checa se é um numero inteiro, Real, uma variavel ou uma expressão aritmética
    // entre parenteses
    if (!match(Tipotoken.NumInt)) {
      if (!match(Tipotoken.NumReal)) {
        if (!match(Tipotoken.Var)) {
          if (match(Tipotoken.AbrePar)) {
            buf.append(" (");
            ExpressaoAritmetica();
            buf.append(")");
            return match(Tipotoken.FechaPar);
          }
        } else {
          buf.append(VetToken.get(i-1).lexema);
          return true;
        }
      } else {
        buf.append(VetToken.get(i-1).lexema);
        return true;
      }
    } else {
      buf.append(VetToken.get(i-1).lexema);
      return true;
    }

    // Não existe Fator Aritmético
    return false;
  }

}