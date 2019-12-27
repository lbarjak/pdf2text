package pdf2text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDF2text {

    public static void main(String[] args) throws IOException {
        
    	String fileName = "Audio_Technica_201805_arlista.pdf";
        PDDocument doc = PDDocument.load(new File(fileName));
        String text = new PDFTextStripper().getText(doc);
        //System.out.println(text);
        replaced(text);
//        List<String> rows = Arrays.asList(text.split("\\n"));
//        for(String item : rows) {
//        	System.out.println(replaced(item));
//        }
//        System.out.println(rows);

//        String out = fileName.replace("pdf", "txt");
//        try (FileWriter fw = new FileWriter(out)) {
//            fw.write(text);
//        }
    }
    
    static void replaced(String text) {
    	String result = "";
    	String amitKeresunkRegex = "(Hívjon!$&&AT-.+(?=(\\nAT))|AT-.+\\n.+Ft(?=(\\nAT)))";
        Pattern amitKeresunkRegexObject = Pattern.compile(amitKeresunkRegex);
        Matcher matcherIlleszkedesek = amitKeresunkRegexObject.matcher(text);
        int k, v;
        while (matcherIlleszkedesek.find()) {
        	k= matcherIlleszkedesek.start();
        	v= matcherIlleszkedesek.end();
        	//System.out.print(k + " - " + v + " ");
        	//(?<=foo)bar(?=bar)
        	result = text.substring(k, v)
        			.replaceFirst("\\n", " ")
        	        .replace(" Ft ", ";")
        	        .replace(" Ft", "")
        			.replaceAll("(?<= \\d+) (?=\\d{3};)", "")
        			.replaceAll(" (?=\\d{3}$)", "")
        			.replaceAll(" (?=\\d+;\\d+$)", "~")
        			.replaceFirst("(?<=^AT-.+) ", "~")
        			.replaceFirst("~.+~", ";")
        			;
        	System.out.println(result);
        }
    }
}
//https://www.regular-expressions.info/refcharacters.html

//https://www.tutorialkart.com/pdfbox/read-text-pdf-document-using-pdfbox-2-0/
//pdfbox-2.0.11.jar + fontbox-2.0.11.jar + commons-logging-1.2.jar
//http://pinter.org/archives/5782

//https://www.ocpsoft.org/opensource/guide-to-regular-expressions-in-java-part-2/
//https://www.javaworld.com/article/2077757/optimizing-regular-expressions-in-java.html?page=4