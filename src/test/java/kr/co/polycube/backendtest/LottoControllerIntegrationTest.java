package kr.co.polycube.backendtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LottoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGenerateLottoNumbers() throws Exception {
        MvcResult result = mockMvc.perform(post("/lottos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numbers").isArray())
                .andExpect(jsonPath("$.numbers.length()").value(6))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println("로또 번호 생성 응답: " + content);

        // 로또 번호가 1에서 45 사이의 숫자인지 확인
        String[] numbers = content.replaceAll("[^0-9,]", "").split(",");
        for (String number : numbers) {
            int num = Integer.parseInt(number);
            assertTrue(num >= 1 && num <= 45, "로또 번호는 1에서 45 사이여야 합니다.");
        }
    }
}