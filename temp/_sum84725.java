import ua.artcode.utils.dynamic_compile.MethodInvoker;

public class _sum84725 implements MethodInvoker {

    public _sum84725() {
    }

    @Override
    public Object call(Object...args) {
                Double arg0 = (Double) args[0];
                Double arg1 = (Double) args[1];
                return sum(arg0,arg1);
    }

public double sum(double a, double b){return a+b;}

}
