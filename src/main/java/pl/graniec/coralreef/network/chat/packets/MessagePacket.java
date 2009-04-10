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
 * Packet sent from client to server when w user want to say
 * something and from server to client when a message to user
 * or the user's room is appeared.
 * <p>
 * There are four types of message:
 * <ul>
 * <li>user to room</li>
 * <li>room to user</li>
 * <li>user to user</li>
 * <li>room to room</li>
 * </ul>
 * 
 * The first one <b>user to room</b> is most basic one.
 * When room user wants to tell something to everyone
 * present in the place then this is the right type
 * of message to send.
 * 
 * <p>
 * The second one <b>room to user</b> is used when
 * a system message (from room) is sent to one user.
 * 
 * <p>
 * The third one <b>user to user</b> is so-called private
 * message. Used when message should be read by one selected
 * person.
 * 
 * <p>
 * The last one is <b>room to room</b>. Is used when a
 * system message is sent to all (room) users.
 * 
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class MessagePacket implements ChatPacket {

	private byte messageType;
	
	private String senderName;
	
	private String receiverName;
	
	private String message;
	
	/**
	 * Creates new <code>message</code> of selected <code>messageType</code>
	 * addressed from <code>senderName</code> to <code>receiverName</code>.
	 * 
	 * @param messageType Message type from {@link MessageType}.
	 * @param senderName User/room name that sent this message.
	 * @param receiverName User/room name that this message is addressed to.
	 * @param message The message body.
	 */
	public MessagePacket(byte messageType, String senderName, String receiverName, String message) {
		this.messageType = messageType;
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.message = message;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return the messageType
	 */
	public byte getMessageType() {
		return messageType;
	}
	
	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}
	
	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}
	

	/*
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		messageType = in.readByte();
		senderName = in.readUTF();
		receiverName = in.readUTF();
		message = in.readUTF();
	}

	/*
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeByte(messageType);
		out.writeUTF(senderName);
		out.writeUTF(receiverName);
		out.writeUTF(message);
	}

}
