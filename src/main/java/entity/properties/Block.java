package entity.properties;

import java.util.ArrayList;

/**
 * Block is the block inside method.
 * package block, file block, class block are coresponding to their childrenIds,
 * so we can use parentId to specify these blocks.
 *
 * But, variables inside a function, have local scope.
 * Such kind of local block is only valid inside method body.
 * for example, for-stmt, if-stmt, switch-stmt, other nested block inside a local block.
 *
 * Just like localName, LocalBlock is valid only inside a method.
 * So, We bind a Block list to its methodEntity, please see MethodEntity class declaration.
 */
public class Block {

    int id;
    //the kind of block : if-stmt, for-stmt, switch-stmt, etc.
    String blockName;
    int depth;
    int parentBlockId;


    public Block(int id, String blockName, int depth, int parentBlockId){
        this.id = id;
        this.blockName = blockName;
        this.depth = depth;
        this.parentBlockId = parentBlockId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getParentBlockId() {
        return parentBlockId;
    }

    public void setParentBlockId(int parentBlockId) {
        this.parentBlockId = parentBlockId;
    }
}
