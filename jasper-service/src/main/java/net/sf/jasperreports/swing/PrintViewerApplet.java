package net.sf.jasperreports.swing;

import java.awt.Color;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRLoader;

public class PrintViewerApplet extends JApplet {
    private URL url = null;

    public void init() {
        String strUrl = getParameter("REPORT_URL");
        this.setBackground(new Color(0, 50, 50));
        if (strUrl != null) {
            try {
                url = new URL(getCodeBase(), strUrl); //从获得html参数中获得报表URL
                //System.out.println(url);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "处理异常：" + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Source URL not specified");
        }
    }

    public void start() {
        if (url != null) {
            try {
                // JOptionPane.showMessageDialog(this, url);
                JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(url); //发送对象请求，获得JasperPrint对象
                //System.out.println("Height:" + jasperPrint.getPageHeight() + "Width:" + jasperPrint.getPageWidth()
                //       + ",jasperPrint=" + jasperPrint);
                //JasperPrintManager.printReport((JasperPrint) obj, true); //调用方法打印所获得的JasperPrint对象
                //System.out.println("appletprinter 完成！");
                //WbbJasperViewer view = new WbbJasperViewer((JasperPrint) obj, false);
                JRViewer view = new EditableJRViewer(jasperPrint);
                //view.pack();
                this.getContentPane().add(view);
                view.setVisible(true);
                //view.setExtendedState(JFrame.MAXIMIZED_BOTH);
                view.requestFocus();
                this.setVisible(true);
                view.setFocusable(false);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "处理异常：" + e.getMessage());
            }
        }
    }
}