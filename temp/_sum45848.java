import ua.artcode.utils.dynamic_compile.MethodInvoker;

public class _sum45848 implements MethodInvoker {

    public _sum45848() {
    }

    @Override
    public Object call(Object...args) {
                long arg0 = (long) args[0];
                long arg1 = (long) args[1];
                return sum(arg0,arg1);
    }

public long sum(long a, long b){return a-b;}

}
