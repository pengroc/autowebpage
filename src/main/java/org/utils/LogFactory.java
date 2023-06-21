package org.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


public class LogFactory {

    private LogFactory() {

    }

    private static Logger logger = null;

    private static LogFactory factory = null;

    public static LogFactory getInstance(Class clazz) {
        if (null == factory) {
            factory = new LogFactory();
            if (null == logger) {
                logger = Logger.getLogger(clazz.getName());
            }

        }
        return factory;
    }

    /**
     * 打印日志
     *
     * @param priority 级别
     * @param message  日志
     */
    public void log(Priority priority, Object message) {
        logger.log(priority, message);

    }

    /**
     * 打印日志
     *
     * @param priority 级别
     * @param message  日志
     * @param t        抛出的异常
     */
    public void log(Priority priority, Object message, Throwable t) {
        logger.log(priority, message, t);

    }

    /**
     * 打印日志
     *
     * @param priority  级别
     * @param className 类名
     * @param method    方法名
     * @param key       对应参数key
     * @param value     对应参数值
     * @param msg       消息日志
     * @param t         抛出的异常
     */
    public void log(Priority priority, String className, String method,
                    String[] key, String[] value, String msg, Throwable t) {
        String message = builderMessage(className, method, key, value, msg);
        logger.log(priority, message, t);

    }

    /**
     * 构造消息
     *
     * @param className
     * @param method
     * @param key
     * @param value
     * @param msg
     * @return 消息体
     */
    private String builderMessage(String className, String method,
                                  String[] key, String[] value, String msg) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(LogConstants.LOG_LEFT_SUFFER_BRACKET)
                .append(className)
                .append(LogConstants.LOG_RIGHT_SUFFER_BRACKET)
                .append(LogConstants.LOG_DOT)
                .append(LogConstants.LOG_LEFT_SUFFER_BRACKET)
                .append(method)
                .append(LogConstants.LOG_RIGHT_SUFFER_BRACKET)
                .append(LogConstants.LOG_LEFT_SUFFER_BRACKET)
                .append(msg)
                .append(LogConstants.LOG_RIGHT_SUFFER_BRACKET);
        if (key.length == value.length) {
            buffer.append(LogConstants.LOG_LEFT_SUFFER_BRACKET
                    + "The parameter details as below:");
            // buffer.append(LogConstants.LOG_LINE_SEPARATOR);
            buffer.append(LogConstants.LOG_LEFT_BIG_BRACKET);
            for (int i = 0; i < key.length; i++) {

                buffer.append(LogConstants.LOG_LINE_SEPARATOR)
                        .append(key[i])
                        .append(LogConstants.LOG_AMOUNT)
                        .append(value[i]);

            }
            buffer.append(LogConstants.LOG_LINE_SEPARATOR);
            buffer.append(LogConstants.LOG_RIGHT_BIG_BRACKET);
            buffer.append(LogConstants.LOG_RIGHT_SUFFER_BRACKET);
        }
        return buffer.toString();
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

}
