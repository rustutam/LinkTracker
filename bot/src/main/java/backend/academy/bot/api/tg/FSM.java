package backend.academy.bot.api.tg;

public class FSM {
    private States currentState;

    public States getCurrentState() {
        return currentState;
    }

    public void setCurrentState(States newState) {
        currentState = newState;
    }
}
