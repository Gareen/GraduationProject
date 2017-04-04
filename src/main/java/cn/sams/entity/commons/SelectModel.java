package cn.sams.entity.commons;

/**
 * Created by Fanpeng on 2017/2/25.
 */

/**
 * 此对象专供选择框的键值对设置
 */
public class SelectModel  {

    // 选项的key  displayMember
    private String key;
    // 选项的value valueMember
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SelectModel{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectModel that = (SelectModel) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

}
