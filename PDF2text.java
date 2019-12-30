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
	private static String text;

	public static void main(String[] args) throws IOException {
		String fileName = "Audio_Technica_201805_arlista.pdf";
		PDDocument doc = PDDocument.load(new File(fileName));
		text = new PDFTextStripper().getText(doc);
		replacements();
		writeToFileCSV();
	}

	static void replacements() {
		int count = 0;
		String row = "";
		String amitKeresunkRegex = "(AT\\S+) .*?((\\n[^AT])?).*? ([ \\d]+) Ft.+Ft";
		Pattern amitKeresunkRegexObject = Pattern.compile(amitKeresunkRegex);
		Matcher mIlleszkedesek = amitKeresunkRegexObject.matcher(text);
		while (mIlleszkedesek.find()) {
			row = (mIlleszkedesek.group(1) + ";" + mIlleszkedesek.group(4)).replace(" ", "");
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