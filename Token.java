public class Token{
  public Tipotoken nome;
  public String lexema;
  public Token(Tipotoken nome, String lexema){
    this.nome = nome;
    this.lexema = lexema;
  }
  @Override
  public String toString(){
    return ("<"+nome+",\""+lexema+"\">");
  }
}