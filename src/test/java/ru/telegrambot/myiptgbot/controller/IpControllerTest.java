package ru.telegrambot.myiptgbot.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.telegrambot.myiptgbot.service.IpClient;

@ExtendWith(MockitoExtension.class)
class IpControllerTest {

    @Mock
    private IpClient ipClient;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new IpController(ipClient)).build();
    }

    @Test
    void getIp_returnsIp() throws Exception {
        when(ipClient.getIp()).thenReturn("1.2.3.4");
        mockMvc.perform(get("/ip"))
                .andExpect(status().isOk())
                .andExpect(content().string("1.2.3.4"));
    }
}
