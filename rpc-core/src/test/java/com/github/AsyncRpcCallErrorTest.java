
package com.github;

import com.github.async.AsyncCallObject;
import com.github.async.AsyncCallback;
import com.github.async.AsyncInvoker;
import com.github.exception.AsyncCallException;
import com.github.services.CostTimeCalculate;
import com.github.services.pojo.CostTime;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AsyncRpcCallErrorTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        final CostTimeCalculate calculate = (CostTimeCalculate) context.getBean("costTime");

        AsyncInvoker invoker = new AsyncInvoker();

        try {
            CostTime elapse0 = invoker.submit(new AsyncCallback<CostTime>() {
                @Override
                public CostTime call() {
                    throw new RuntimeException("calculate fail 1!");
                }
            });

            System.out.println("1 async rpc call:[" + "result:" + elapse0 + ", status:[" + ((AsyncCallObject) elapse0)._getStatus() + "]");
        } catch (AsyncCallException e) {
            System.out.println(e.getMessage());
            context.destroy();
            return;
        }

        context.destroy();
    }
}
