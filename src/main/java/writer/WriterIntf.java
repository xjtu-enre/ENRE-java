package writer;

import TempOutput.CreateFileUtil;
import TempOutput.Django;
import TempOutput.JsonMap;
import formator.fjson.JDepObject;
import formator.fxml.XDepObject;
import util.Configure;

import java.util.ArrayList;

public class WriterIntf {

    Configure configure = Configure.getConfigureInstance();

    public void run(JDepObject jDepObject, XDepObject xDepObject, ArrayList<String[]> nodes, ArrayList<String[]> edges) {

        //output data by writers
        /**
         *json_dep
         */
//        JsonWriter jsonWriter = new JsonWriter();
//        jsonWriter.toJson(jDepObject, configure.getOutputJsonFile());
//        System.out.println("Export "+ configure.getOutputJsonFile());

        /**
         * full info
         */
//        JsonMap jsonMap = new JsonMap();
//        CreateFileUtil.createJsonFile(configure.getAnalyzedProjectName()+ "-Diango-out",configure.getAnalyzedProjectName()+ "-node", Django.nodeWriter());
//        CreateFileUtil.createJsonFile(configure.getAnalyzedProjectName()+ "-Diango-out",configure.getAnalyzedProjectName()+ "-edge", Django.edgeWriter(jsonMap.getFinalRes()));

//        XmlWriter xmlWriter = new XmlWriter();
//        xmlWriter.toXml(xDepObject, configure.getOutputXmlFile() );
//        System.out.println("Export "+ configure.getOutputXmlFile());
//
//        CsvWriter csvWriter = new CsvWriter();
//        csvWriter.writeCsv(nodes, configure.getOutputCsvNodeFile());
//        System.out.println("Export "+configure.getOutputCsvNodeFile());
//        csvWriter.writeCsv(edges, configure.getOutputCsvEdgeFile());
//        System.out.println("Export "+configure.getOutputCsvEdgeFile());
//
//        DotWriter dotWriter = new DotWriter();
//        String fileName1 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_NO_DEP + ".dot";
//        String fileName2 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FILE_FOLDER_DEP + ".dot";
//        String fileName3 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_CLASS_DEP + ".dot";
//        String fileName4 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FUNC_CLASS_DEP + ".dot";
//        String fileName5 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FUNCTION_DEP + ".dot";
//        String fileName6 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_DEFAULT_DEP + ".dot";
//
//        dotWriter.writeDot(DotUtil.FILTER_NO_DEP, fileName1);
//        dotWriter.writeDot(DotUtil.FILTER_FILE_FOLDER_DEP, fileName2);
//        dotWriter.writeDot(DotUtil.FILTER_CLASS_DEP, fileName3);
//        dotWriter.writeDot(DotUtil.FILTER_FUNC_CLASS_DEP, fileName4);
//        dotWriter.writeDot(DotUtil.FILTER_FUNCTION_DEP, fileName5);
//        dotWriter.writeDot(DotUtil.FILTER_DEFAULT_DEP, fileName6);

    }

    public void undTest() {
        UndWriter undWriter = new UndWriter();
        undWriter.writeUnd();
    }


//    public void exportImplicitExternalAtFileLevel() {
//        ImplicitCallWriter implicitCallWriter = new ImplicitCallWriter();
//        implicitCallWriter.writeImplicitCalls();
//    }

}
