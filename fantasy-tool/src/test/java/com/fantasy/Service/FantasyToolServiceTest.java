package com.fantasy.Service;

import com.fantasy.Model.League;
import com.fantasy.Repository.IRepository;
import com.fantasy.Request.RequestModels.UsernameResponse;
import com.fantasy.Request.SleeperRequestHandler;
import com.fantasy.Model.SystemMetadata;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FantasyToolServiceTest {

    private IRepository repo;

    private FantasyToolService serviceFromInput(String input) {
        Scanner scan = new Scanner(new ByteArrayInputStream(input.getBytes()));
        return new FantasyToolService(repo, scan);
    }

    @BeforeEach
    void setup() {
        repo = mock(IRepository.class);
    }

    // ---------------------------
    // ifQuit()
    // ---------------------------
    @Test
    void testIfQuit_quits() {
        FantasyToolService service = serviceFromInput("q\n");

        boolean result = service.ifQuit();

        assertTrue(result);
    }

    @Test
    void testIfQuit_chooseDifferentLeague() {
        FantasyToolService service = serviceFromInput("c\n");

        boolean result = service.ifQuit();

        assertFalse(result);
    }

    @Test
    void testIfQuit_invalidThenQuit() {
        FantasyToolService service = serviceFromInput("x\nq\n");

        boolean result = service.ifQuit();

        assertTrue(result);
    }

    @Test
    void testIfQuit_quitsUppercase() {
        FantasyToolService service = serviceFromInput("Q\n");

        boolean result = service.ifQuit();

        assertTrue(result);
    }

    @Test
    void testIfQuit_chooseDifferentLeagueUppercase() {
        FantasyToolService service = serviceFromInput("C\n");

        boolean result = service.ifQuit();

        assertFalse(result);
    }

    // ---------------------------
    // getUserId()
    // ---------------------------
    @Test
    void testGetUserId_validUsername() throws Exception {
        FantasyToolService service = serviceFromInput("myUser\n");

        HttpResponse<String> mockResp = mock(HttpResponse.class);
        when(mockResp.statusCode()).thenReturn(200);
        when(mockResp.body()).thenReturn("{\"user_id\":12345}");

        try (MockedStatic<SleeperRequestHandler> staticMock = mockStatic(SleeperRequestHandler.class)) {
            staticMock.when(() -> SleeperRequestHandler.getUserFromUsername("myUser"))
                    .thenReturn(mockResp);

            long userId = service.getUserId();
            assertEquals(12345, userId);
        }
    }

    @Test
    void testGetUserId_invalidThenValid() throws Exception {
        FantasyToolService service = serviceFromInput("badUser\nmyUser\n");

        // First call throws
        HttpResponse<String> okResp = mock(HttpResponse.class);
        when(okResp.statusCode()).thenReturn(200);
        when(okResp.body()).thenReturn("{\"user_id\":888}");

        try (MockedStatic<SleeperRequestHandler> staticMock = mockStatic(SleeperRequestHandler.class)) {
            staticMock.when(() -> SleeperRequestHandler.getUserFromUsername("badUser"))
                    .thenThrow(new RuntimeException("bad"));

            staticMock.when(() -> SleeperRequestHandler.getUserFromUsername("myUser"))
                    .thenReturn(okResp);

            long result = service.getUserId();
            assertEquals(888, result);
        }
    }

    // ---------------------------
    // chooseLeague()
    // ---------------------------
    @Test
    void testChooseLeague_validChoice() {
        FantasyToolService service = serviceFromInput("2\n");

        List<League> leagues = List.of(
                new League(1, 12, "League1", 2025),
                new League(2, 10, "League2", 2025)
        );

        Long chosen = service.chooseLeague(leagues);

        assertEquals(2L, chosen);
    }

    @Test
    void testChooseLeague_invalidThenValid() {
        FantasyToolService service = serviceFromInput("x\n1\n");

        List<League> leagues = List.of(
                new League(10, 12, "League1", 2025)
        );

        Long chosen = service.chooseLeague(leagues);

        assertEquals(10L, chosen);
    }

    @Test
    void testChooseLeague_invalidEdgeCase1() {
        FantasyToolService service = serviceFromInput("0\n1\n");

        List<League> leagues = List.of(
                new League(10, 12, "League1", 2025)
        );

        Long chosen = service.chooseLeague(leagues);

        assertEquals(10L, chosen);
    }

    @Test
    void testChooseLeague_invalidEdgeCase2() {
        FantasyToolService service = serviceFromInput("2\n1\n");

        List<League> leagues = List.of(
                new League(10, 12, "League1", 2025)
        );

        Long chosen = service.chooseLeague(leagues);

        assertEquals(10L, chosen);
    }

    // ---------------------------
    // close()
    // ---------------------------
    @Test
    void testClose_callsRepoClose() throws IOException {
        FantasyToolService service = serviceFromInput("");

        service.close();

        verify(repo, times(1)).close();
    }
}
