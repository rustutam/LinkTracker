//package backend.academy.scrapper.controller;
//
//import static org.mockito.Mockito.verifyNoInteractions;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import backend.academy.scrapper.handler.LinkHandler;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(LinksController.class)
//@AutoConfigureMockMvc
//class LinksControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private LinkHandler linkHandler;
//
//    @Test
//    void getLinks_InvalidStringHeader_ReturnsBadRequest() throws Exception {
//        mockMvc.perform(get("/links").header("Tg-Chat-Id", "invalid_string")) // Передаём строку вместо числа
//                .andExpect(status().isBadRequest());
//
//        verifyNoInteractions(linkHandler);
//    }
//
//    @Test
//    void addLinks_InvalidStringHeader_ReturnsBadRequest() throws Exception {
//        String requestBody =
//                """
//            {
//                "link": "https://example.com",
//                "tags": ["tag1", "tag2"],
//                "filters": ["filter1", "filter2"]
//            }
//            """;
//
//        mockMvc.perform(post("/links")
//                        .header("Tg-Chat-Id", "invalid_string") // Некорректный заголовок
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isBadRequest());
//
//        verifyNoInteractions(linkHandler);
//    }
//
//    @Test
//    void addLinks_InvalidBody_ReturnsBadRequest() throws Exception {
//        String requestBody =
//                """
//            {
//                "link": null,
//                "tags": ["tag1"],
//                "filters": ["filter1"]
//            }
//            """;
//
//        mockMvc.perform(post("/links")
//                        .header("Tg-Chat-Id", "123") // Корректный заголовок
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isBadRequest());
//
//        verifyNoInteractions(linkHandler);
//    }
//
//    @Test
//    void deleteLinks_InvalidStringHeader_ReturnsBadRequest() throws Exception {
//        String requestBody =
//                """
//            {
//                "link": "https://example.com"
//            }
//            """;
//
//        mockMvc.perform(delete("/links")
//                        .header("Tg-Chat-Id", "invalid_string") // Некорректный заголовок
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isBadRequest());
//
//        verifyNoInteractions(linkHandler);
//    }
//
//    @Test
//    void deleteLinks_InvalidBody_ReturnsBadRequest() throws Exception {
//        String requestBody = """
//            {
//                "link": null
//            }
//            """;
//
//        mockMvc.perform(delete("/links")
//                        .header("Tg-Chat-Id", "123") // Корректный заголовок
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isBadRequest());
//
//        verifyNoInteractions(linkHandler);
//    }
//}
