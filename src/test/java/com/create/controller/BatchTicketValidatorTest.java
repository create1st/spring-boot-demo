/*
 * Copyright  2016 Sebastian Gil.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.create.controller;

import com.create.application.configuration.ServiceConfiguration;
import com.create.application.configuration.TestConfiguration;
import com.create.application.configuration.ValidatorConfiguration;
import com.create.application.configuration.WebConfiguration;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

@RunWith(Theories.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
        TestConfiguration.class,
        ValidatorConfiguration.class,
        WebConfiguration.class,
        ServiceConfiguration.class}
)
public class BatchTicketValidatorTest {
    private static final String VALIDATOR_TRADE_URL = "/validator/batch";

    @Autowired
    private MockMvc mvc;

    @Before
    public void before() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @DataPoints
    public static String[] getTestNames() throws IOException {
        return new RestTestNameProvider()
                .getTestNames(VALIDATOR_TRADE_URL);
    }

    @Theory
    public void shouldTestBatchTicketValidatorRequest(String testName) throws Exception {
        final RestTestExecutor restTestExecutor = new RestTestExecutor(mvc);
        restTestExecutor.executeTestRestRequest(VALIDATOR_TRADE_URL, testName);
    }
}