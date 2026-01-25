package net.yao.stress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RuntimeTest {
    public static void main(String[] args) {
        try {
            // 调⽤Jmeter执⾏脚本 必须要写进空文件夹

            Process process =
                    Runtime.getRuntime().exec("D://apache-jmeter-5.4.1//bin/jmeter.bat -n -t /Users/Hachi/OneDrive/Desktop/jmx/HTTP_requestJson.jmx -l results.log -e -o /Users/Hachi/OneDrive/Desktop/fuck");
            // 读取Jmeter的输出
            BufferedReader reader = new BufferedReader(new
                                    InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 等待Jmeter进程结束
            int exitCode = process.waitFor();
            System.out.println("Jmeter exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
