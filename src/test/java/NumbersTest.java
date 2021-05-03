import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

public class NumbersTest {
    Random rand=new Random();
    Module add=new addNumbers();
    @Test
    void binaryAdd(){
        double aD= rand.nextDouble();
        Variable aV=new Variable(Variable.Type.NUMBER).set(aD);
        double bD= rand.nextDouble();
        Variable bV=new Variable(Variable.Type.NUMBER).set(bD);
        Variable sum=add.call(aV,bV);
        assertEquals(sum.number,aD+bD);
    }
    @Test
    void emptyAdd(){//pretty useless, but it is a certain corner case
        assertEquals(0d,add.call().number);
    }
    @Test
    void chainAdd(){
        int testSize=1500;

        double sum=0;
        Variable[] args=new Variable[testSize];
        for(int i=0;i<testSize;++i){
            double a= rand.nextDouble();
            args[i]= new Variable(Variable.Type.NUMBER).set(a);
            sum+=a;
        }
        System.out.println(sum+" "+add.call(args).number);
        assertEquals(sum,add.call(args).number);
    }


}

