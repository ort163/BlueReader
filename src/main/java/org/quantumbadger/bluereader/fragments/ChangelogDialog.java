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

package org.quantumbadger.bluereader.fragments;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import org.quantumbadger.bluereader.R;
import org.quantumbadger.bluereader.common.ChangelogManager;

public final class ChangelogDialog extends PropertiesDialog {

	public static ChangelogDialog newInstance() {
		return new ChangelogDialog();
	}

	@Override
	protected String getTitle(Context context) {
		return context.getString(R.string.title_changelog);
	}

	@Override
	protected void prepare(AppCompatActivity context, LinearLayout items) {
		ChangelogManager.generateViews(context, items, false);
	}
}
