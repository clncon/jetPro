package com.jet.stream.domain;

/**
 * 2021/10/1
 *
 * @author kongmin
 * @version 1.0.0
 * <p>
 * 任务类
 */
public class Task {

    /**任务ID**/
    private String id;

    /**任务名称**/
    private String taskName;

    /**提交时间**/
    private Long submitTime;

    /**任务状态**/
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
