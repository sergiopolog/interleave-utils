package org.recreativospiscis.interleaveutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * INTERLEAVE UTILS
 *
 */
public class App {

	public static void main(String[] args) throws Exception {
		if ((args.length == 2 && "deinterleave".equals(args[0]))
				|| ((args.length == 3 || args.length == 4) && "interleave".equals(args[0]))) {
			switch (args[0]) {
			case "interleave":
				byte[] file1 = readFile(args[1]);
				byte[] file2 = readFile(args[2]);
				byte[] interleavedFile = interleaveFiles(file1, file2);
				String outputFilename = args.length == 4 ? args[3] : "interleavedFile.bin";
				writeFile(outputFilename, interleavedFile);
				System.out.println("Successfully interleave input files into file: " + outputFilename);
				break;
			case "deinterleave":
				byte[] file = readFile(args[1]);
				byte[][] deinterleavedFiles = deinterleaveFile(file);
				writeFile("0_" + args[1], deinterleavedFiles[0]);
				writeFile("1_" + args[1], deinterleavedFiles[1]);
				System.out.println("Successfully deinterleave input file into files: " + "0_" + args[1] + " and " + "1_"
						+ args[1]);
				break;
			default:
				break;
			}
		} else {
			System.err.println(
					"Invalid number of arguments. Usage: interleave-utils.jar interleave input_filename1 input_filename2 | deinterleave input_filename");
			return;
		}
	}

	private static byte[] readFile(String fileName) throws IOException {
		if (!Files.exists(Paths.get(fileName))) {
			throw new IOException("Input file not exist: " + fileName);
		}
		return Files.readAllBytes(Paths.get(fileName));
	}

	private static void writeFile(String fileName, byte[] content) throws IOException {
		if (Files.exists(Paths.get(fileName))) {
			throw new IOException("Output file still exist, do not overwrite it: " + fileName);
		}
		Files.write(Paths.get(fileName), content);
	}

	private static byte[] interleaveFiles(byte[] file1, byte[] file2) throws Exception {
		if (file1.length != file2.length) {
			throw new Exception("Size of files provided are not the same.");
		}
		byte[] interleavedFile = new byte[file1.length + file2.length];
		for (int i = 0; i < file1.length; i++) {
			interleavedFile[i * 2] = file1[i];
			interleavedFile[(i * 2) + 1] = file2[i];
		}
		return interleavedFile;
	}

	private static byte[][] deinterleaveFile(byte[] file) throws Exception {
		byte[] deinterleavedFile1 = new byte[file.length / 2];
		byte[] deinterleavedFile2 = new byte[file.length / 2];
		for (int i = 0; i < file.length; i += 2) {
			deinterleavedFile1[i / 2] = file[i];
			deinterleavedFile2[i / 2] = file[i + 1];
		}
		return new byte[][] { deinterleavedFile1, deinterleavedFile2 };
	}
}