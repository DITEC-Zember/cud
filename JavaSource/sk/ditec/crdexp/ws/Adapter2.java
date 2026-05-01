
package sk.ditec.crdexp.ws;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (sk.ditec.common.utils.DateUtils.parseDateTime(value));
    }

    public String marshal(Date value) {
        return (sk.ditec.common.utils.DateUtils.printDateTime(value));
    }

}
