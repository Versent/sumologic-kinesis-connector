package com.sumologic.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class SumologicSenderTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    private static final String MOCKED_HOST = "http://localhost:8089";
    private static final String MOCKED_COLLECTION = "/sumologic/collections/1234";


    @Before
    public void setUp() {
        mockEmitMessages();
    }

    @Test
    public void theSenderShouldReturnFalseWhenFailing() {
        String url = MOCKED_HOST + "/sumologic/collections/fake-url";

        String data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "\nIn nisl tortor, dictum nec tristique ut, tincidunt vitae tortor. "
                + "\nNam vitae urna ac sem vulputate dignissim at ac nibh. ";

        SumologicSender sender = new SumologicSender(url);
        try {
            boolean response = sender.sendToSumologic(data);
            Assert.assertFalse(response);
        } catch (IOException e) {
            Assert.fail("Got an exception during test: " + e.getMessage());
        }
    }

    @Test
    public void theSenderShouldReturnTrueOnSuccess() {
        String url = MOCKED_HOST + MOCKED_COLLECTION;

        String data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "\nIn nisl tortor, dictum nec tristique ut, tincidunt vitae tortor. "
                + "\nNam vitae urna ac sem vulputate dignissim at ac nibh. ";

        SumologicSender sender = new SumologicSender(url);
        try {
            boolean response = sender.sendToSumologic(data);
            Assert.assertTrue(response);
        } catch (IOException e) {
            Assert.fail("Got an exception during test: " + e.getMessage());
        }
    }


    private void mockEmitMessages() {
        WireMock.stubFor(WireMock.post(WireMock.urlMatching(MOCKED_COLLECTION))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody("")));
    }

}