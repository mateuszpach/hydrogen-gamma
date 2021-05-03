public class AddNumbers implements Module<NumericVariable> {
    @Override
    public NumericVariable call(Variable... args) {
        System.out.println(args.length);
        double sum = 0d;
        for (Variable arg : args) {
            if (arg instanceof NumericVariable) {
                sum += ((NumericVariable) arg).value;
            } else {
                throw new IllegalArgumentException();
            }
        }
        return new NumericVariable(sum);
    }
}
