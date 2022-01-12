package entity;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeEntity extends ScopeEntity{

    protected ArrayList<Integer> importedBy = new ArrayList<Integer>();

    protected ArrayList<Integer> innerType = new ArrayList<>();


    public ArrayList<Integer> getImportedBy(){
        return this.importedBy;
    }

    public void addImportedBy(int id){
        this.importedBy.add(id);
    }

    public ArrayList<Integer> getInnerType(){
        return this.innerType;
    }

    public void addInnerType(int id){
        this.innerType.add(id);
    }


}
