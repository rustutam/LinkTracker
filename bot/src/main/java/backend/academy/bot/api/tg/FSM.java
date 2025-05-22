package backend.academy.bot.api.tg;

public class FSM {
    private State currentState;

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State newState) {
        currentState = newState;
    }
}
