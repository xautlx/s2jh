package lab.s2jh.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPAddrFetcher {

    private static Logger logger = LoggerFactory.getLogger(IPAddrFetcher.class);

    /**
     * 获取客户端IP地址，支持代理服务器
     * @param request
     * @return
     */
    public static String getRemoteIpAddress(HttpServletRequest request) {
        String ip = "";
        //匹配大小写，保证无论Nginx如何配置代理参数，系统都能正常获取代理IP
        Enumeration<?> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String paraName = (String) enumeration.nextElement();
            if ("x-forward-for".equalsIgnoreCase(paraName) || "x-forwarded-for".equalsIgnoreCase(paraName)) {
                ip = request.getHeader(paraName);
                break;
            }
        }
        String localIP = "127.0.0.1";
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getGuessUniqueIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    if (address instanceof Inet4Address) {
                        if (!"127.0.0.1".equals(address.getHostAddress())) {
                            return address.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Get IP Error", e);
        }
        return null;
    }

    public String getIPInfo() {
        StringBuilder sb = new StringBuilder();

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                sb.append("Interface " + ni.getName() + ":\r\n");

                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();

                    sb.append("Address");

                    if (address instanceof Inet4Address) {
                        sb.append("(v4)");
                    } else {
                        sb.append("(v6)");
                    }

                    sb.append(":address=" + address.getHostAddress() + " name=" + address.getHostName() + "\r\n");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(IPAddrFetcher.getGuessUniqueIP());
    }
}
