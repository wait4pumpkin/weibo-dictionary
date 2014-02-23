/**
 * Created by pumpkin on 2/17/14.
 */
package com.pityboy.document;

import org.ansj.dic.LearnTool;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Manager {

    public static void main(String[] args) {
        LearnTool learnTool = new LearnTool();

        Jedis jedis = new Jedis("localhost");
        Set<String> keys = jedis.keys("mid:*:content");
        int counter = 0;
        for (String key: keys) {
            String content = jedis.get(key);
            System.out.println(key);

            NlpAnalysis.parse(content, learnTool);

//            List<Term> parse = ToAnalysis.parse(content);
//            System.out.println(parse);
            if (++counter > 10000) break;
        }
        List<Map.Entry<String, Double>> newWords = learnTool.getTopTree(100);
        for (Map.Entry<String, Double> word: newWords) {
            if (!UserDefineLibrary.contains(word.getKey())) {
                System.out.println(word.getKey() + " " + word.getValue());
            }
        }
    }
}
