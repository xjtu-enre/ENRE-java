package visitor.deper;

import entity.BaseEntity;
import entity.properties.Location;
import util.Configure;
import util.Tuple;

public class AnnotationBf extends DepBackfill{

    @Override
    public void setDep() {
        for (BaseEntity entity : singleCollect.getEntities()){
            if(!entity.getAnnotations().isEmpty()){
                for (Tuple<String, Location> ant : entity.getAnnotations()){
                    if(singleCollect.getCreatedAnt().containsKey(ant.getL())){
                        int annotationId = singleCollect.getCreatedAnt().get(ant.getL());
                        saveRelation(annotationId, entity.getId(), Configure.RELATION_ANNOTATE, Configure.RELATION_ANNOTATED_BY, ant.getR());
                    }
                }
            }
        }
    }

}
