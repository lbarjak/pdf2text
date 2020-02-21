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
		String fileName = "mipro_arlista.pdf";
		PDDocument doc = PDDocument.load(new File(fileName));
		text = new PDFTextStripper().getText(doc);
		//System.out.println(text);
		// audioTechnica();
		mipro();
		// writeToFileCSV();
	}

	static void audioTechnica() {
		int count = 0;
		String row = "";
		String amitKeresunkRegex = "(AT\\S+) .*?(\\n[^AT].*?)? ([ \\d]+) Ft.+Ft";// Audio Technica
		Pattern amitKeresunkRegexObject = Pattern.compile(amitKeresunkRegex);
		Matcher mIlleszkedesek = amitKeresunkRegexObject.matcher(text);
		while (mIlleszkedesek.find()) {
			row = (mIlleszkedesek.group(1) + ";");
			if (mIlleszkedesek.group(2) != null) {
				row = row + mIlleszkedesek.group(2).replaceAll("\\D", "");
			}
			row = row + mIlleszkedesek.group(3).replaceAll("\\s", "");
			toFile.add(row);
			System.out.print(++count + ". ");
			System.out.println(row);
		}
	}

	static void mipro() {
		String cikkszam = "";
		String arak = "";
		String rowRegex = "(\\d{6}C?)(?:.|\\n)+?(\\d{0,3} ?\\d{1,3}) Ft.+";
		Pattern rowRegexObject = Pattern.compile(rowRegex);
		Matcher rowIlleszkedesek = rowRegexObject.matcher(text);
		while (rowIlleszkedesek.find()) {
			cikkszam = rowIlleszkedesek.group(1);
			arak = rowIlleszkedesek.group(2).replace(" ", "");
			System.out.println(cikkszam + ";" + arak);
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