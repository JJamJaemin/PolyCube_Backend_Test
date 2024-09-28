package kr.co.polycube.backendtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.polycube.backendtest.DTO.UserDTO;
import kr.co.polycube.backendtest.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // 유저 생성 테스트
    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testuser");

        String requestBody = objectMapper.writeValueAsString(userDTO);
        System.out.println("Create User - Request Body: " + requestBody);

        MvcResult result = mockMvc.perform(post("/users")
                        .header("User-Agent", "MockMvc Test Agent") //test일 경우 header 유저정보 넣어주기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk()) //응답상태 확인
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("testuser"))
                .andReturn();

        System.out.println("Create User - Response: " + result.getResponse().getContentAsString());
    }

    // 유저 불러오기 테스트
    @Test
    void testGetUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testuser");
        String createResponse = mockMvc.perform(post("/users")
                        .header("User-Agent", "MockMvc Test Agent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andReturn().getResponse().getContentAsString();
        UserDTO createdUser = objectMapper.readValue(createResponse, UserDTO.class);

        System.out.println("Get User - Request: GET /users/" + createdUser.getId());

        MvcResult result = mockMvc.perform(get("/users/" + createdUser.getId())
                        .header("User-Agent", "MockMvc Test Agent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.name").value("testuser"))
                .andReturn();

        System.out.println("Get User - Response: " + result.getResponse().getContentAsString());
    }

    // 유저 수정 테스트
    @Test
    void testUpdateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testuser");
        String createResponse = mockMvc.perform(post("/users")
                        .header("User-Agent", "MockMvc Test Agent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andReturn().getResponse().getContentAsString();
        UserDTO createdUser = objectMapper.readValue(createResponse, UserDTO.class);

        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("updateduser");
        String requestBody = objectMapper.writeValueAsString(updateDTO);
        System.out.println("Update User - Request Body: " + requestBody);

        MvcResult result = mockMvc.perform(put("/users/" + createdUser.getId())
                        .header("User-Agent", "MockMvc Test Agent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.name").value("updateduser"))
                .andReturn();

        System.out.println("Update User - Response: " + result.getResponse().getContentAsString());
    }

    //특수문자 필터링 테스트
    @Test
    void testSpecialCharacterFilter() throws Exception {
        System.out.println("Special Character Filter - Request: GET /users/1?name=test!!");

        MvcResult result = mockMvc.perform(get("/users/1?name=test!!"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"reason\": \"주소창에 특수 문자가 들어가있습니다.\"}"))
                .andReturn();

        System.out.println("Special Character Filter - Response: " + result.getResponse().getContentAsString());
    }

    // 유저 존재 x 테스트
    @Test
    void testNonExistentUser() throws Exception {
        System.out.println("Non-existent User - Request: GET /users/999");

        MvcResult result = mockMvc.perform(get("/users/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("유저를 찾을 수 없습니다."))
                .andReturn();

        System.out.println("Non-existent User - Response: " + result.getResponse().getContentAsString());
    }

    // 존재하지 않는 api 테스트
    @Test
    void testNonExistentApi() throws Exception {
        System.out.println("Non-existent API - Request: GET /non-existent-api");

        MvcResult result = mockMvc.perform(get("/non-existent-api"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value("존재하지 않는 API입니다."))
                .andReturn();

        System.out.println("Non-existent API - Response: " + result.getResponse().getContentAsString());
    }
}