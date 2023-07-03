import java.util.ArrayList;

// Classe leitora do arquivo
public class AnalisadorSemantico {

  public ArrayList<Token> vard;
  public ArrayList<Token> vart;
  public ArrayList<Token> vare;
  public ArrayList<Token> NumI;
  public ArrayList<Token> NumR;
  public ArrayList<Tipotoken> VarAtrib;
  public ArrayList<Tipotoken> ValAtrib;
  public ArrayList<Token> nomeAtribs;
  public int[] Usado;

  public AnalisadorSemantico(LeitorTokens AS) {
    this.vard = AS.VarD;
    this.vart = AS.TipVarD;
    this.vare = AS.VarE;
    this.NumI = AS.numsInt;
    this.NumR = AS.numsReal;
    VarAtrib = AS.VarAtrib;
    ValAtrib = AS.TipVarAtrib;
    nomeAtribs = AS.nomVarAtrib;
    Usado = new int[vard.size()];
    for (int i = 0; i < vard.size(); i++) {
      Usado[i] = 0;
    }
  }

  public void Analise() {
    Variaveis();
    FormatosAtribs();
    Numeros();
  }

  // Checa se todas as variaveis usadas foram declaradas
  private void Variaveis() {
    //Variaveis executadas
    for (int i = 0; i < vare.size(); i++) {
      //Variaveis declaradas
      for (int j = 0; j < vard.size(); j++) {
        //Se a a executada é igual a declarada
        if (vare.get(i).lexema.equals(vard.get(j).lexema)) {
          Usado[j] = 1;
          break;
        }

        //Se não existe nenhuma variavel declarada igual a executada
        if (j == vard.size() - 1) {
          System.out.println("\nErro Semântico: Variavel \"" + vare.get(i).lexema + "\" não foi declarada.");
        }
      }
    }

    //Ve se alguma variavel declarada não foi usada
    for (int i = 0; i < vard.size(); i++) {
      if (Usado[i] == 0) {
        System.out.print("\nWarning: Variavel \"" + vard.get(i).lexema + "\"  não foi utilizada.\n");
      }
    }
  }

  // Checa se tamanho dos numeros está dentro do padrão do programa
  private void Numeros() {
    int jm18 = 0;

    // Procura Erros nos valores Inteiros
    for (int i = 0; i < NumI.size(); i++) {
      if (NumI.get(i).lexema.length() <= 10) {
        double aux = Double.parseDouble(NumI.get(i).lexema);
        if (aux > 2147483647 || aux < -2147483648) {
          System.out.println("\nErro Semântico: Valor \"" + NumI.get(i).lexema + "\" causou Overflow");
        }
      } else {
        System.out.println("\nErro Semântico: Valor \"" + NumI.get(i).lexema + "\" causou Overflow");
      }
    }

    // Procura erros nos valores reais
    for (int i = 0; i < NumR.size(); i++) {
      int j = 0;
      // Checa todos os valores antes do ponto
      for (j = 0; NumR.get(i).lexema.charAt(j) != '.'; j++) {
        // Se tiver mais que 18 grande de mais
        if (j > 18) {
          System.out.println("\nErro Semântico: Valor \"" + NumR.get(i).lexema + "\" causou Overflow");
          break;
        }
        // Se for igual a 18 checa se ele é diferente de 10^18
        if (j == 18 && NumR.get(i).lexema.charAt(j + 1) == '.') {
          // Se o primeiro caracter é 1
          if (NumR.get(i).lexema.charAt(0) == '1') {
            // Passa por outros valores checando se algum é diferente de zero
            for (int h = 1; NumR.get(i).lexema.charAt(h) != '.'; h++) {
              if (NumR.get(i).lexema.charAt(h) != '0') {
                System.out.println("\nErro Semântico: Valor \"" + NumR.get(i).lexema + "\" causou Overflow");
                break;
              }
            }
          } else {
            Erronum(NumR.get(i).lexema, "Overflow");
          }
        }
      }

      if (NumR.get(i).lexema.charAt(j) != '.') {
        for (j = j+1; NumR.get(i).lexema.charAt(j) != '.'; j++) {
          jm18 = j + 18;
        }
      }
      jm18 = j + 18;
      // Passa por todos os valores a direita da virgula
      for (j = j + 1; j < NumR.get(i).lexema.length(); j++) {
        if (j > jm18) {
          
          Erronum(NumR.get(i).lexema, "Underflow");
          break;
        }
      }

    }
  }

  // Printa erro numero
  private void Erronum(String num, String e) {
    System.out.println("\nErro Semântico: Valor \"" + num + "\" causou " + e);
  }

  // Procura por erro nos formatos da atribuição
  private void FormatosAtribs() {
    // Passar por formato das variaveis que recebem atribuição
    for (int i = 0; i < VarAtrib.size(); i++) {

      
      if (VarAtrib.get(i) == Tipotoken.NumInt && VarAtrib.get(i) != ValAtrib.get(i)) {
        System.out.print("\nErro Semântico: Na atribuição para a variavel \"" + nomeAtribs.get(i).lexema
            + "\" valor Real passado, esperado valor Inteiro.\n");
      }
    }
  }
}