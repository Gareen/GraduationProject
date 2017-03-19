package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/2/6.
 * 学期
 */
public class Term implements Serializable {

    private String term_id;
    private String term_name;

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (term_id != null ? !term_id.equals(term.term_id) : term.term_id != null) return false;
        return term_name != null ? term_name.equals(term.term_name) : term.term_name == null;
    }

    @Override
    public int hashCode() {
        int result = term_id != null ? term_id.hashCode() : 0;
        result = 31 * result + (term_name != null ? term_name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Term{" +
                "term_id='" + term_id + '\'' +
                ", term_name='" + term_name + '\'' +
                '}';
    }
}
