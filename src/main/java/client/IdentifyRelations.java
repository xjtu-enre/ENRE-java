package client;

import visitor.deper.*;

public class IdentifyRelations {

    public void run(){
        DepBackfill depBackfill = new ImportBf();
        depBackfill.setDep();
        System.out.println("Import dependency identified successfully...");

        depBackfill = new InheritBf();
        depBackfill.setDep();
        System.out.println("Inherit dependency identified successfully...");

        depBackfill = new ImplementBf();
        depBackfill.setDep();
        System.out.println("Implement dependency identified successfully...");

        depBackfill = new ContainDefineBf();
        depBackfill.setDep();
        System.out.println("Implement dependency identified successfully...");

        depBackfill = new ParametersBf();
        depBackfill.setDep();
        System.out.println("Parameter dependency identified successfully...");

        System.out.println("Return dependency identified successfully...");

        depBackfill = new CallBf();
        depBackfill.setDep();
        System.out.println("Call dependency identified successfully...");

        depBackfill = new VarInfoBf();
        depBackfill.setDep();
        System.out.println("Var dependency, including use, set and modify, identified successfully...");
    }
}
