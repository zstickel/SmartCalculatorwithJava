import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface PrintArgs {
    boolean printReturn();
}

class TestClass {
    @PrintArgs(printReturn = true)
    public void myMethod() {
        // some code
    }
}