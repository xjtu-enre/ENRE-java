package visitor;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class RelationVisitor extends EntityVisitor{

    public RelationVisitor(String fileFullPath, CompilationUnit compilationUnit) {
        super(fileFullPath, compilationUnit);
    }

}
