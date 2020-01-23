import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server {

    private final int port = 8989;
    private int users = 0;

    public static void main(String[] args){
        Server s = new Server();
    }

    public Server() { 
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Listening on port: " + this.port);

            //NumberGuessGame g = new NumberGuessGame();
            HashMap<String,NumberGuessGame> games = new HashMap<>();

            while (true) {

                
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    String line;
                    HashMap<String, String> headers = new HashMap<>();
                    String initLine = "";
                    

                    while (!(line = in.readLine()).isEmpty()) { // read
                        //System.out.println(" <<< " + line); // log

                        if (line.matches("GET\\s+.*")) {
                            initLine = line;
                        } 
                        else if (line.matches("POST\\s+.*")) {
                            initLine = line;
                        }
                        else if (line.matches("\\S+: .*")){
                            String key = line.split(" ",2)[0];
                            key = key.substring(0, key.length()-1);
                            String value = line.split(" ",2)[1];
                            headers.put(key, value);
                        }
                    }
                    
                    String cookieString = "";
                    String htmlString = "game.html";
                    //Link the client to their game
                    String clientid = headers.get("Cookie");
                    if ( clientid == null || clientid.indexOf("clientID") == -1){
                        //make a new clientid
                        clientid = Integer.toString(users);
                        users += 1;
                        cookieString = "Set-Cookie: clientID=" + clientid + "\n";
                    } else{
                        clientid = clientid.substring(clientid.indexOf("clientID")+9);
                    }
                    System.out.println("This is the clientID: " + clientid);

                    NumberGuessGame g = games.get(clientid);
                    //If they dont have a game, create a new game
                    if(g == null){
                        // make a new game
                        g = new NumberGuessGame();
                        // give client a client_id in cookie
                        games.put(clientid, g);
                        // link client_id to game in map
                    }

                    if(initLine.startsWith("GET / HTTP/1.1")){
                        System.out.println("FOUND A REQUEST");
                        System.out.println("Init: " + initLine);
                        System.out.println("headers: " + headers + "\n\n\n\n");

                        
                            
                    }
                    else if(initLine.startsWith("POST")){
                        System.out.println("FOUND A REQUEST");
                        System.out.println("Init: " + initLine);
                        System.out.println("headers: " + headers + "\n\n\n\n");

                        String data = "";
                        for(int i = Integer.parseInt(headers.get("Content-Length")); i > 0; i--){
                            data += (char) in.read();
                        }

                        // If the input was empty
                        if(data.length() > 13){
                            data = data.split("=", 2)[1];
                        }
                        System.out.println("THIS IS THE DATA " + data);
                        //Does the game need to reset?
                        if (g.gameOver){
                            g.resetGame();
                        }else{
                            g.guess(data);
                            //If game over
                            if (g.gameOver){
                                htmlString = "gameover.html";
                            }
                        }

                    }
                    String payload = readFile(htmlString).replaceAll("!firstline!", g.getState()).replaceAll("!secondline!", g.getAttempts());
                    String response = "HTTP/1.1 200 OK\nDate: Mon, 15 Jan 2018 22:14:15 GMT\nContent-Length: "
                            + payload.length() + "\nConnection: close\nContent-Type: text/html\n" + cookieString + "\n";
                    response += payload;
                    //System.out.println(" >>> " + response); // log
                    out.write(response); // write
                    out.flush(); // flush

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

    public static String readFile(String filename) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(filename));
        String contents = "";
        String line = "";
        while ((line = file.readLine()) != null)
            contents += line;
        file.close();
        return contents;
    }
}