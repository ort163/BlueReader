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

package org.quantumbadger.bluereader.common;

import java.util.HashSet;
import java.util.LinkedList;

public class UniqueSynchronizedQueue<E> {

	private final HashSet<E> set = new HashSet<>();
	private final LinkedList<E> queue = new LinkedList<>();

	public synchronized void enqueue(E object) {
		if(set.add(object)) {
			queue.addLast(object);
		}
	}

	public synchronized E dequeue() {

		if(queue.isEmpty()) return null;

		final E result = queue.removeFirst();
		set.remove(result);
		return result;
	}
}
