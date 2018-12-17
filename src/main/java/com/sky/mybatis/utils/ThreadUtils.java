package com.sky.mybatis.utils;

/**
 * @author: 甜筒
 * @Date: 17:39 2018/12/17
 * Modified By:
 */
public class ThreadUtils {

    /** 方法结束、正常返回、抛出异常时回复当前线程的原名称,标识当前栈帧执行完毕*/
    public static void resetThreadNmae (Thread thread) {
        String name = thread.getName();
        thread.setName(name.split(":")[0]);
    }
}
