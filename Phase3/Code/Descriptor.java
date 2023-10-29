public class Descriptor {
    public String name;
    public Type type;
    public boolean isLocal;
    private String value;

    public Descriptor(String name, Type type, boolean isLocal) {
        this.name = name;
        this.type = type;
        this.isLocal = isLocal;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}

class LocalVarDscp extends Descriptor {

    public LocalVarDscp(String name, Type type) {
        super(name, type, true);
    }
}