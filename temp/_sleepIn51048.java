import ua.artcode.utils.dynamic_compile.MethodInvoker;

public class _sleepIn51048 implements MethodInvoker {

    public _sleepIn51048() {
    }

    @Override
    public Object call(Object...args) {
                boolean arg0 = (boolean) args[0];
                boolean arg1 = (boolean) args[1];
                return sleepIn(arg0,arg1);
    }

public boolean sleepIn(boolean weekday, boolean vacation) {
  
return(!weekday||vacation);
}

}
