import javax.swing.*;

/**
 * Created by zhihan on 2/25/17.
 */
abstract class ObserverPanel extends JPanel {
    abstract protected void initializeAppearance();
    void update() {
        this.removeAll();
        this.initializeAppearance();
    }
}
