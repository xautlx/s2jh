package lab.apollo.tool.builder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * 批量的基于原始密码生成对应的MD5编码，直接输出到控制台，在造基础数据时可以用到
 */
public class MD5Builder {
    public static void main(String[] args) throws Exception {
        Md5PasswordEncoder md5PasswordEncoder=new Md5PasswordEncoder();
 
        BufferedReader reader=new BufferedReader(new InputStreamReader(MD5Builder.class.getResourceAsStream("password.txt")));
        String line;  
        while ((line = reader.readLine()) != null) { 
            if(StringUtils.isNotBlank(line)){
                System.out.println(md5PasswordEncoder.encodePassword(line, null));
            }            
        }  
    }
}
