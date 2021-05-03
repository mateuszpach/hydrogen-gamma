public class Variable {
    enum Type {
        NUMBER,
        FUNCTION,
        MATRIX,
        TEXT
    }
    /*TODO:
       1. Lepszy constructor, np. (Type,Object), rzutuje patzrąc na dany enum, bo teraz jest możliwość otrzymania niezainicjalizowanej zmiennej
       2. Efektywnie pozwoli to zrobić całą zmienną final i usunie set()
       3. By zrzutować object na Double[][], trzeba znać jej rozmiar: Jakiś Array<Array> lub klasa na matrix?
       4. Testy dla Variable
    */
    final Type type;
    Variable(Type type){
        this.type=type;
    }

    double number;
    Func function;
    double[][] matrix;
    String text;
    Variable set(double number){
        if(this.type!=Type.NUMBER)
            return null;
        this.number=number;
        return this;
    }
    Variable set(Func function){
        if(this.type!=Type.FUNCTION)
            return null;
        this.function=function;
        return this;
    }
    Variable set(double[][] matrix){
        if(this.type!=Type.MATRIX)
            return null;
        this.matrix=matrix.clone();
        return this;
    }
    Variable set(String text){
        if(this.type!=Type.TEXT)
            return null;
        this.text=text;
        return this;
    }
}
