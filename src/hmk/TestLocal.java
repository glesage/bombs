package hmk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author WMarrero
 */
public class TestLocal {
    public static void main(String[] args) throws IOException {
        ArrayList<String> question = new ArrayList<String>();

//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//        String line = in.readLine();
//        while (line != null && line.length() > 0) {
//            question.add(line);
//            line = in.readLine();
//        }

//        question.add("4");
//        question.add("0,0");
//        question.add("3,4");
//        question.add("0,1");
//        question.add("1,2");
//        question.add("2,0");

        question.add("60");
        question.add("0,0");
        question.add("59,59");

        ArrayList<String> answer = Solution.solve(question);
        for(String s : answer) {
            System.out.println(s);
        }
    }
    
}
