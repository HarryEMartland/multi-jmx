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
public class ComparatorTest {

    private static char displayCountChar = 0;

    public Comparator<JMXConnectionResponse> victim;

    public ComparatorTest(Class<Comparator<JMXConnectionResponse>> comparatorClass) throws IllegalAccessException, InstantiationException {
        victim = comparatorClass.newInstance();
    }

    @Parameterized.Parameters
    public static List<Class<? extends Comparator<JMXConnectionResponse>>> parameters() {
        return Arrays.asList(JMXConnectionResponse.DisplayComparator.class, JMXConnectionResponse.ValueComparator.class);
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparator() throws Exception {
        JMXConnectionResponse response = createResponse(1);
        JMXConnectionResponse response2 = createResponse(2);
        JMXConnectionResponse response3 = createResponse(3);
        Assert.assertEquals(Arrays.asList(response, response2, response3),
                Arrays.asList(response, response2, response3).stream().sorted(victim).collect(Collectors.toList()));
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparatorNotInOrder() throws Exception {
        JMXConnectionResponse response = createResponse(1);
        JMXConnectionResponse response2 = createResponse(2);
        JMXConnectionResponse response3 = createResponse(3);

        Assert.assertEquals(Arrays.asList(response, response2, response3),
                Arrays.asList(response3, response2, response).stream().sorted(victim).collect(Collectors.toList()));
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparatorNotInOrderMultipleValues() throws Exception {
        JMXConnectionResponse response = createResponse(1, 1);
        JMXConnectionResponse response2 = createResponse(2, 2);
        JMXConnectionResponse response3 = createResponse(3, 3);

        Assert.assertEquals(Arrays.asList(response, response2, response3),
                Arrays.asList(response3, response2, response).stream().sorted(victim).collect(Collectors.toList()));
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparatorNotInOrderMultipleValuesWithSameFirst() throws Exception {
        JMXConnectionResponse response = createResponse(1, 1);
        JMXConnectionResponse response2 = createResponse(1, 2);
        JMXConnectionResponse response3 = createResponse(2, 3);

        Assert.assertEquals(Arrays.asList(response, response2, response3),
                Arrays.asList(response3, response2, response).stream().sorted(victim).collect(Collectors.toList()));
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparatorNotInOrderAndWithException() throws Exception {
        JMXConnectionResponse response = createResponse(1);
        JMXConnectionResponse response2 = createResponse(2);
        JMXConnectionResponse response3 = createResponse(3);
        JMXConnectionResponse error1 = createErrorResponse();

        Assert.assertEquals(Arrays.asList(error1, response, response2, response3),
                Arrays.asList(response3, response2, response, error1).stream().sorted(victim).collect(Collectors.toList()));
    }

    @Test
    public void testShouldReturnValuesInOrderWhenUsingValueComparatorNotInOrderAndWithExceptionReversed() throws Exception {
        JMXConnectionResponse response = createResponse(1);
        JMXConnectionResponse response2 = createResponse(2);
        JMXConnectionResponse response3 = createResponse(3);
        JMXConnectionResponse error1 = createErrorResponse();

        Assert.assertEquals(Arrays.asList(error1, response3, response2, response),
                Arrays.asList(response3, response2, response, error1).stream().sorted(victim.reversed()).collect(Collectors.toList()));
    }

    private JMXConnectionResponse createResponse(Comparable... order) {
        return new JMXConnectionResponse("display" + displayCountChar++,
                Arrays.asList(order).stream().map(JMXValueResult::new).collect(Collectors.toList()));
    }

    private JMXConnectionResponse createErrorResponse() {
        return new JMXConnectionResponse("display" + displayCountChar++, new Exception());
    }

}