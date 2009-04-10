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
 * Abstract response class.
 * 
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class AbstractResponse implements ChatPacket {

	/** Response state. */
	private boolean succeed;
	/** If failed this is set to specified reason */
	private byte failReason; 
	
	
	/**
	 * Creates a new response. The <code>succeed</code> tells what is the
	 * status of operation. When failed then the <code>failReason</code>
	 * should tell what went wrong.
	 * 
	 * @param succeed Status of operation. <code>true</code> means success.
	 * @param failReason If failed this will hold the reason.
	 */
	public AbstractResponse(boolean succeed, byte failReason) {
		this.succeed = succeed;
		this.failReason = failReason;
	}
	
	/**
	 * Provides the fail reason of requested operation. You should
	 * look at the documentation where to find fail definitions for
	 * your response class.
	 * <p>
	 * If {@link #isSucceed()} is true then return value of this
	 * method is undefined.
	 * 
	 * @return the fail reason of requested operation.
	 */
	public byte getFailReason() {
		return failReason;
	}
	
	/**
	 * Tells if the requested operation has succeed or failed.
	 * <p>
	 * In case of failure you probably want to know the reason.
	 * You can do so by reading the {@link #getFailReason()}.
	 * 
	 * @return the succeed state.
	 */
	public boolean isSucceed() {
		return succeed;
	}
	
	/*
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		succeed = in.readBoolean();
		failReason = in.readByte();
	}

	/*
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeBoolean(succeed);
		out.writeByte(failReason);
	}

}
