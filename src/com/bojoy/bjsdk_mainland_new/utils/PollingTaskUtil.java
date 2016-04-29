package com.bojoy.bjsdk_mainland_new.utils;

import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.support.task.PollingTimeoutTask;

/**
 * Created by wutao on 2016/1/15.
 * 轮询任务工具类
 */
public class PollingTaskUtil implements PollingTimeoutTask.PollingListener {
    private final String TAG = PollingTaskUtil.class.getSimpleName();

    private static PollingTaskUtil instance;
    private final static Object block = new Object();
    private PollingTimeoutTask pollingTask;
    private EventBus eventBus = EventBus.getDefault();
    private String taskTag;

    private PollingTaskUtil() {

    }

    public static PollingTaskUtil getDefault() {
        if (instance == null) {
            synchronized (block) {
                if (instance == null) {
                    instance = new PollingTaskUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 启动全局的轮询事件
     * @param period
     * @param delay
     * @param maxTime
     * @param tag
     * @return 返回是否启动成功，如果不成功，说明有轮询超时再用，要合理判断后先中断，后启动
     */
    public boolean startPollingTask(int period, int delay, int maxTime, String tag) {
        if (pollingTask != null && pollingTask.isStart()) {
            return false;
        }
        taskTag = tag;
        pollingTask = null;
        pollingTask = new PollingTimeoutTask(period, delay, maxTime, this);
        pollingTask.startPolling();
        return true;
    }

    /**
     * 中断轮询事件
     */
    public void suspendGlobalPolling() {
        if (pollingTask != null && pollingTask.isStart()) {
            pollingTask.suspendPolling();
        }
        pollingTask = null;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public boolean isStart(String tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag not null");
        }
        if (pollingTask == null) {
            return false;
        }
        if (tag.equals(taskTag)) {
            return pollingTask.isStart();
        }
        return false;
    }

    @Override
    public void onExecute() {
        eventBus.post(new PollingExecuteEvent(taskTag));
    }

    @Override
    public void onTimeout() {
        eventBus.post(new PollingTimeoutEvent(taskTag));
    }

    public class PollingExecuteEvent {

        private String taskTag;

        public PollingExecuteEvent(String tag) {
            taskTag = tag;
        }

        public String getTaskTag() {
            return taskTag;
        }

    }

    public class PollingTimeoutEvent {

        private String taskTag;

        public PollingTimeoutEvent(String tag) {
            taskTag = tag;
        }

        public String getTaskTag() {
            return taskTag;
        }

    }
}
