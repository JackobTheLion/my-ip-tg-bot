package ru.telegrambot.myiptgbot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.telegrambot.myiptgbot.config.BotInitializer;
import ru.telegrambot.myiptgbot.service.IpClient;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"cron.ip-check=-", "cron.retry-unsent=-"})
class OpenApiTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private BotInitializer botInitializer;

    @MockitoBean
    private IpClient ipClient;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void describesBothControllerInterfaces() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.version").value("1.0"))
                .andExpect(jsonPath("$.paths['/ip'].get.responses['200'].content['text/plain'].schema.type")
                        .value("string"))
                .andExpect(jsonPath("$.paths['/ip'].get.responses['503']").exists())
                .andExpect(jsonPath("$.paths['/notify'].get.parameters[0].name").value("text"))
                .andExpect(jsonPath("$.paths['/notify'].get.parameters[0].required").value(true))
                .andExpect(jsonPath("$.paths['/notify'].get.responses['400']").exists());
    }

    @Test
    void servesSwaggerUi() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Swagger UI")));

        mockMvc.perform(get("/v3/api-docs/swagger-config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("/v3/api-docs"));
    }

    @Test
    void notificationRejectsBlankText() throws Exception {
        mockMvc.perform(get("/notify").param("text", " "))
                .andExpect(status().isBadRequest());
    }
}
