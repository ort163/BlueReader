/*******************************************************************************
 * This file is part of BlueReader.
 *
 * BlueReader is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlueReader is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlueReader.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package org.quantumbadger.bluereader.reddit.prepared;

import org.apache.commons.lang3.StringEscapeUtils;
import org.quantumbadger.bluereader.reddit.prepared.markdown.MarkdownParagraphGroup;
import org.quantumbadger.bluereader.reddit.prepared.markdown.MarkdownParser;
import org.quantumbadger.bluereader.reddit.things.RedditComment;
import org.quantumbadger.bluereader.reddit.things.RedditThingWithIdAndType;

public class RedditParsedComment implements RedditThingWithIdAndType {

	private final RedditComment mSrc;

	private final MarkdownParagraphGroup mBody;

	private final String mFlair;

	public RedditParsedComment(final RedditComment comment) {

		mSrc = comment;

		mBody = MarkdownParser.parse(StringEscapeUtils.unescapeHtml4(comment.body).toCharArray());
		if(comment.author_flair_text != null) {
			mFlair = StringEscapeUtils.unescapeHtml4(comment.author_flair_text);
		} else {
			mFlair = null;
		}
	}

	public MarkdownParagraphGroup getBody() {
		return mBody;
	}

	public String getFlair() {
		return mFlair;
	}

	@Override
	public String getIdAlone() {
		return mSrc.getIdAlone();
	}

	@Override
	public String getIdAndType() {
		return mSrc.getIdAndType();
	}

	public RedditComment getRawComment() {
		return mSrc;
	}
}
