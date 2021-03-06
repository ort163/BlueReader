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

package org.quantumbadger.bluereader.views.glview.displaylist;


import org.quantumbadger.bluereader.common.MutableFloatPoint2D;
import org.quantumbadger.bluereader.views.glview.program.RRGLMatrixStack;

public class RRGLRenderableTranslation extends RRGLRenderableRenderHooks {

	private float mPositionX, mPositionY;

	public RRGLRenderableTranslation(final RRGLRenderable entity) {
		super(entity);

	}

	public void setPosition(float x, float y) {
		mPositionX = x;
		mPositionY = y;
	}

	@Override
	protected void preRender(final RRGLMatrixStack stack, final long time) {
		stack.pushAndTranslate(mPositionX, mPositionY);
	}

	@Override
	protected void postRender(final RRGLMatrixStack stack, final long time) {
		stack.pop();
	}

	public void setPosition(MutableFloatPoint2D mPositionOffset) {
		mPositionX = mPositionOffset.x;
		mPositionY = mPositionOffset.y;
	}
}
