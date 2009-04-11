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

import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pl.graniec.coralreef.network.PacketListener;
import pl.graniec.coralreef.network.chat.packets.ChatPacket;
import pl.graniec.coralreef.network.chat.packets.JoinRejectReason;
import pl.graniec.coralreef.network.chat.packets.MessagePacket;
import pl.graniec.coralreef.network.chat.packets.MessageType;
import pl.graniec.coralreef.network.chat.packets.RegisterRejectReason;
import pl.graniec.coralreef.network.chat.packets.RoomJoinRequest;
import pl.graniec.coralreef.network.chat.packets.RoomJoinResponse;
import pl.graniec.coralreef.network.chat.packets.UserRegisterRequest;
import pl.graniec.coralreef.network.chat.packets.UserRegisterResponse;
import pl.graniec.coralreef.network.exceptions.NetworkException;
import pl.graniec.coralreef.network.server.RemoteClient;

/**
 * User that is connected to running chat server.
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class User {
	
	private final static String NAME_EXPRESSION = "^[a-zA-Z0-9_-\\.]+$";
	
	/** Chat Server that this client is connected to */
	private final ChatServer server;
	/** RemoteClient of this user */
	private final RemoteClient client;
	/** User's name */
	private String name;
	/** Rooms that user is in: String => Room */
	private final Map rooms = new HashMap();
	
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
		if (!(data instanceof ChatPacket)) {
			return;
		}
		
		Class dataClass = data.getClass();
		
		if (dataClass == UserRegisterRequest.class) {
			handleUserRegisterRequest((UserRegisterRequest) data);
		} 
		else if (dataClass == RoomJoinRequest.class) {
			handleRoomJoinRequest((RoomJoinRequest) data);
		}
	}

	private void handleRoomJoinRequest(RoomJoinRequest data) {
		final String roomName = data.getRoomName();
		final String password = data.getPassword();
		
		try {
		
			if (!rooms.containsKey(roomName)) {
				// not in this room
				synchronized (server.rooms) {
					
					final Room room = (Room) server.rooms.get(roomName);
					
					if (room == null) {
						// room doesn't exist
						client.send(
								new RoomJoinResponse(
										false,
										JoinRejectReason.DoNotExists
								)
						);
						
						return;
					}
					
					
					if (!room.getPassword().equals(password)) {
						// wrong password or needed password
						client.send(
								new RoomJoinResponse(
										false,
										(password.isEmpty() ? JoinRejectReason.PasswordNeeded : JoinRejectReason.WrongPassword)
								)
						);
						
						return;
					}
					
					joinTo(room);
					
				}
			}
		
		} catch (NotSerializableException e) {
			e.printStackTrace();
		} catch (NetworkException e) {
			// disconnected? I cannot do anything about it
		}
	}

	private void joinTo(Room room) {
		rooms.put(room.getName(), room);
		room.joinUser(this);
	}

	private void handleUserRegisterRequest(UserRegisterRequest data) {
		
		final String wantedName = data.getName();
		boolean found = false;

		try {
			
			// TODO: Do the version check
			
			// check if name is legal
			if (!wantedName.matches(NAME_EXPRESSION)) {
				client.send(
						new UserRegisterResponse(
								false,
								RegisterRejectReason.IllegalUserName
						)
				);
				return;
			}
			
			// check if name is free
			synchronized (server.users) {
				for (Iterator itor = server.users.values().iterator(); itor.hasNext(); ) {
					User user = (User) itor.next();
					
					if (wantedName.equals(((User)user).name)) {
						found = true;
						break;
					}
					
				}
				
				if (found) {
					// name already in use
					client.send(
							new UserRegisterResponse(
									false,
									RegisterRejectReason.UserNameAlreadyInUse
							)
					);
					return;
				}
				
				// accept
				name = wantedName;
				
				client.send(
						new UserRegisterResponse(
								true,
								(byte) 0
						)
				);
			}
			
		} catch (NotSerializableException e) {
			e.printStackTrace();
		} catch (NetworkException e) {
			// disconnected? I cannot do anything about it
		}
	}

	public void notifyUserJoined(Room room, User user) {
		// TODO: not finished
	}
}
