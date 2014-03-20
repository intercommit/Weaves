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
package nl.intercommit.weaves.components.window;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.chenillekit.tapestry.core.components.Window;

/**
 * The chenillekit window extended with options
 * 
 * @tapestrydoc
 */
public class OptionsWindow extends Window {

	@Parameter(required=false)
	private JSONObject options;
	
	@Override
	protected void configure(JSONObject _options) {
		if (options != null) {
			// why is there no merge?
			for (String key: options.keys()) {
				_options.put(key, options.get(key));
			}
		}
	}
}
