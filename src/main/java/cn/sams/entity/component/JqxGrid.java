package cn.sams.entity.component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanpeng on 2017/3/21.
 */
public class JqxGrid {

    private List<JqxDatafield> datafields = new ArrayList<JqxDatafield>();
    private List<JqxColumn> columns = new ArrayList<JqxColumn>();
    private List<JqxColumnGroup> groups = new ArrayList<JqxColumnGroup>();


    public List<JqxDatafield> getDatafields() {
        return datafields;
    }

    public void setDatafields(List<JqxDatafield> datafields) {
        this.datafields = datafields;
    }

    public List<JqxColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<JqxColumn> columns) {
        this.columns = columns;
    }


    public List<JqxColumnGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<JqxColumnGroup> groups) {
        this.groups = groups;
    }
}
