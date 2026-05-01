package sk.ditec.cud.master;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReaderClass {

	public static void readFile(String filename) throws Exception {

		try {
			File myObj = new File(filename);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.contains("sendInboundMessage") || data.contains("taf_prijem_zapis_clob")) {
					System.out.println(data);
				}
			}
			myReader.close();

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		try {
			// readFile("d:\\2025-06-25.comm_pis.log");
			readFile("d:\\2025-06-26.comm_pis.log");

			System.out.println("end");

		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

}
