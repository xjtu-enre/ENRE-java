package entity;

import java.util.ArrayList;

public class TypeEntity extends ScopeEntity{

    protected ArrayList<Integer> importedBy = new ArrayList<Integer>();

    public ArrayList<Integer> getImportedBy(){
        return this.importedBy;
    }

    public void addImportedBy(int id){
        this.importedBy.add(id);
    }
}
