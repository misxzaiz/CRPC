package chen.shangquan.utils.object;

import chen.shangquan.utils.object.po.P;
import chen.shangquan.utils.object.po.stackOverflowError.Person;
import org.junit.jupiter.api.Test;

public class ObjectResolutionTest {

    /**
     * Person 和 Friend 相互调用，存在 StackOverflowError
     */
    @Test
    public void testGetObjectResolution() throws ClassNotFoundException {
        String objectResolution = ObjectResolution.getObjectResolution(Person.class);
        System.out.println(objectResolution);
    }

    @Test
    public void testGetObjectResolution1() throws ClassNotFoundException {
        String objectResolution = ObjectResolution.getObjectResolution(P.class);
        System.out.println(objectResolution);
    }
}
