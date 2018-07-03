package rk.com.demo_smartagent;

public class SmartAgentPojo {

    private  String id;
    private String name;
    private String type;
    private String size_byte;
    private String cdn_path;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize_byte() {
        return size_byte;
    }

    public void setSize_byte(String size_byte) {
        this.size_byte = size_byte;
    }

    public String getCdn_path() {
        return cdn_path;
    }

    public void setCdn_path(String cdn_path) {
        this.cdn_path = cdn_path;
    }

    public SmartAgentPojo(String id, String name, String type,String cdn_path,String size_byte)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cdn_path = cdn_path;
        this.size_byte = size_byte;

    }


}
