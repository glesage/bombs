package hmk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class TestClient {

	private static int PORT_BASE = 6000;
	private static boolean TIMING = false;
	// private static String SERVER_HOST = "localhost";
	private static String SERVER_HOST = "wmarrerowww.cstcis.cti.depaul.edu";
	private static ArrayList<String> problem = new ArrayList<String>();
	// private static final String MAGIC_NUMBER = "Hastings1066";
	public static final String MAGIC_NUMBER = null;

	public static void main(String[] args) throws Exception {
		if (args.length < 2 || args.length > 3) {
			System.out.println("usage: java TestClient lname HW_num [-t]");
			System.out.println("where lname is replaced with your last name");
			System.out
					.println("where HW_num is replaced with the homework number");
			System.out.println();
			System.out.println("For example:   java TestClient marrero 1");
			System.exit(1);
		}
		if (args.length == 3) {
			if (!args[2].equals("-t")) {
				System.out.println("usage: java TestClient lname HW_num [-t]");
				System.out
						.println("If the third argument is present, it must be -t");
				System.exit(1);
			} else
				TIMING = true;
		}
		int hw = Integer.parseInt(args[1]);
		if (hw < 1 || hw > 10) {
			System.out.println("The HW number must be between 1 and 10");
			System.exit(1);
		}
		String name = args[0];
		System.setProperty("line.separater", "\r\n");
		try {
			Socket sock = new Socket(SERVER_HOST, PORT_BASE + hw);
			PrintWriter out = new PrintWriter(sock.getOutputStream());
			InputStreamReader temp = new InputStreamReader(
					sock.getInputStream());
			BufferedReader in = new BufferedReader(temp);
			if (MAGIC_NUMBER != null) {
				out.println(MAGIC_NUMBER);
				out.flush();
			}

			out.println(name);
			out.flush();
			String response = in.readLine();
			if (response.equals("No such user")) {
				System.out.println("The name \"" + name
						+ "\" could not be found on " + "the course roster.");
				System.out
						.println("Please make sure to use only your last name and try again.");
				System.out
						.println("If you continue to receive this message, please contact the instructor.");
			} else {
				run(in, out);
				out.flush();
				System.out.println("Test is finished.");
				System.out.println("Don't forget to RELOAD all pages in your "
						+ "browser when checking the server website.");
			}
			sock.close();
		} catch (ConnectException ce) {
			System.out.println("There does not seem to be a server for HW" + hw
					+ " running.");
			System.out
					.println("Are you sure you have the HW number correct and");
			System.out.println("that the deadline has not passed?");
		} catch (IOException e) {
			System.out.println("An exception occured while running the tests.");
			System.out.println("Make sure to check the server log for details");
			System.out.println("    about what happened.");
		}
	}

	private static void run(BufferedReader in, PrintWriter out)
			throws IOException {
		ArrayList<String> answer;
		boolean hasMore = getProblem(in);
		while (hasMore) {
			long start = System.currentTimeMillis();
			answer = Solution.solve(problem);
			long finish = System.currentTimeMillis();
			sendAnswer(out, answer);
			if (TIMING)
				System.out.println("Time taken: " + (finish - start)
						+ "milliseconds");
			hasMore = getProblem(in);
		}
	}

	private static boolean getProblem(BufferedReader in) throws IOException {
		problem.clear();
		String line = in.readLine();
		if (line.length() == 0) {
			return false;
		}
		while (line.length() != 0) {
			problem.add(line);
			line = in.readLine();
		}
		return true;
	}

	private static void sendAnswer(PrintWriter out, ArrayList<String> answer) {
		for (String s : answer) {
			out.println(s);
		}
		out.println();
		out.flush();
	}
}
