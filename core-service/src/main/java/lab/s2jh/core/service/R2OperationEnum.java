package lab.s2jh.core.service;

public enum R2OperationEnum {
    
    add("添加关联"),

    delete("删除关联"),

    update("更新关联");

    private String label;

    private R2OperationEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
