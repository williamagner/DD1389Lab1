import java.io.*;
import java.net.*;

public class javaServer {

    private final int port = 8989;

    public static String put_lastname_in_payload(String response_payload, String firstName) {
        return response_payload.replace("$last_name$", firstName);
    }

    public static String readFile(String filename) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(filename));
        String contents = "";
        String line = "";
        while ((line = file.readLine()) != null)
            contents += line;
        return contents;
    }

    public static String processTheForm() throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("test.html"));
        String contents = "";
        String line = "";
        while ((line = file.readLine()) != null)
            contents += line;
        return contents;
    }

    public static String readPayload(BufferedReader scktIn, String headers) throws IOException {
        int content_length = Integer.parseInt((((headers.split("Content-Length: "))[1]).split("\n"))[0]);
        char[] cbuf = new char[content_length];
        scktIn.read(cbuf, 0, content_length);
        return new String(cbuf);
    }

    public static void main(String[] args) {
        javaServer s = new javaServer();
    }

    public javaServer() {

        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Listening on port: " + this.port);

            while (true) {

                try (Socket socket = serverSocket.accept();
                        BufferedReader scktIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter scktOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    String line;
                    String headers = "";
                    String response = "";
                    String payload = "";
                    numberGuessGame game = new numberGuessGame();
                    String gameState = "Welcome to the Number Guess Game. ";
                    String attemptsString = "Guess a number between 1 and 100.";
                    int guess = 0;
                    while ((line = scktIn.readLine()) != null) { // read
                        System.out.println(" <<< " + line); // log

                        if (line.matches("GET\\s+.*")) {
                            // process the GET request
                            while (!line.equals("")) {
                                headers += line + "\n";
                                line = scktIn.readLine();
                            }
                            System.out.println("Got a GET-request. Headers: \n" + headers + "<");

                        } else if (line.matches("POST\\s+.*")) {
                            // process the POST request
                            int a = 0;
                            System.out.println("ENTERING WHILE");
                            String contentLength = "";
                            while (!line.equals("")) {
                                headers += line + "\n";
                                line = scktIn.readLine();
                                if(line.matches("Content-Length.*")){
                                    contentLength = line.substring(16, 18);
                                    System.out.println(contentLength);
                                }
                            }
                            System.out.println("OUT OF WHILE");
                            String data = "";
                            System.out.println(contentLength);
                            for(int i=0; i<Integer.parseInt(contentLength); i++){
                                data = data + scktIn.read();
                            }
                            System.out.println("Got a POST-request");
                            System.out.println("Headers: " + headers);
                            System.out.println("Data: " + data);
                            /* Get the guess from the data
                            data = data.replace("gissadeTalet=", "");
                            guess = Integer.parseInt(data);
                            game.guess(guess);  */
                            attemptsString = "You have made " + Integer.toString(game.attempts) + " guesses.";
                            gameState = game.gameState;
                        }
                        payload = readFile("guess.html").replaceAll("!firstline!", gameState).replaceAll("!secondline!", attemptsString);
                        response = "HTTP/1.1 200 OK\nDate: Mon, 15 Jan 2018 22:14:15 GMT\nContent-Length: "
                                + payload.length() + "\nConnection: close\nContent-Type: text/html\n\n";
                        response += payload;
                        System.out.println(" >>> " + response); // log
                        scktOut.write(response); // write
                        scktOut.flush(); // flush
                    }

                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }

            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not listen on port: " + this.port);
            System.exit(1);
        }
    }

}