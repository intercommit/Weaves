/*  Copyright 2014 InterCommIT b.v.
*
*  This file is part of the "Weaves" project hosted on https://github.com/intercommit/Weaves
*
*  Weaves is free software: you can redistribute it and/or modify
*  it under the terms of the GNU Lesser General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  any later version.
*
*  Weaves is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with Weaves.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package nl.intercommit.weaves.growler;

import nl.intercommit.weaves.growler.Message.LEVEL;

public class Message {

	public enum LEVEL {
		INFO,
		WARN,
		ERROR;
	}
	
	private final LEVEL _level;
	private final String _msg;
	
	/**
	 * Default is {@link LEVEL.INFO} level 
	 * 
	 */
	public Message(final String message) {
		this._level = LEVEL.INFO;
		this._msg = message;
	}
	
	
	public Message(final LEVEL level,final String message) {
		this._level = level;
		this._msg = message;
	}
	
	public String getMessage() {
		return _msg;
	}
	
	public LEVEL getLevel() {
		return _level;
	}
}
