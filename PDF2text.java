package pdf2text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDF2text {
	
	private static ArrayList<String> toFile = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        
    	String fileName = "Audio_Technica_201805_arlista.pdf";
        PDDocument doc = PDDocument.load(new File(fileName));
        String text = new PDFTextStripper().getText(doc);
        //System.out.println(text);
        replaced(text);
        writeToFileCSV();
    }
    
    static void replaced(String text) {
    	String row = "";
    	//String amitKeresunkRegex = "\\S+(?=( Audio-Technica)).*Ft(?=(\\nAT))|\\S+(?=( Audio-Technica)).*\\n.*Ft(?=(\\nAT))";
    	String amitKeresunkRegex = "\\S+(?=( Audio-Technica)).*Ft(?=(\\nAT))|\\S+(?=( Audio-Technica)).*\\n.*Ft(?=(\\nAT))";
    	Pattern amitKeresunkRegexObject = Pattern.compile(amitKeresunkRegex);
        Matcher matcherIlleszkedesek = amitKeresunkRegexObject.matcher(text);
        int k, v;
        int count = 0;
        while (matcherIlleszkedesek.find()) {
        	k= matcherIlleszkedesek.start();
        	v= matcherIlleszkedesek.end();
        	row = text.substring(k, v)
        			.replaceFirst("\\n", " ")
        	        .replace(" Ft ", ";")
        	        .replace(" Ft", "")
        			.replaceAll("(?<= \\d+) (?=\\d{3};)", "")
        			.replaceAll("(?<= \\d+) (?=\\d{6};)", "")
        			.replaceAll(" (?=\\d{3}$)", "")
        			.replaceAll(" (?=\\d{6}$)", "")
        			.replaceAll(" (?=\\d+;\\d+$)", "~")
        			.replaceFirst("(?<=^AT.+) ", "~")
        			.replaceFirst("~.+~", ";")
        			;
        	toFile.add(row);
        	System.out.print(++count + ". ");
        	System.out.println(row);
        }
    }

	private static void writeToFileCSV() {

		String time = new Dates().now();
		FileWriter fw;
		try {
			fw = new FileWriter("arlista_" + time + ".csv");
			for (String row : toFile) {
				fw.write(row + "\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

//https://www.regular-expressions.info/refcharacters.html

//https://www.tutorialkart.com/pdfbox/read-text-pdf-document-using-pdfbox-2-0/
//pdfbox-2.0.11.jar + fontbox-2.0.11.jar + commons-logging-1.2.jar
//http://pinter.org/archives/5782

//https://www.ocpsoft.org/opensource/guide-to-regular-expressions-in-java-part-2/
//https://www.javaworld.com/article/2077757/optimizing-regular-expressions-in-java.html?page=4