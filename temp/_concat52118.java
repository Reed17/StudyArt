import ua.artcode.utils.dynamic_compile.MethodInvoker;

public class _concat52118 implements MethodInvoker {

    public _concat52118() {
    }

    @Override
    public Object call(Object...args) {
                String arg0 = (String) args[0];
                int arg1 = (int) args[1];
                return concat(arg0,arg1);
    }

public String concat(String word, int b){return word+b;}

}
