package util;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class CompilationUnitPair {

    public CompilationUnitPair(String source, CompilationUnit ast) {
        this.source = source;
        this.ast = ast;
    }

    public final String source;
    public final CompilationUnit ast;
}
