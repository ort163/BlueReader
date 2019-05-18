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

package org.quantumbadger.bluereader.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.quantumbadger.bluereader.R;
import org.quantumbadger.bluereader.account.RedditAccountChangeListener;
import org.quantumbadger.bluereader.account.RedditAccountManager;
import org.quantumbadger.bluereader.common.DialogUtils;
import org.quantumbadger.bluereader.common.General;
import org.quantumbadger.bluereader.common.LinkHandler;
import org.quantumbadger.bluereader.common.PrefsUtility;
import org.quantumbadger.bluereader.fragments.CommentListingFragment;
import org.quantumbadger.bluereader.fragments.SessionListDialog;
import org.quantumbadger.bluereader.listingcontrollers.CommentListingController;
import org.quantumbadger.bluereader.reddit.prepared.RedditPreparedPost;
import org.quantumbadger.bluereader.reddit.url.PostCommentListingURL;
import org.quantumbadger.bluereader.reddit.url.RedditURLParser;
import org.quantumbadger.bluereader.reddit.url.UserCommentListingURL;
import org.quantumbadger.bluereader.views.RedditPostView;

import java.util.UUID;

public class CommentListingActivity extends RefreshableActivity
		implements RedditAccountChangeListener,
		OptionsMenuUtility.OptionsMenuCommentsListener,
		RedditPostView.PostSelectionListener,
		SessionChangeListener {

	private static final String TAG = "CommentListingActivity";

	public static final String EXTRA_SEARCH_STRING = "cla_search_string";

	private static final String SAVEDSTATE_SESSION = "cla_session";
	private static final String SAVEDSTATE_SORT = "cla_sort";
	private static final String SAVEDSTATE_FRAGMENT = "cla_fragment";

	private CommentListingController controller;

	private CommentListingFragment mFragment;

	public void onCreate(final Bundle savedInstanceState) {

		PrefsUtility.applyTheme(this);

		super.onCreate(savedInstanceState);

		setTitle(getString(R.string.app_name));

		RedditAccountManager.getInstance(this).addUpdateListener(this);

		if(getIntent() != null) {

			final Intent intent = getIntent();

			final String url = intent.getDataString();
			final String searchString = intent.getStringExtra(EXTRA_SEARCH_STRING);
			controller = new CommentListingController(RedditURLParser.parseProbableCommentListing(Uri.parse(url)), this);
			controller.setSearchString(searchString);

			Bundle fragmentSavedInstanceState = null;

			if(savedInstanceState != null) {

				if(savedInstanceState.containsKey(SAVEDSTATE_SESSION)) {
					controller.setSession(UUID.fromString(savedInstanceState.getString(SAVEDSTATE_SESSION)));
				}

				if(savedInstanceState.containsKey(SAVEDSTATE_SORT)) {
					controller.setSort(PostCommentListingURL.Sort.valueOf(
							savedInstanceState.getString(SAVEDSTATE_SORT)));
				}

				if(savedInstanceState.containsKey(SAVEDSTATE_FRAGMENT)) {
					fragmentSavedInstanceState = savedInstanceState.getBundle(SAVEDSTATE_FRAGMENT);
				}
			}

			doRefresh(RefreshableFragment.COMMENTS, false, fragmentSavedInstanceState);

		} else {
			throw new RuntimeException("Nothing to show!");
		}
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);

		final UUID session = controller.getSession();
		if(session != null) {
			outState.putString(SAVEDSTATE_SESSION, session.toString());
		}

		final PostCommentListingURL.Sort sort = controller.getSort();
		if(sort != null) {
			outState.putString(SAVEDSTATE_SORT, sort.name());
		}

		if(mFragment != null) {
			outState.putBundle(SAVEDSTATE_FRAGMENT, mFragment.onSaveInstanceState());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		OptionsMenuUtility.prepare(
				this,
				menu,
				false,
				false,
				true,
				false,
				false,
				controller.isUserCommentListing(),
				false,
				controller.isSortable(),
				false,
				null,
				false,
				true,
				null,
				null);

		if(mFragment != null) {
			mFragment.onCreateOptionsMenu(menu);
		}

		return true;
	}

	public void onRedditAccountChanged() {
		requestRefresh(RefreshableFragment.ALL, false);
	}

	@Override
	protected void doRefresh(final RefreshableFragment which, final boolean force, final Bundle savedInstanceState) {
		mFragment = controller.get(this, force, savedInstanceState);

		final View view = mFragment.getView();
		setBaseActivityContentView(view);
		General.setLayoutMatchParent(view);

		setTitle(controller.getCommentListingUrl().humanReadableName(this, false));
		invalidateOptionsMenu();
	}

	public void onRefreshComments() {
		controller.setSession(null);
		requestRefresh(RefreshableFragment.COMMENTS, true);
	}

	public void onPastComments() {
		final SessionListDialog sessionListDialog = SessionListDialog.newInstance(controller.getUri(), controller.getSession(), SessionChangeListener.SessionChangeType.COMMENTS);
		sessionListDialog.show(getSupportFragmentManager(), null);
	}

	public void onSortSelected(final PostCommentListingURL.Sort order) {
		controller.setSort(order);
		requestRefresh(RefreshableFragment.COMMENTS, false);
	}

	public void onSortSelected(final UserCommentListingURL.Sort order) {
		controller.setSort(order);
		requestRefresh(RefreshableFragment.COMMENTS, false);
	}

	@Override
	public void onSearchComments() {
		DialogUtils.showSearchDialog(this, new DialogUtils.OnSearchListener() {
			@Override
			public void onSearch(@Nullable String query) {
				Intent searchIntent = getIntent();
				searchIntent.putExtra(EXTRA_SEARCH_STRING, query);
				startActivity(searchIntent);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		if(mFragment != null) {
			if(mFragment.onOptionsItemSelected(item)) {
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	public void onSessionRefreshSelected(SessionChangeType type) {
		onRefreshComments();
	}

	public void onSessionSelected(UUID session, SessionChangeType type) {
		controller.setSession(session);
		requestRefresh(RefreshableFragment.COMMENTS, false);
	}

	public void onSessionChanged(UUID session, SessionChangeType type, long timestamp) {
		Log.i(TAG, type.name() + " session changed to " + (session != null ? session.toString() : "<null>"));
		controller.setSession(session);
	}

	public void onPostSelected(final RedditPreparedPost post) {
		LinkHandler.onLinkClicked(this, post.src.getUrl(), false, post.src.getSrc());
	}

	public void onPostCommentsSelected(final RedditPreparedPost post) {
		LinkHandler.onLinkClicked(this, PostCommentListingURL.forPostId(post.src.getIdAlone()).toString(), false);
	}

	@Override
	public void onBackPressed() {
		if(General.onBackPressed()) super.onBackPressed();
	}
}
