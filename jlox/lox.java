import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class lox {
	static boolean hadError = false;

	public static void main(String[] args) throws IOException {

		if (args.length > 1) {
			System.out.println("Usage: jlox [script]");
			System.exit(64);
		} else if (args.length == 1) {
			runFile(args[0]);
		} else {
			runPrompt();
		}
	}

	// Gets the path of the file and executes it
	private static void runFile(String path) throws IOException {

		byte[] bytes = Files.readAllBytes(Paths.get(path));
		run(new String(bytes, Charset.defaultCharset()));

		if (hadError) {
			System.exit(65);
		}

	}

	// Interactive mode where the code can be excuted while being typed in
	private static void runPrompt() throws IOException {

		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		// infinite loop
		for (;;) {

			System.out.println("> ");
			String line = reader.readLine(); // reads in the code line by line

			if (line == null) {

				break;
			}

			run(line);

			hadError = false;

		}
	}

	private static void run(String source) {

		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();

		for (Token token : tokens) {
			System.out.println(token);
		}

	}

	static void error(int line, String message) {
		report(line, "", message);
	}

	private static void report(int line, String where, String message) {

		System.err.println("[line " + line + "] Error" + where + ": " + message);
		hadError = true;
	}
}
