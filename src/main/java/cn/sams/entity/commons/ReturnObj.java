package cn.sams.entity.commons;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/1/30.
 * 此为公共返回对象
 *
 * status : 状态码
 * msg : 消息内容
 * data : 对象实体
 */
public class ReturnObj implements Serializable {

    // error success
    private String status;
    private String msg;
    private Object data;

    public ReturnObj() {}

    public ReturnObj(String status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
