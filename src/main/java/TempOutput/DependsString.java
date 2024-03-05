package TempOutput;

import entity.BaseEntity;
import entity.FileEntity;
import entity.PackageEntity;
import entity.properties.Relation;
import util.Configure;
import util.SingleCollect;
import util.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependsString {

    public static class LocationDTO {
        int line;
        int row;

        public LocationDTO(int line, int row){
            this.line = line;
            this.row = row;
        }

        public int getLine() {
            return line;
        }

        public int getRow() {
            return row;
        }

    }

    public static class DetailDTO {
        int fromEntity;
        int toEntity;
        String relationType;
        LocationDTO location;

        public DetailDTO(int fromEntity, int toEntity, String relationType, int line, int row){
            this.fromEntity = fromEntity;
            this.toEntity = toEntity;
            this.relationType = relationType;
            this.location = new LocationDTO(line, row);
        }

        public int getFromEntity() {
            return fromEntity;
        }

        public int getToEntity() {
            return toEntity;
        }

        public String getRelationType() {
            return relationType;
        }

        public LocationDTO getLocation() {
            return location;
        }

    }

    public static class CellsDTO {
        int srcFile;
        int destFile;
        HashMap<String, Integer> values = new HashMap<>();
        ArrayList<DetailDTO> details = new ArrayList<>();

        public CellsDTO(int srcFile, int destFile){
            this.srcFile = srcFile;
            this.destFile = destFile;
        }

        public void addValue(String relationType){
            if (this.values.containsKey(relationType)){
                int count = this.values.get(relationType) + 1;
                this.values.replace(relationType, count);
            } else {
                this.values.put(relationType, 1);
            }
        }

        public int getSrcFile() {
            return srcFile;
        }

        public int getDestFile() {
            return destFile;
        }

        public HashMap<String, Integer> getValues() {
            return values;
        }

        public ArrayList<DetailDTO> getDetails() {
            return details;
        }

    }

    public static class IndicesDTO {

        String object;
        String file;
        LocationDTO location;
        String type;
        String modifier;
        String rawType;

        public IndicesDTO(String object, String file, int line, int row, String type, String rawType, String modifier){
            this.object = object;
            this.file = file;
            this.location = new LocationDTO(line, row);
            this.type = type;
            this.rawType = rawType;
            this.modifier = modifier;
        }

        public void setModifier(String modifier) {
            this.modifier = modifier;
        }

        public String getModifier(){
            return modifier;
        }

        public String getObject() {
            return object;
        }

        public String getFile() {
            return file;
        }

        public LocationDTO getLocation() {
            return location;
        }

        public String getType() {
            return type;
        }

        public String getRawType() {
            return rawType;
        }
    }

    String name;
    String lang;
    String rootDir;
    Integer nodeNum;
    Integer edgeNum;
    ArrayList<CellsDTO> cells = new ArrayList<>();
    ArrayList<String> variables = new ArrayList<>();
    ArrayList<IndicesDTO> indices = new ArrayList<>();
    Integer indexNum;

    protected SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    public DependsString(){

    }

    public DependsString(String name, String lang, String rootDir){
        this.name = name;
        this.lang = lang;
        this.rootDir = rootDir;
    }

    public void addCell(CellsDTO cell){
        this.cells.add(cell);
    }

    public void addVariables(String filePath){
        this.variables.add(filePath);
    }

    public void addIndice(IndicesDTO indice){
        this.indices.add(indice);
    }

    public String getName() {
        return name;
    }

    public String getLang() {
        return lang;
    }

    public String getRootDir() {
        return rootDir;
    }

    public Integer getNodeNum() {
        return nodeNum;
    }

    public Integer getEdgeNum() {
        return edgeNum;
    }

    public ArrayList<CellsDTO> getCells() {
        return cells;
    }

    public ArrayList<String> getVariables() {
        return variables;
    }

    public ArrayList<IndicesDTO> getIndices() {
        return indices;
    }

    public Integer getIndexNum() {
        return indexNum;
    }

    /**
     * get current entity's file id
     * @param id
     * @return
     */
    public int getCurrentFileId(int id){
        if(singleCollect.getEntityById(id) instanceof FileEntity){
            return id;
        }
        else if (singleCollect.getEntityById(singleCollect.getEntityById(id).getParentId()) instanceof FileEntity){
            return singleCollect.getEntityById(id).getParentId();
        }
        else {
            return getCurrentFileId(singleCollect.getEntityById(id).getParentId());
        }
    }

    public DependsString getDependsString(String projectName, String projectPath, String lang){
        DependsString dependsString = new DependsString(projectName, lang, projectPath);

        for (BaseEntity entity : singleCollect.getEntities()){
            String entityFile;
            try {
                if (entity instanceof PackageEntity){
                    entityFile = null;
                } else {
                    entityFile = ((FileEntity)singleCollect.getEntityById(getCurrentFileId(entity.getId()))).getFullPath();
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println(entity.getQualifiedName());
                entityFile = null;
            }
            String m = "";
            if (!entity.getModifiers().isEmpty()) {
                for (String modifier : entity.getModifiers()) {
                    m = m.concat(modifier + " ");
                }
                try{
                    m = m.substring(0, m.length()-1);
                } catch (StringIndexOutOfBoundsException e){
                    System.out.println("Exception Entity : " + entity.getQualifiedName());
                    System.out.println("Please check its modifiers...");
                }
            }
            IndicesDTO indice = new IndicesDTO(entity.getQualifiedName(), entityFile, entity.getLocation().getStartLine(),
                    entity.getLocation().getStartColumn(), singleCollect.getEntityType(entity.getId()),
                    processRawType(singleCollect.getEntityType(entity.getId()), entity.getRawType()), m);
            dependsString.addIndice(indice);
        }
        dependsString.indexNum = singleCollect.getEntities().size();

        for (Integer fileId : singleCollect.getFileIds()){
            dependsString.addVariables(((FileEntity)singleCollect.getEntityById(fileId)).getFullPath());
        }
        dependsString.nodeNum = dependsString.variables.size();

        JsonMap jsonMap = new JsonMap();
        Map<Integer, ArrayList<Tuple<Integer, Relation>>> relationMap = jsonMap.getFinalRes();
        for(int fromEntity:relationMap.keySet()) {
            if (singleCollect.getEntityById(fromEntity) instanceof PackageEntity){
                continue;
            }
            int src = -1;
            try{
                src = singleCollect.getFileIndex(getCurrentFileId(fromEntity));
            } catch (IndexOutOfBoundsException e){
                continue;
            }
            for (Tuple<Integer,Relation> toEntityObj : relationMap.get(fromEntity)) {
                int toEntity=toEntityObj.getL();
                if (singleCollect.getEntityById(toEntity) instanceof PackageEntity){
                    continue;
                }
                int dest = -1;
                try {
                    dest = singleCollect.getFileIndex(getCurrentFileId(toEntity));
                }catch (IndexOutOfBoundsException e){
                    continue;
                }
                //get current cell
                CellsDTO currentCell = null;
                for (CellsDTO cell : dependsString.cells){
                    if (cell.srcFile == src && cell.destFile == dest){
                        dependsString.cells.remove(cell);
                        currentCell = cell;
                        break;
                    }
                }
                if (currentCell == null && src != -1 && dest != -1){
                    currentCell = new CellsDTO(src, dest);
                }

                if (currentCell != null){
//                    for (String type : relationMap.get(fromEntity).get(toEntity)) {
                    Relation type = toEntityObj.getR();
                        DetailDTO detail = new DetailDTO(fromEntity, toEntity, type.getKind(), 0, 0);
                        currentCell.addValue(type.getKind());
                        currentCell.details.add(detail);
//                    }
                    dependsString.addCell(currentCell);
                }
            }
        }
        dependsString.edgeNum = dependsString.cells.size();

        return dependsString;
    }

    public String processRawType(String entityType, String rawType){
        if (entityType.equals(Configure.BASIC_ENTITY_METHOD)){
            if (singleCollect.getCreatedType().containsKey(rawType)){
                return rawType;
            }else {
                return "<Builtin>";
            }
        }
        if (rawType == null){
            return null;
        }else {
            if (rawType.contains("<")){
                rawType = rawType.split("<")[0];
            } else if (rawType.contains("[")){
                rawType = rawType.split("\\[")[0];
            }

            if (rawType.contains("java")){
                String[] temp = rawType.split("\\.");
                rawType = temp[temp.length - 1];
            }
        }
        return rawType;
    }

}
