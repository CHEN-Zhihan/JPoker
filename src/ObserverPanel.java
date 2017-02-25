import javax.swing.*;

/**
 * Created by zhihan on 2/25/17.
 */
public abstract class ObserverPanel extends JPanel {
    abstract protected void initializeAppearance();
    public void update() {
        this.removeAll();
        this.initializeAppearance();
    }
}