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

package org.quantumbadger.bluereader.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.quantumbadger.bluereader.R;

/**
 * A view holder for a two line, text and icon list item.
 */
public class VH3TextIcon extends RecyclerView.ViewHolder {

	public final TextView text;
	public final TextView text2;
	public final TextView text3;
	public final ImageView icon;

	public long bindingId = 0;

	public VH3TextIcon(View itemView) {
		super(itemView);

		text = itemView.findViewById(R.id.recycler_item_text);
		text2 = itemView.findViewById(R.id.recycler_item_2_text);
		text3 = itemView.findViewById(R.id.recycler_item_3_text);
		icon = itemView.findViewById(R.id.recycler_item_icon);
	}
}
