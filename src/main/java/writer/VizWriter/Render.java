package writer.VizWriter;

import java.io.IOException;

public class Render
{
    /**
     * Read the DOT source from a file,
     * convert to image and store the image in the file system.
     * @param dotFileName   .dot
     * @param renderType
     *             dot
     *             neato
     *             fdp
     *             sfdp
     *             twopi
     *             circo
     * @param destType
     *              .gif
     *              .dot
     *              .pdf
     *              .ps
     *              svg
     *              .png
     *              .plain
     */
	public void run(String dotFileName, String renderType, String destFileName, String destType) {
		String executable = "c:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
		//String[] args = { executable, "-T" + destType, "-K" + renderType, dotFileName, "-o", destFileName };
		String[] args = {executable, "-Tsvg", "-Kfdp", dotFileName, "-o", destFileName };
		Runtime rt = Runtime.getRuntime();
		try {
			Process p = rt.exec(args);
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		GraphViz gv = new GraphViz();
		gv.readSource(dotFileName);
		System.out.println(gv.getDotSource());

		File fp = new File(destFileName);
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), destType, renderType), fp);
		 */
	}

}
