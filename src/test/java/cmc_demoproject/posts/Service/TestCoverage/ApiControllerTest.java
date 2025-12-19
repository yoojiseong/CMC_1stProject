package cmc_demoproject.posts.Service.TestCoverage;

import cmc_demoproject.posts.user.dto.RegistMemberDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 API 호출 테스트 (PermitAll)")
    void signupApiTest() throws Exception {
        RegistMemberDTO dto = RegistMemberDTO.builder()
                .email("api@test.com")
                .password("1234")
                .userName("API테스터")
                .build();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증 없이 카테고리 조회 API 호출 (GET)")
    void getCategoriesGuestTest() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("인증된 사용자의 카테고리 생성 API 호출 (POST)")
    void createCategoryWithUserTest() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .content("새로운카테고리")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }
}