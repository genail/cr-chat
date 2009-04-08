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

import pl.graniec.coralreef.network.PacketListener;
import pl.graniec.coralreef.network.server.RemoteClient;

/**
 * User that is connected to running chat server.
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class User {
	/** Chat Server that this client is connected to */
	private final ChatServer server;
	/** RemoteClient of this user */
	private final RemoteClient client;
	/** User's name */
	private String name;
	
	protected User(ChatServer server, RemoteClient client) {
		this.server = server;
		this.client = client;
		
		// FIXME: Check if first packet is lost when I'll put a sleep here
		
		client.addPacketListener(new PacketListener() {
			public void packetReceiver(Object data) {
				handlePacket(data);
			}
		});
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	private void handlePacket(Object data) {
		// TODO: Chat packets handling
	}
}
