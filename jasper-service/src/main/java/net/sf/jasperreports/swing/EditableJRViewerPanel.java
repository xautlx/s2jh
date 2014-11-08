package net.sf.jasperreports.swing;

import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.view.JRHyperlinkListener;

import org.apache.commons.lang3.StringUtils;

public class EditableJRViewerPanel extends JRViewerPanel {

    protected void createHyperlinks(List<JRPrintElement> elements, int offsetX, int offsetY) {
        for (Iterator<JRPrintElement> it = elements.iterator(); it.hasNext();) {
            JRPrintElement element = it.next();
            if (element instanceof JRPrintHyperlink) {
                JRPrintHyperlink hyperlink = (JRPrintHyperlink) element;
                if (StringUtils.isBlank(hyperlink.getHyperlinkTooltip())
                        && hyperlink.getHyperlinkTypeValue() == HyperlinkTypeEnum.CUSTOM) {
                    hyperlink.setHyperlinkTooltip("点击可修改数据");
                }
            }
        }
        super.createHyperlinks(elements, offsetX, offsetY);
    }

    public EditableJRViewerPanel(final JRViewerController viewerContext) {
        super(viewerContext);
        this.addHyperlinkListener(new JRHyperlinkListener() {
            @Override
            public void gotoHyperlink(JRPrintHyperlink hyperlink) throws JRException {
                JRPrintElement element = (JRPrintElement) hyperlink;
                if (element instanceof JRPrintText) {
                    JRPrintText text = (JRPrintText) element;
                    String fullText = text.getFullText();
                    String newText = JOptionPane.showInputDialog("请输入新数据：", fullText);
                    if (newText != null) {
                        text.setText(newText);
                        viewerContext.refreshPage();
                    }
                }
            }
        });
    }
}
