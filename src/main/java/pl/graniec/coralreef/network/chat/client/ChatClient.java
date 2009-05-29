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
package pl.graniec.coralreef.network.chat.client;


import java.io.NotSerializableException;
import java.util.logging.Logger;

import pl.graniec.coralreef.network.PacketListener;
import pl.graniec.coralreef.network.chat.Protocol;
import pl.graniec.coralreef.network.chat.exceptions.ChatException;
import pl.graniec.coralreef.network.chat.exceptions.ChatServerTimeoutException;
import pl.graniec.coralreef.network.chat.exceptions.IllegalUserNameException;
import pl.graniec.coralreef.network.chat.exceptions.ProtocolVersionMismatchException;
import pl.graniec.coralreef.network.chat.exceptions.UserNameAlreadyInUseException;
import pl.graniec.coralreef.network.chat.exceptions.WrongPasswordExcepion;
import pl.graniec.coralreef.network.chat.packets.ProtocolPacket;
import pl.graniec.coralreef.network.chat.packets.RegisterRejectReason;
import pl.graniec.coralreef.network.chat.packets.UserRegisterRequest;
import pl.graniec.coralreef.network.chat.packets.UserRegisterResponse;
import pl.graniec.coralreef.network.client.Client;
import pl.graniec.coralreef.network.exceptions.NetworkException;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class ChatClient {
	
	private static class ObjectContainer {
		Object object;
	}
	
	private static final Logger logger = Logger.getLogger(ChatClient.class.getName());
	
	/** Time to wait for response from server */
	private static final int ANSWER_TIMEOUT = 30000;
	
	/** Network client implementation */
	private final Client client;
	/** Name of the chat user */
	private String name;
	
	public ChatClient(Client client, String name) {
		
		if (client == null || name == null || name.isEmpty()) {
			throw new IllegalArgumentException("params cannot be null/empty");
		}
		
		this.client = client;
		this.name = name;
	}
	
	public void connect(String host, int port) throws NetworkException, ChatException {
		connect(host, port, "");
	}
	
	public void connect(String host, int port, String password) throws NetworkException, ChatException {
		if (password == null) {
			password = "";
		}
		
		final ObjectContainer objContainer = new ObjectContainer();

		final PacketListener listener = new PacketListener() {
			public void packetReceived(Object data) {
				
				if (!(data instanceof ProtocolPacket)) {
					logger.warning("Got trash from server: " + data.getClass().getName());
					return;
				}
				
				objContainer.object = data;
				
				synchronized (this) {
					notify();
				}
			}
		};
		
		client.addPacketListener(listener);
		
		try {
		
			client.connect(host, port);
			
			// wait for protocol packet
			synchronized (this) {
				wait(ANSWER_TIMEOUT);
			}
			
			throw new ChatServerTimeoutException("chat server didn't respond in time");
					
					
		} catch (InterruptedException e) {
			// got packet
		} finally {
			client.removePacketListener(listener);
		}

		final ProtocolPacket protocolPacket = (ProtocolPacket) objContainer.object;
		
		if (protocolPacket.getVersion() != Protocol.VERSION) {
			throw new ProtocolVersionMismatchException(
					"server protocol version (" + protocolPacket.getVersion() +
					") differs from client's one (" + Protocol.VERSION + ")"
			);
		}
		
		registerUser(name, password);
		
	}
	
	private void registerUser(String name, String password) throws NetworkException, ChatException {
		
		final ObjectContainer objContainer = new ObjectContainer();
		
		final PacketListener listener = new PacketListener() {
			public void packetReceived(Object data) {
				if (!(data instanceof UserRegisterResponse)) {
					logger.warning("got trash from server: " + data.getClass().getName());
					return;
				}
				
				objContainer.object = data;
			}
		};
		
		client.addPacketListener(listener);
		
		// send the registration packet and wait for response
		try {
			client.send(new UserRegisterRequest(name, password));
			
			synchronized (this) {
				wait(ANSWER_TIMEOUT);
			}
			
			// timeout
			throw new ChatServerTimeoutException("chat server didn't respond in time");
			
		} catch (NotSerializableException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			// got packet
		} finally {
			client.removePacketListener(listener);
		}
		
		final UserRegisterResponse response = (UserRegisterResponse) objContainer.object;
		
		if (response.isSucceed()) {
			return;
		}
		
		switch (response.getFailReason()) {
			case RegisterRejectReason.IllegalUserName:
				throw new IllegalUserNameException("user name '" + name + "' is illegal on this server");
			case RegisterRejectReason.UserNameAlreadyInUse:
				throw new UserNameAlreadyInUseException("user name '" + name + "' is already in use");
			case RegisterRejectReason.WrongPassword:
				throw new WrongPasswordExcepion("wrong password");
				
		}
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
