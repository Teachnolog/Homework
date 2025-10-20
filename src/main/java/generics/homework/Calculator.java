package generics.homework;

public class Calculator<T extends Number> {
    public double sum(T a, T b) {
        if (a == null || b == null) {
            return Double.NaN;
        }
        return a.doubleValue() + b.doubleValue();
    }

    public double subtract(T a, T b) {
        if (a == null || b == null) {
            return Double.NaN;
        }
        return a.doubleValue() - b.doubleValue();
    }

    public double multiply(T a, T b) {
        if (a == null || b == null) {
            return Double.NaN;
        }
        return a.doubleValue() * b.doubleValue();
    }

    public double divide(T a, T b) {
        if (a == null || b == null) {
            return Double.NaN;
        }
        if (b.doubleValue() == 0.0) {
            return Double.NaN;
        }
        return a.doubleValue() / b.doubleValue();
    }

    public static void main(String[] args) {
        final Calculator<Integer> intCalc = new Calculator<>();
        final double result = intCalc.sum(5, 3);
        System.out.println("Integer sum: " + result);

        final Calculator<Double> doubleCalc = new Calculator<>();
        final double div = doubleCalc.divide(10.0, 4.0);
        System.out.println("Double division: " + div);

        final double zeroDiv = doubleCalc.divide(10.0, 0.0);
        System.out.println("Division by zero: " + zeroDiv);

        final double nullResult = intCalc.sum(null, 5);
        System.out.println("Null argument result: " + nullResult);

        System.out.println("Multiplication: " + intCalc.multiply(4, 3));
        System.out.println("Subtraction: " + intCalc.subtract(10, 4));
    }
}