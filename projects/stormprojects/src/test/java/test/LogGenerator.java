package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class LogGenerator {

    public static void main(String[] args) throws Exception{

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("/Users/mac/IdeaProjects/bigdata/projects/stormprojects/logs/service.log")));

        for(int i = 0;i < 30000000;i ++) {
            String str = "AAAAACCCCCCCCSSSSSSSSSSDFSAAAAExceptionSDFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA@#@#SDFAAAAAAAAAAAAAAAAAAAAAAAAAAASDFASDF\r\n";
            bufferedWriter.write(str);
            if(i % 100000 == 0) {
                bufferedWriter.flush();
            }
        }
        bufferedWriter.close();
    }

}
