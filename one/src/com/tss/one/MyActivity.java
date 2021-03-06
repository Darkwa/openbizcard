package com.tss.one;

import com.tssoft.one.webservice.ImageLoaderFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageButton;

public class MyActivity extends Activity {

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	protected void buildMenu(Activity act) {
		final Activity myAct = act;
		ImageButton icon0 = (ImageButton) act.findViewById(R.id.main_button);
		ImageButton icon1 = (ImageButton) act
				.findViewById(R.id.my_teams_button);
		ImageButton icon2 = (ImageButton) act.findViewById(R.id.news_button);
		ImageButton icon3 = (ImageButton) act
				.findViewById(R.id.score_board_button);

		icon0.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (MainList.isShowDetail) {
					myAct.finish();
					MainList.isShowDetail = false;
					return;
				}
				myAct.finish();
				Intent mainListIntent = new Intent(view.getContext(),
						MainList.class);
				// mainDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(mainListIntent);

			}
		});

		icon1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (MainList.isShowDetail) {
					MainList.isShowDetail = false;
				}
				myAct.finish();
				Intent myTeamsTabIntent = new Intent(view.getContext(),
						MyTeamsTab.class);
				// mainDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(myTeamsTabIntent);
			}
		});

		icon2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (MainList.isShowDetail) {
					MainList.isShowDetail = false;
				}
				myAct.finish();
				Intent newsListIntent = new Intent(view.getContext(),
						NewsList.class);
				// newsListIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(newsListIntent);
			}
		});
		icon3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (MainList.isShowDetail) {
					MainList.isShowDetail = false;
				}
				myAct.finish();
				Intent scoreBoardIntent = new Intent(view.getContext(),
						ScoreBoard.class);
				// newsListIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(scoreBoardIntent);
			}
		});
	}

}
