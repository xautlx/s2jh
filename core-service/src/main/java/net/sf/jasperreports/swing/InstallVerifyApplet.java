package net.sf.jasperreports.swing;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

public class InstallVerifyApplet extends JApplet {

    public void start() {
        JOptionPane.showMessageDialog(this, "Applet runtime verify: OK");
    }
}