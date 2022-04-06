package com.autotest.qa.utils;


import com.autotest.qa.common.TestData;

import java.util.*;

public class Test {
    public static void main(String[] args) {
//        Queue<Integer> q=new LinkedList<Integer>();
//        q.add(1);
//        q.add(2);
//        System.out.println(q.peek());
//        System.out.println(q.poll());
//        System.out.println(q.poll());
//        System.out.println(q.poll());
//        Stack<Integer> s=new Stack<Integer>();
//        s.push(1);
//        s.push(2);
//        System.out.println(s.pop());
//        System.out.println(s.pop());
//        CQueue cQueue=new CQueue();
//        cQueue.appendTail(1);
//        cQueue.appendTail(2);
//        System.out.println(cQueue.deleteHead());
//        System.out.println(cQueue.deleteHead());
//        ttt t=new ttt();
//        System.out.println(t.divide(-1,1));
        TestData t=new TestData();
        t.testData("src\\main\\resources\\data\\Boss.xlsx","commonPhoneSendCode");

    }
}
