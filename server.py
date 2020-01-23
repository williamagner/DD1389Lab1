import re
import socket
from NumberGuessGame import NumberGuessGame


class Server:

    def __init__(self, port: int):
        self.__host: str = '0.0.0.0'
        self.__port: int = port
        self.users = 0
        self.games = {}
        self.__run()
        

    def __run(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as serverSocket:
            serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            serverSocket.bind((self.__host, self.__port))
            serverSocket.listen(5)
            print(f"Listening on port: {self.__port}")

            while True:
                clientSocket, address = serverSocket.accept()
                with clientSocket:
                    print(f"Connection from {address}")

                    # extract parts of response
                    init_line = ""
                    headers = {}
                    while True:
                        line = self.__readLine(clientSocket)
                        if (not line):
                            break

                        if re.match(r"GET\s+.*", line): # matches GET request line
                            init_line = line
                        elif re.match(r"POST\s+.*", line): # matches POST request line
                            init_line = line
                        elif re.match(r"\S+: .*", line): # matches header line
                            key = line.split(":", 2)[0]
                            value = line.split(":", 2)[1]
                            headers[key] = value
                    
                    print("Request start: ", init_line)
                    print("Headers:")
                    print(headers)


                    # set up client ID cookie
                    cookie_header = ""
                    client_id = headers.get("Cookie")
                    if(not (client_id) or not ("clientID" in client_id)):
                        client_id = str(self.users)
                        self.users += 1
                        cookie_header = "Set-Cookie: clientID=" + client_id + "\n"
                    else:
                        client_id = client_id[client_id.find("clientID=") + 9:]


                    # Get game for corresponding client
                    try:
                        game = self.games[client_id]
                    except: # Game does not exist, create new game
                        game = NumberGuessGame()
                        self.games[client_id] = game


                    # Process GET and POST requests
                    html_file = "game.html"
                    if init_line.startswith("GET / HTTP/1.1"):
                        pass
                    elif init_line.startswith("POST"):
                        # Get post body
                        data = clientSocket.recv(int(headers["Content-Length"])).decode(encoding="utf-8", errors="ignore")
                        if (len(data) > 13):
                            data = data.split("=", 2)[1];
                        print("This is the data: ", data)

                        # Call action or reset on game
                        # Does the game need to reset?
                        if (game.gameOver):
                            game.resetGame()
                        else:
                            game.guess(data)
                            # If game over
                            if (game.gameOver):
                                html_file = "gameover.html"
                
                    # Create response
                    with open(html_file) as html_template:
                        payload = html_template.read().replace("!firstline!", game.getState()).replace("!secondline!", game.getAttempts())
                    response = "HTTP/1.1 200 OK\nDate: Mon, 15 Jan 2018 22:14:15 GMT\nContent-Length: " + str(len(payload)) + "\nConnection: close\nContent-Type: text/html\n" + cookie_header + "\n"
                    response += payload
                    # print(" >>> ", response, "\n")
                    clientSocket.sendall(response.encode(encoding="utf-8", errors="ignore"))

            
              

    def __readLine(self, clientSocket: socket.socket) -> str:
        bString: bytes = b""

        while True:
            b: bytes = clientSocket.recv(1)
            if b == b'\r':
                continue
            elif b == b'\n':
                break
            bString += b
        if bString == b"":
            return None
        
        line: str = bString.decode(encoding="utf-8", errors="ignore")
        return line


def main():
    Server(8989)


if __name__ == "__main__":
    main()