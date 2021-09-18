package client;

import TempOutput.Verification;
import util.Configure;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) throws Exception {

        if(args.length < 2) {
            System.out.println("Not enough parameters!");
            exit(1);
        }

        if(!args[0].equals(Configure.getConfigureInstance().getLang())) {
            System.out.println("Not support this language: " + args[0]);
            exit(1);
        }

        TemplateWork templateWork = new TemplateWork();

        templateWork.workflow(args);

//        String[] cmd = {"java", "C:\\Users\\pc\\Desktop\\test_project\\halo-1.4.10", null, "halo-1.4.10"};
//        templateWork.workflow(cmd);

    }
}
