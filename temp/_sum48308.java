import ua.artcode.utils.dynamic_compile.MethodInvoker;

public class _sum48308 implements MethodInvoker {

    public _sum48308() {
    }

    @Override
    public Object call(Object...args) {
                byte arg0 = (byte) args[0];
                byte arg1 = (byte) args[1];
                return sum(arg0,arg1);
    }

public short sum(short a, short b){return a+b;}

}
