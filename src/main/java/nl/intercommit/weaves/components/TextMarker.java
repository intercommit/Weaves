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
package nl.intercommit.weaves.components;

import java.util.LinkedList;
import java.util.List;

import nl.intercommit.weaves.base.BasicClientElement;

import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
/**
 * Marks specific keywords in a text with a yellow background.
 * 
 * @tapestrydoc
 */
@Import(stylesheet="TextMarker.css")
public class TextMarker extends BasicClientElement {

	private final static String POINTS = "...";
	
	@Parameter(required = true)
    private String value;
	
	@Parameter(required = true)
    private String[] keywords;
	
	@Parameter(value="20")
    private int paddingSize;
	
	@Parameter(value="0")
	private int maxLength;
	
	@Property
	private List<Match> matches;
	
	@Property
	private Match match;
	
	@BeginRender
	public void makeMatches() {
		
		final String[] lowerCaseKeywords= new String[keywords.length]; 
		int i =0;
		for (String keyword: keywords) {
			lowerCaseKeywords[i++] = keyword.toLowerCase();
		}
		
		
		String workingMessage = value;
		StringBuilder returnValue = null;
		int currentLength = 0 ;
		matches = new LinkedList<Match>();
		
		match = nextOccurence(workingMessage, lowerCaseKeywords); 
		while (match != null) {
			
			returnValue = new StringBuilder();
			if (currentLength == 0) {
				
				if (match.pos > paddingSize) {
					returnValue.append(POINTS);
					returnValue.append(workingMessage.substring(match.pos-paddingSize,match.pos));
				} else {
					returnValue.append(workingMessage.substring(0, match.pos));
				}
			} else {
				if (match.pos > paddingSize) {
					returnValue.append(workingMessage.substring(0,paddingSize)).append(POINTS);	
				} else {
					returnValue.append(workingMessage.substring(0, match.pos));
				}
			}
			match.prefix = returnValue.toString();
			currentLength += returnValue.length() + match.word.length();
			
			matches.add(match);
			
			workingMessage = workingMessage.substring(match.pos+ match.word.length());
			
			match = nextOccurence(workingMessage, lowerCaseKeywords);
			
			if (maxLength > 0 && currentLength >= maxLength) {
				match = null;
			}
		}
		// end
		
		match = new Match(0,null);
		
		if (!matches.isEmpty()) {
			int remainder = 0;
			if (maxLength > 0 ) {
				remainder = maxLength - currentLength;
				if (remainder <0) remainder= 0;
			} else {
				remainder = workingMessage.length();
			}
			if (remainder > paddingSize) {
				match.prefix = workingMessage.substring(0, paddingSize) + POINTS;
			} else {
				match.prefix = workingMessage.substring(0,remainder);
			}	
		} else {
			match.prefix = workingMessage;
		}
		matches.add(match);
	}
	
	/**
	 * finds a next occurence of a word from words in the searchText
	 * 
	 * @param searchText
	 * @param words
	 * @return null if nothing was found, a {@link Match} otherwise
	 */
	private Match nextOccurence(final String searchText,final String[] words) {
		Match nextMatch = null;
		
		final String lowerCaseSearchText = searchText.toLowerCase();
		for (String word:words) {
			if (lowerCaseSearchText.indexOf(word) != -1) {
				if (nextMatch == null) {
					final int pos = lowerCaseSearchText.indexOf(word);
					nextMatch = new Match(pos,searchText.substring(pos, pos+ word.length())); 
				} else {
					if (lowerCaseSearchText.indexOf(word) < nextMatch.pos) {
						final int pos = lowerCaseSearchText.indexOf(word);
						nextMatch = new Match(pos,searchText.substring(pos, pos+ word.length())); 
					}
				}
			}
		}
		return nextMatch;
	}

	public class Match {
		
		private int pos;
		private String prefix;
		private String word;
		
		public Match(final int pos,final String word) {
			this.pos = pos;
			this.word = word;
		}
		
		public int getPos() {
			return pos;
		}
		
		public String getWord() {
			return word;
		}
		
		public String getPrefix() {
			return prefix;
		}
		
	}
	
}