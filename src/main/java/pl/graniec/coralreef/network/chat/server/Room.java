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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class Room {
	/** Maximal number of users in this room. 0 means infinity */
	private int maxUsers;
	/** Name of this room */
	private String name;
	/** Room password. Empty means no password */
	private String password;
	/** Users listing */
	private final Set users = new HashSet();
	
	/**
	 * Creates a new room called <code>name</code> with
	 * empty password (everyone can enter).
	 * 
	 * @param name Name of this room.
	 */
	public Room(String name) {
		this(name, "");
	}
	
	/**
	 * Creates a new room called <code>name</code> with
	 * given <code>password</code>. If given password is
	 * empty then room has no passowrd.
	 * 
	 * @param name Name of this room.
	 * @param password Access password.
	 */
	public Room(String name, String password) {
		
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name cannot be null nor empty");
		}
		
		if (password == null) {
			throw new IllegalArgumentException("password cannot be null");
		}
		
		this.name = name;
		this.password = password;
	}
	
	/**
	 * Provides the number of users that can be in this room at once.
	 * <p>
	 * The <code>0</code> value means no limit.
	 * 
	 * @return the maxUsers Maximum number of users.
	 * 
	 * @see #setMaxUsers(int)
	 */
	public int getMaxUsers() {
		return maxUsers;
	}
	
	/**
	 * @return The name of this room.
	 * 
	 * @see #setName(String)
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the password of this room.
	 * 
	 * @see #setPassword(String)
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return complete set of room users.
	 */
	public User[] getUsers() {
		synchronized (users) {
			return (User[]) users.toArray(new User[users.size()]);
		}
	}
	
	/**
	 * Sets the number of users that can be in this room at once.
	 * If this number is reached then no user is allowed to join
	 * until somebody leaves.
	 * <p>
	 * The <code>0</code> value means no limit.
	 * 
	 * @param maxUsers the maxUsers to set
	 * 
	 * @see #setMaxUsers(int)
	 */
	public void setMaxUsers(int maxUsers) {
		
		if (maxUsers < 0) {
			throw new IllegalArgumentException("you can specify value only >= 0");
		}
		
		this.maxUsers = maxUsers;
	}
	
	/**
	 * Sets the <code>name</code> of this room.
	 * 
	 * @param name the name to set.
	 * 
	 * @see #getName()
	 */
	public void setName(String name) {
		
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name cannot be null nor empty");
		}
		
		this.name = name;
	}
	
	/**
	 * Sets the <code>password</code> for this room. If password
	 * is set then only users that can provide a correct password
	 * can join the room.
	 * <p>
	 * Empty password means that the room has no password.
	 * You can pass <code>null</code> so this will be treated as no
	 * password as well.
	 * 
	 * @param password the password to set
	 * 
	 * @see Room#getPassword()
	 */
	public void setPassword(String password) {
		
		if (password == null) {
			this.password = "";
		} else {
			this.password = password;
		}
	}

	public void joinUser(User user) {
		synchronized (users) {
			if (users.add(user)) {
				notifyUserJoined(user);
			}
		}
	}

	/**
	 * Notifies about new user in room all current
	 * users expect the joining user.
	 */
	private void notifyUserJoined(User user) {
		synchronized (users) {
			
			User u;
			for (Iterator itor = users.iterator(); itor.hasNext(); ) {
				
				u = (User) itor.next();
				
				if (u == user) {
					continue;
				}
				
				u = (User) itor.next();
				u.notifyUserJoined(this, user);
			}
		}
	}
	
}
