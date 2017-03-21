package cn.sams.entity.component;

/**
 * Created by Fanpeng on 2017/3/21.
 */
public class JqxColumn {

    private String text;
    private String datafield;
    private String width;
    private String cellsalign = "center";
    private String align = "center";
    private String columngroup;
    private String filtercondition = "contains";
    private String cellsrenderer;
    private boolean pinned;

    public JqxColumn() {
    }

    public JqxColumn(String datafield, String text, String columngroup, String width, boolean pinned) {
        this.datafield = datafield;
        this.text = text;
        this.width = width;
        this.columngroup = columngroup;
        this.pinned = pinned;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDatafield() {
        return datafield;
    }

    public void setDatafield(String datafield) {
        this.datafield = datafield;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getCellsalign() {
        return cellsalign;
    }

    public void setCellsalign(String cellsalign) {
        this.cellsalign = cellsalign;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getColumngroup() {
        return columngroup;
    }

    public void setColumngroup(String columngroup) {
        this.columngroup = columngroup;
    }

    public String getCellsrenderer() {
        return cellsrenderer;
    }

    public void setCellsrenderer(String cellsrenderer) {
        this.cellsrenderer = cellsrenderer;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public String getFiltercondition() {
        return filtercondition;
    }

    public void setFiltercondition(String filtercondition) {
        this.filtercondition = filtercondition;
    }
}
