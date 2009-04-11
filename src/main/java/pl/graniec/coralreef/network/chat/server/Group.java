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
import java.util.Set;

/**
 * Group is a group of users. It helps to send message to multiple
 * of chat users at once but not to everyone.
 * <p>
 * Every group has its name and this name is a group ID.
 * 
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class Group {
	/** The group ID */
	private final String name;
	/** Group members */
	private final Set members = new HashSet();
	
	public Group(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Adds a new member to the group.
	 * 
	 * @return <code>false</code> if this user is already
	 * a member of this group. 
	 */
	public boolean addMember(User u) {
		synchronized (members) {
			return members.add(u);
		}
	}
	
	/**
	 * Removes member from the group.
	 * 
	 * @return <code>false</code> if this user isn't a member
	 * of this group.
	 */
	public boolean removeMember(User u) {
		synchronized (members) {
			return members.remove(u);
		}
	}
	
	/**
	 * @return Members count.
	 */
	public int size() {
		return members.size();
	}
	
	/**
	 * @return Array of all members of this group.
	 */
	public User[] getMembers() {
		synchronized (members) {
			return (User[]) members.toArray(new User[members.size()]);
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
