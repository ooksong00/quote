package young.com.sayingstory.ui.event;

//When using only string event, we use this class.
public class LoginEvent {
    public final String message;

    public LoginEvent(String message) {
        this.message = message;
    }
}
