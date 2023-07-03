public class Gyhlex{
  public LeitorArquivo ldat;
  public char Tempc;

  public Gyhlex(String Arq){
    //Cria leitor de arquivo e coloca um espaço vazio em Tempc ao inicializar objeto dessa classe
    ldat = new LeitorArquivo(Arq);
    Tempc = ' ';
  }

  public Token proximoToken(){
    int caracterelido = 0;
    char c;
    if(Tempc != ' ' && Tempc != '\t' && (int)Tempc != 13){
        c = Tempc;
        Tempc = ' ';
      }else{
        caracterelido = ldat.lerProximoCaracter();
        c = (char)caracterelido;
      }
    while((caracterelido) !=-1){

      
//Ignora espaços vazios e novalinhas
      if(c== ' ' || c=='\t' || (int)c == 13){ 
        caracterelido = ldat.lerProximoCaracter();
        c = (char)caracterelido;
        continue;}
      
      //Analisa qual o primeiro caracter
      switch(c){
        case '\n':
          return new Token(Tipotoken.BrL,"NewLine");
          
        //No caso de ser um comentário le todos os caracteres até encontrar o final da linha
        case '#':
          while(c !='\n'){
            caracterelido = ldat.lerProximoCaracter();
            c = (char)caracterelido;
          }
          continue;

          //Checa Operações Aritméticas
        case '-': return new Token(Tipotoken.OpAritSub,"-");
       	case '+': return new Token(Tipotoken.OpAritSoma,"+");
        case '*': return new Token(Tipotoken.OpAritMult,"*");
        case '/': return new Token(Tipotoken.OpAritDiv,"/");

          //Real
      case 'R':
          c = (char)ldat.lerProximoCaracter();
            if(c == 'E'){
              c = (char)ldat.lerProximoCaracter();
              if(c == 'A'){
                c = (char)ldat.lerProximoCaracter();
                if(c == 'L'){
                  return new Token(Tipotoken.PCReal,"REAL");}}}
        return new Token(Tipotoken.Erro,"Erro");


          
        //Checa sequencia de palavras SE
        case 'S': 
          c = (char)ldat.lerProximoCaracter();
          if(c == 'E'){
            c = (char)ldat.lerProximoCaracter();
            if(c == 'N'){
              c = (char)ldat.lerProximoCaracter();
              if(c == 'A'){
                c = (char)ldat.lerProximoCaracter();
                if(c == 'O'){
                  return new Token(Tipotoken.PCSeNao,"SENAO");
                }
              }
            }else{
              return new Token(Tipotoken.PCSe,"SE");
              }
          }
          return new Token(Tipotoken.Erro,"Erro");
          
        // E ENTAO ENQTO
        case 'E': 
          c = (char)ldat.lerProximoCaracter();
          if(c == ' '){
          return new Token(Tipotoken.OpBoolE,"E");}
          else if(c == 'N'){
            c = (char)ldat.lerProximoCaracter();
            if(c == 'T'){
                c = (char)ldat.lerProximoCaracter();
              if(c == 'A'){
                c = (char)ldat.lerProximoCaracter();
                if(c == 'O'){
                  return new Token(Tipotoken.PCEntao,"ENTAO");
          }}}
            //Checa ENQTO
            else if(c == 'Q'){
              c = (char)ldat.lerProximoCaracter();
                if(c == 'T'){
                  c = (char)ldat.lerProximoCaracter();
                if(c == 'O'){
                  return new Token(Tipotoken.PCEnqto,"ENQTO");
          }}}}
          return new Token(Tipotoken.Erro,"Erro");
  
        //OU
        case 'O': 
          //Testar se proximo é U
          c = (char)ldat.lerProximoCaracter();
          if(c == 'U') return new Token(Tipotoken.OpBoolOu,"OU");
        return new Token(Tipotoken.Erro,"Erro");

          //Menor ou Menor ou igual
       case '<':
          //Le proximo char;
          c = (char)ldat.lerProximoCaracter();
          //Testar se proximo é vazio ou =
          
          if(c == '=') {
            return new Token(Tipotoken.OpRelMenorIgual,"<=");}
          else{ 
            Tempc = c;
            return new Token(Tipotoken.OpRelMenor,"<");
            }
          
        //Maior ou maior ou igual
        case '>':
          c = (char)ldat.lerProximoCaracter();
          if(c == '='){ 
            return new Token(Tipotoken.OpRelMaiorIgual,">=");}
          else{ 
            Tempc = c;
            return new Token(Tipotoken.OpRelMaior,">");
            }

          //Igual logica
          case '=':
            c = (char)ldat.lerProximoCaracter();
            //Testar se proximo é vazio ou =
            if(c == '='){ 
              return new Token(Tipotoken.OpRelIgual,"==");
              } 
            return new Token(Tipotoken.Erro,"Erro");

          //Diferente 
          case '!':
            c = (char)ldat.lerProximoCaracter();
            //Testar se proximo é vazio ou =
            if(c == '='){ return new Token(Tipotoken.OpRelDif,"!="); }
          return new Token(Tipotoken.Erro,"Erro");

          //Atribuição ou Delin
         case ':': 
          c = (char)ldat.lerProximoCaracter();
          // Atribuição 
          if(c == '='){ 
            return new Token(Tipotoken.Atrib,":=");
            }
          //Delin
          else{
            Tempc = c;
            return new Token(Tipotoken.Delim,":");
            }
          
        //DEC
        case 'D':
          c = (char)ldat.lerProximoCaracter();
            if(c == 'E'){
              c = (char)ldat.lerProximoCaracter();
              if(c == 'C'){
                return new Token(Tipotoken.PCDec,"DEC");}}
          return new Token(Tipotoken.Erro,"Erro");
          
//      PROG
        case 'P':
          c = (char)ldat.lerProximoCaracter();
            if(c == 'R'){
              c = (char)ldat.lerProximoCaracter();
              if(c == 'O'){
                c = (char)ldat.lerProximoCaracter();
                if(c == 'G')
                  return new Token(Tipotoken.PCProg,"PROG");
                }
              }
            return new Token(Tipotoken.Erro,"Erro");


//      INT/INI/IMPRIMIR
        case 'I':
          c = (char)ldat.lerProximoCaracter();
            if(c == 'N'){
              c = (char)ldat.lerProximoCaracter();
              if(c == 'T'){
                  return new Token(Tipotoken.PCInt,"INT");}
              else if(c == 'I'){
                return new Token(Tipotoken.PCIni,"INI");                
                  }
            }else if(c == 'M'){
              c = (char)ldat.lerProximoCaracter();
              if(c == 'P'){
                c = (char)ldat.lerProximoCaracter();
                if(c == 'R'){
                  c = (char)ldat.lerProximoCaracter();
                if(c == 'I'){
                  c = (char)ldat.lerProximoCaracter();
                if(c == 'M'){
                  c = (char)ldat.lerProximoCaracter();
                if(c == 'I'){
                  c = (char)ldat.lerProximoCaracter();
                if(c == 'R'){
                  return new Token(Tipotoken.PCImprimir,"IMPRIMIR");}}}}
                  }
                }
              }
        return new Token(Tipotoken.Erro,"Erro");

//      LER
        case 'L':
          c = (char)ldat.lerProximoCaracter();
            if(c == 'E'){
              c = (char)ldat.lerProximoCaracter();
              if(c == 'R'){
                  return new Token(Tipotoken.PCLer,"LER");}}
        return new Token(Tipotoken.Erro,"Erro");

//      FIM
        case 'F':
          c = (char)ldat.lerProximoCaracter();
            if(c == 'I'){
              c = (char)ldat.lerProximoCaracter();
              if(c == 'M'){
                  return new Token(Tipotoken.PCFim,"FIM");}}

        //Reseta o c para não ser lido em mais nada mas deveria soltar um erro
        return new Token(Tipotoken.Erro,"Erro");
      

              
        case '(':
          return new Token(Tipotoken.AbrePar,"(");
        case ')':
          return new Token(Tipotoken.FechaPar,")");

      }
      
      //Usando Tabela ASCII checa se está no alfabeto minusculo
      if(caracterelido >=97  && caracterelido <=122){
        StringBuilder palavra =  new StringBuilder(c);
          palavra.append(c);
          
          caracterelido = ldat.lerProximoCaracter();
          c = (char)caracterelido;
          while(c!= ':' &&c!= ' ' && c != '\n'&& ((caracterelido >=97  && caracterelido <=122) || (caracterelido >=65  && caracterelido <=90) || (caracterelido >=48  && caracterelido <=57))){
            
            palavra.append(c);
            caracterelido = ldat.lerProximoCaracter();
            c = (char)caracterelido;
          }
          Tempc = c;

          String pa = palavra.toString();
          return new Token(Tipotoken.Var,pa);
      }

      //Numero Inteiro ou float
      if(caracterelido >=48  && caracterelido <=57){
        
        StringBuilder num =  new StringBuilder(c);
        String pa;
          num.append(c);
          
          caracterelido = ldat.lerProximoCaracter();
          c = (char)caracterelido;
          while((caracterelido >=48  && caracterelido <=57) || c == '.'){

            if(caracterelido >=48  && caracterelido <=57){
              num.append(c);
              caracterelido = ldat.lerProximoCaracter();
              c = (char)caracterelido;
              }
            else if(c == '.'){
                num.append(c);
                caracterelido = ldat.lerProximoCaracter();
                c = (char)caracterelido;
                while((caracterelido >=48  && caracterelido <=57) || c == '.'){

                  if(caracterelido >=48  && caracterelido <=57){
                    num.append(c);
                    caracterelido = ldat.lerProximoCaracter();
                    c = (char)caracterelido;
                    }
                  }
                  Tempc = c;
                  pa = num.toString();
                  return new Token(Tipotoken.NumReal,pa);
                }else{
              break;
                }
            }
          Tempc = c;
          pa = num.toString();
          return new Token(Tipotoken.NumInt,pa);
      }

      //String
      if(c == '\"'){
        StringBuilder str =  new StringBuilder(c);
          str.append(c);
          
          c = (char)ldat.lerProximoCaracter();
          while(c!= '\"'){
            if(c == '\n'){
              return new Token(Tipotoken.Erro,"Erro");
            }
            str.append(c);
            c = (char)ldat.lerProximoCaracter();
          }
          str.append(c);
          
          String pa = str.toString();
          return new Token(Tipotoken.Cadeia,pa);
      }

      //O caracter não faz parte da linguagem
    
      return new Token(Tipotoken.Erro,"Erro1");
    }
    return new Token(Tipotoken.EOF,"CABO");
  }
  
}