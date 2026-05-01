package sk.ditec.cud.procvys.out;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Zaznam implements Serializable {

    private Object[] values;

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}
