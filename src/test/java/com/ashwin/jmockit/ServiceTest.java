package com.ashwin.jmockit;

import mockit.*;
import mockit.integration.junit5.JMockitExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(JMockitExtension.class)
public class ServiceTest {
    @Tested
    private Service service;  // Class under test

    @Injectable
    private Repository repository;  // Single mocked instance

//    @Mocked  // Entire object is mocked, will return default value for every method.
//    private Repository repository;

//    @Injectable
//    private Source source;

    @BeforeEach
    public void setUp() {
        System.out.println("ServiceTest: setUp");
    }

    @Test
    public void printTest() {
        service.print("abc");
    }

    @Test
    public void getWordsSyncTest() {
        List<String> fakeWords = Arrays.asList("alpha", "beta", "cupcake");
        new Expectations() {{
            repository.getWordsSync();
            result = fakeWords;
            times = 1;
        }};

        List<String> words = service.getWordsSync();
        System.out.println(words);

        Assertions.assertEquals(fakeWords.size(), words.size());
        for (int i = 0; i < words.size(); i++) {
            Assertions.assertEquals(fakeWords.get(i), words.get(i));
        }
    }

    // Mock static method
    @Test
    public void getNameTest() {
        final String mockName = "mock-test-app";
        new MockUp<Repository>() {
            @Mock
            public String getName() {
                return mockName;
            }
        };

        String actual = service.getRepositoryName();

        Assertions.assertEquals(mockName, actual);
    }

    // Mock for any parameter
    @Test
    public void getWordsContainSync() {
        List<String> fakeWords = Arrays.asList("alpha", "beta", "cupcake");
        new Expectations() {{
            repository.getWordsContainSync(anyString);
            result = fakeWords;
            times = 1;
        }};

        List<String> words = service.getWordsContainSync("abc");

        Assertions.assertEquals(fakeWords.size(), words.size());
    }

    // Mock for different sequence of outputs
    @Test
    public void addTest() {
        int exp1 = 1, exp2 = 2, exp3 = 3;
        new Expectations() {{
            repository.add(anyInt, anyInt);
            returns(exp1, exp2, exp3);
        }};

        int sum1 = service.add(5, 6);
        Assertions.assertEquals(exp1, sum1);

        int sum2 = service.add(50, 60);
        Assertions.assertEquals(exp2, sum2);

        int sum3 = service.add(500, 600);
        Assertions.assertEquals(exp3, sum3);

//        new Verifications() {{
//            // Order does not matter
//            repository.add(500, 600);
//            minTimes = 1;
//            maxTimes = 3;
//
//            repository.add(5, 6);
//            times = 1;
//
//            repository.add(50, 60);
//            times = 1;
//        }};

        new VerificationsInOrder() {{
            // Order matters
            repository.add(5, 6);
            times = 1;

            repository.add(50, 60);
            times = 1;

            repository.add(500, 600);
            minTimes = 1;
            maxTimes = 3;
        }};
    }

    // Delegate demo
    @Test
    public void subTest() {
        new MockUp<Repository>() {
            @Mock
            public int add(int a, int b) {
                return a + b;
            }
        };

        new Expectations() {{
            repository.sub(anyInt, anyInt);
            // We are delegating sub to add because that's what we have mocked :P
            // Never do this in prod!
            result = new Delegate<Repository>() {
                public int sub(int x, int y) {
                    int sum = repository.add(x, y);  // Do not call the same (sub) method here as it can lead to infinite recursion!
                    System.out.println("subTest: Delegate: " + sum);
                    return sum;
                }
            };
        }};

        int actual = service.sub(10, 5);

        Assertions.assertEquals(15, actual);
    }

    // Mock with invocation
    @Test
    public void getAppNameTest() {
        new MockUp<Repository>() {
            @Mock
            public String getAppName(Invocation invocation) {
                // Adding invocation as 1st parameter helps to delegate to the original method.
                // Invocation is different than Delegate, as by using Delegate you can invoke other methods but not the original method.
                // If you use original method, then it will be infinite recursion.
                String appName = invocation.proceed();
                return appName + "-mock";
            }
        };

        String actual = service.getAppName();
        System.out.println("getAppNameTest: actual: " + actual);
    }

    // Capture argument demo
    @Test
    public void getWordsAsyncTest() {
        List<String> fakeWords = Arrays.asList("alpha", "beta", "cupcake");
        new Expectations() {{
            repository.getWordsAsync(withInstanceOf(CompletionHandler.class));
            result = new Delegate<Repository>() {
                public void getWordsAsync(Invocation invocation) {
                    CompletionHandler handler = (CompletionHandler) invocation.getInvokedArguments()[0];
                    handler.onComplete(fakeWords);
                }
            };
        }};
        List<String> actual = new ArrayList<>();
        Observer observer = new Observer() {
            @Override
            public void onChange(List<String> words) {
                System.out.println("observer onChange( " + words + " )");
                actual.addAll(words);
            }
        };
        service.setObserver(observer);

        service.getWordsAsync();

        Assertions.assertTimeout(Duration.ofSeconds(2), () -> {
            Assertions.assertEquals(fakeWords.size(), actual.size());
        });
    }
}
