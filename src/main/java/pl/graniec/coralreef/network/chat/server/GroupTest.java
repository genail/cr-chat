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

import junit.framework.TestCase;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class GroupTest extends TestCase {

	final Group group = new Group("group");
	final User user1 = new User("jack");
	final User user2 = new User("john");
	
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link pl.graniec.coralreef.network.chat.server.Group#addMember(pl.graniec.coralreef.network.chat.server.User)}.
	 */
	public void testAddMember() {
		assertTrue(group.addMember(user1));
		assertFalse(group.addMember(user1));
		assertTrue(group.addMember(user2));
		assertFalse(group.addMember(user2));
	}

	/**
	 * Test method for {@link pl.graniec.coralreef.network.chat.server.Group#removeMember(pl.graniec.coralreef.network.chat.server.User)}.
	 */
	public void testRemoveMember() {
		
		assertFalse(group.removeMember(user1));
		
		group.addMember(user1);
		group.addMember(user2);
		
		assertTrue(group.removeMember(user1));
		assertFalse(group.removeMember(user1));
		assertTrue(group.removeMember(user2));
	}

	/**
	 * Test method for {@link pl.graniec.coralreef.network.chat.server.Group#getMembers()}.
	 */
	public void testGetMembers() {
		assertEquals(0, group.getMembers().length);
		
		group.addMember(user1);
		assertEquals(user1, group.getMembers()[0]);
	}

}
