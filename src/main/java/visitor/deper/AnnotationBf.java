package visitor.deper;

import entity.BaseEntity;
import util.Configure;

public class AnnotationBf extends DepBackfill{

    @Override
    public void setDep() {
        for (BaseEntity entity : singleCollect.getEntities()){
            if(!entity.getAnnotations().isEmpty()){
                for (String ant : entity.getAnnotations()){
                    if(singleCollect.getCreatedAnt().containsKey(ant)){
                        int annotationId = singleCollect.getCreatedAnt().get(ant);
                        saveRelation(annotationId, entity.getId(), Configure.RELATION_ANNOTATE, Configure.RELATION_ANNOTATED_BY);
                    }
                }
            }
        }
    }

}
