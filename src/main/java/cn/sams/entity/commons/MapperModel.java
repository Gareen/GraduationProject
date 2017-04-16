package cn.sams.entity.commons;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/4/16.
 */
public class MapperModel implements Serializable {

    private String key;
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MapperModel{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
