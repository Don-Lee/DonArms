package cn.rjgc.donarms.bean;

/**
 * Date 2021/12/1
 *
 * @author Don
 */
public class OilStationBean {
    private String name;
    private String address;

    public OilStationBean(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
