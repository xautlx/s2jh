package net.sf.jasperreports.swing;

import net.sf.jasperreports.engine.JasperPrint;

public class EditableJRViewer extends JRViewer {

    public EditableJRViewer(JasperPrint jrPrint) {
        super(jrPrint);
    }

    protected JRViewerPanel createViewerPanel() {
        return new EditableJRViewerPanel(viewerContext);
    }
}
