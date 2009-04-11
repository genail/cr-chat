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
package pl.graniec.coralreef.network.chat.packets;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This packet is sent from client to server when user wants
 * to join a room.
 * <p>
 * If you'll try to join room that you're already in then
 * you'll receive positive response, but nothing in fact
 * will happend.
 * 
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class RoomJoinRequest implements ChatPacket {

	/** Unique name of the room to join */
	private String roomName;
	/** Password to join the room */
	private String password;
	
	/**
	 * Creates a room join request with <code>roomName</code> and
	 * its <code>password</code>.
	 * <p>
	 * Not every room has a password. You can pass a <code>null</code>
	 * or empty string as a <code>password</code>, and this will
	 * mean that there is no password provided.
	 * 
	 * @param roomName Name of room to join.
	 * @param password Password to join the room. Can be <code>null</code> or empty.
	 */
	public RoomJoinRequest(String roomName, String password) {
		
		this.roomName = roomName;
		
		if (password == null) {
			password = "";
		} else {
			this.password = password;
		}
	}

	/**
	 * Provides the password to join a room. If there is no password
	 * then an empty String will be returned.
	 * 
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return the roomName that user wants to join
	 */
	public String getRoomName() {
		return roomName;
	}
	
	/*
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		roomName = in.readUTF();
		password = in.readUTF();
	}

	/*
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(roomName);
		out.writeUTF(password);
	}

}
