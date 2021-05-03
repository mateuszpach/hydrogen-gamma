public class addNumbers implements Module{
    @Override
    public Variable call(Variable... args) {
        System.out.println(args.length);
        double val=0d;
        for(Variable i: args){
            val+=i.number;
        }
        return new Variable(Variable.Type.NUMBER).set(val);
    }
}
