package me.g1tommy.etfportfolioanalyzer;

import me.g1tommy.etfportfolioanalyzer.controller.ETFController;
import me.g1tommy.etfportfolioanalyzer.service.KRXServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ETFControllerTest {

    @Value("${service.api.max_work_dt.endpoint}")
    private String dtEndpoint;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private KRXServiceImpl krxService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }

    @Test
    public void validateETFList() throws Exception {
        this.mockMvc.perform(
                get("/api/etfs")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$..['ISU_SRT_CD']").exists())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void validateEachETF() throws Exception {
        this.mockMvc.perform(
                get("/api/etf/407170")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$..['COMPST_ISU_CD']").exists());
    }

}
