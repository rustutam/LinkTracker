package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.TgCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TgCommandFactoryTest {

    private StartCommand startCommand;
    private HelpCommand helpCommand;
    private ListCommand listCommand;
    private TrackCommand trackCommand;
    private UntrackCommand untrackCommand;
    private UnknownCommand unknownCommand;
    private TgCommandFactory factory;

    @BeforeEach
    void setUp() {
        startCommand = mock(StartCommand.class);
        helpCommand = mock(HelpCommand.class);
        listCommand = mock(ListCommand.class);
        trackCommand = mock(TrackCommand.class);
        untrackCommand = mock(UntrackCommand.class);
        unknownCommand = mock(UnknownCommand.class);

        factory = new TgCommandFactory(
            startCommand, helpCommand, listCommand,
            trackCommand, untrackCommand, unknownCommand
        );
    }

    @Test
    void dffdfdf() {
        factory.getTgCommand("/start");


    }

    @Test
    void shouldReturnCorrectCommandForEachEnumValue() {
        assertEquals(startCommand, factory.getTgCommand("START"));
        assertEquals(helpCommand, factory.getTgCommand("HELP"));
        assertEquals(listCommand, factory.getTgCommand("LIST"));
        assertEquals(trackCommand, factory.getTgCommand("TRACK"));
        assertEquals(untrackCommand, factory.getTgCommand("UNTRACK"));
    }

    @Test
    void shouldThrowExceptionForInvalidCommand() {
        assertThrows(IllegalArgumentException.class, () -> factory.getTgCommand("UNKNOWN_COMMAND"));
    }
}
