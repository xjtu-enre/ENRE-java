package visitor;

import org.eclipse.jdt.core.dom.CompilationUnit;
import util.Tuple;

public class RelationVisitor extends EntityVisitor{

    public RelationVisitor(String fileFullPath, CompilationUnit compilationUnit, Tuple<String, Integer> currentBin) {
        super(fileFullPath, compilationUnit, currentBin);
    }

}
