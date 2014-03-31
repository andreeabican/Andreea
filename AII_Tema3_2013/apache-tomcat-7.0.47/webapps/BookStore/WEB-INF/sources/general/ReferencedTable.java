package general;

public class ReferencedTable {
    private String parentTable;
    private String attributeName;
    
    public ReferencedTable() {
        parentTable   = new String();
        attributeName = new String();
    }
    
    public ReferencedTable(String parentTable, String attributeName) {
        this.parentTable   = parentTable;
        this.attributeName = attributeName;
    }    
    
    public String getParentTable() {
        return parentTable;
    }
    
    public String getAttributeName() {
        return attributeName;
    }
    
    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }
    
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}