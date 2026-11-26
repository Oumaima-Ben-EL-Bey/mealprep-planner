package de.gruppe1.mealprep.exception;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    @RestController
    static class TestController {
        @GetMapping("/test/crash")
        void crash() {
            throw new IllegalStateException("boom");
        }

        @GetMapping("/test/day/{day}")
        void day(@PathVariable DayOfWeek day) {
        }
    }

    private MockMvc mvc() {
        return MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void unexpectedErrorBecomes500WithGermanMessage() throws Exception {
        mvc().perform(get("/test/crash"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(startsWith("Ein unerwarteter Fehler")));
    }

    @Test
    void wrongEnumValueBecomes400() throws Exception {
        mvc().perform(get("/test/day/monday"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void wrongHttpMethodKeeps405() throws Exception {
        mvc().perform(post("/test/crash"))
                .andExpect(status().isMethodNotAllowed());
    }
}