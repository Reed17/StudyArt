import ua.artcode.utils.dynamic_compile.MethodInvoker;

public class _sum98020 implements MethodInvoker {

    public _sum98020() {
    }

    @Override
    public Object call(Object...args) {
                int arg0 = (int) args[0];
                int arg1 = (int) args[1];
                return sum(arg0,arg1);
    }

public int sum(int a, int b){return a+b;}

}
