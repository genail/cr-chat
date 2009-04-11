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

import javax.naming.Name;

import pl.graniec.coralreef.network.PacketListener;
import pl.graniec.coralreef.network.chat.exceptions.ChatException;
import pl.graniec.coralreef.network.chat.packets.ProtocolPacket;
import pl.graniec.coralreef.network.client.Client;
import pl.graniec.coralreef.network.exceptions.NetworkException;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class ChatClient {
	/** Network client implementation */
	private final Client client;
	/** Name of the chat user */
	private String name;
	
	public ChatClient(Client client, Name name) {
		
		if (client == null || name == null || name.isEmpty()) {
			throw new IllegalArgumentException("params cannot be null/empty");
		}
		
		this.client = client;
	}
	
	public void connect(String host, int port) throws NetworkException, ChatException {
		connect(host, port, "");
	}
	
	public void connect(String host, int port, String password) throws NetworkException, ChatException {
		if (password == null) {
			password = "";
		}
		
		final ProtocolPacket[] protocolPacketContainer = new ProtocolPacket[1];
		protocolPacketContainer[0] = null;

		client.addPacketListener(new PacketListener() {

			public void packetReceiver(Object data) {
				protocolPacketContainer[0] = (ProtocolPacket) data;
				synchronized (this) {
					notify();
				}
			}
			
		});
		
		client.connect(host, port);
		
		// wait for protocol packet
		synchronized (this) {
			try {
				wait(30000);
				// FIXME: throw connection timeout
			} catch (InterruptedException e) {
				// FIXME: finished here: got response
			}
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
