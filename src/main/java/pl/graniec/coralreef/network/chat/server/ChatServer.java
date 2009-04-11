/**
 * Copyright (c) 2009, Coral Reef Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of the Coral Reef Project nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package pl.graniec.coralreef.network.chat.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import pl.graniec.coralreef.network.PacketListener;
import pl.graniec.coralreef.network.exceptions.NetworkException;
import pl.graniec.coralreef.network.server.ConnectionListener;
import pl.graniec.coralreef.network.server.RemoteClient;
import pl.graniec.coralreef.network.server.Server;

/**
 * The chat server.
 * <p>
 * You can run the chat server in two modes:
 * <ul>
 * <li>Standalone Mode</li>
 * <li>Shared Mode</li>
 * </ul>
 * 
 * The <b>Standalone Mode</b> acts as chat server is only
 * service running on given {@link Server} implementation. In this
 * case when chat server is opened the {@link Server#open(int)} method
 * is invoked and when chat server is closed the {@link Server#close()}
 * is invoked.
 * <p>
 * The second mode is called <b>Shared Mode</b>. When initialized, the
 * server acts as {@link Server} is opened and closed by some external
 * mechanism, and it can receive packets that are not destined for the
 * chat server. It only reacts to chat server packets.
 * <p>
 *  
 * 
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class ChatServer {
	
	private Logger logger = Logger.getLogger(ChatServer.class.getName());
	
	/** The server implementation */
	private final Server server;
	/** Port of the server */
	private final int port;
	/** Server state */
	private boolean open;
	/** Standalone mode? */
	private final boolean standalone;
	
	/** Map of connected users: RemoteClient => User */
	protected final Map users = new HashMap();
	/** Map of all rooms: String => Room */
	protected final Map rooms = new HashMap();
	
	/** Connection listener */
	private ConnectionListener connectionListener;
	/** Packet listener */
	private PacketListener packetListener;
	
	/**
	 * Creates new chat server in <b>Shared Mode</b>.
	 * You should pass a wanted implementation
	 * of {@link Server} to the constructor. In this case you can run a chat
	 * server on every service that can act as Server directs.
	 * 
	 * @param server Server implementation.
	 */
	public ChatServer(Server server) {
		this.server = server;
		this.port = -1;
		
		this.standalone = false;
	}
	
	/**
	 * Creates new chat server in <b>Standalone Mode</b>.
	 * You should pass a wanted implementation
	 * of {@link Server} to the constructor. In this case you can run a chat
	 * server on every service that can act as Server directs.
	 *
	 * @param server Server implementation.
	 * @port port Port to run on.
	 */
	public ChatServer(Server server, int port) {
		this.server = server;
		this.port = port;
		
		this.standalone = true;
	}
	
	public void close() {
		
		if (!open) {
			throw new IllegalStateException("chat server is not open");
		}

		server.removeConnectionListener(connectionListener);
		
		if (standalone) {
			server.close();
		}
		
		open = false;
	}
	
	public void open() throws NetworkException {
		
		if (open) {
			throw new IllegalStateException("chat server is already open");
		}
		
		if (standalone) {
			server.open(port);
		} else if (!server.isOpen()) {
			logger.warning("Running in shared mode but server is still offline.");
		}
		
		connectionListener = new ConnectionListener() {

			public void clientConnected(RemoteClient client) {
				handleClientConnected(client);
			}

			public void clientDisconnected(RemoteClient client, int reason, String reasonString) {
				handleClientDisconnected(client, reason, reasonString);
			}
			
		};
		
		server.addConnectionListener(connectionListener);
		
		open = true;
	}
	
	private void handleClientConnected(RemoteClient client) {
		final User user = new User(this, client);
		
		synchronized (users) {
			users.put(client, user);
		}
	}
	
	private void handleClientDisconnected(RemoteClient client, int reason, String reasonString) {
		
	}
}
