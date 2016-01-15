package uk.co.harrymartland.multijmx.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class DisplayComparatorComparatorTest {

    private static char displayCountChar = 0;

    public Comparator<JMXResponse> victim;

    public DisplayComparatorComparatorTest(Class<Comparator<JMXResponse>> comparatorClass) throws IllegalAccessException, InstantiationException {
        victim = comparatorClass.newInstance();
    }

    @Parameterized.Parameters
    public static List<Class<? extends Comparator<JMXResponse>>> parameters() {
        return Arrays.asList(JMXResponse.DisplayComparator.class, JMXResponse.ValueComparator.class);
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparator() throws Exception {
        JMXResponse response = createResponse(1);
        JMXResponse response2 = createResponse(2);
        JMXResponse response3 = createResponse(3);
        Assert.assertEquals(Arrays.asList(response, response2, response3), Arrays.asList(response, response2, response3).stream().sorted(victim).collect(Collectors.toList()));
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparatorNotInOrder() throws Exception {
        JMXResponse response = createResponse(1);
        JMXResponse response2 = createResponse(2);
        JMXResponse response3 = createResponse(3);

        Assert.assertEquals(Arrays.asList(response, response2, response3), Arrays.asList(response3, response2, response).stream().sorted(victim).collect(Collectors.toList()));
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparatorNotInOrderAndWithException() throws Exception {
        JMXResponse response = createResponse(1);
        JMXResponse response2 = createResponse(2);
        JMXResponse response3 = createResponse(3);
        JMXResponse error1 = createErrorResponse();

        Assert.assertEquals(Arrays.asList(error1, response, response2, response3), Arrays.asList(response3, response2, response, error1).stream().sorted(victim).collect(Collectors.toList()));
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparatorNotInOrderAndWithExceptionReversed() throws Exception {
        JMXResponse response = createResponse(1);
        JMXResponse response2 = createResponse(2);
        JMXResponse response3 = createResponse(3);
        JMXResponse error1 = createErrorResponse();

        Assert.assertEquals(Arrays.asList(error1, response, response2, response3), Arrays.asList(response3, response2, response, error1).stream().sorted(victim.reversed()).collect(Collectors.toList()));
    }

    private JMXResponse createResponse(int order) {
        return new JMXResponse("display" + displayCountChar++, order);
    }

    private JMXResponse createErrorResponse() {
        return new JMXResponse("display" + displayCountChar++, new Exception());
    }

}