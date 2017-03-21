package cn.sams.entity.component;

/**
 * Created by Fanpeng on 2017/3/21.
 */
public class JqxColumnGroup {

    private String text;
    private String align = "center";
    private String name;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
