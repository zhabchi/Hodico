package Utilities;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ir.hodicohiff.R;

public class MenuAnimator {

	private Context context;
	private Map<Integer, ImageView> mMenu;

	public MenuAnimator(Map<Integer, ImageView> menu, Context context) {
		mMenu = menu;
		this.context = context;
	}

	public void setMenu(Map<Integer, ImageView> menu) {
		mMenu = menu;
	}

	public void animateMenu(int img) {

		final int nb = img;

		mMenu.get(img).setVisibility(View.VISIBLE);
		Animation myAnim = AnimationUtils
				.loadAnimation(context, R.anim.zoom_in);
		mMenu.get(img).startAnimation(myAnim);

		// then use onAnimationEnd
		myAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (nb + 1 < mMenu.size())
					animateMenu(nb + 1);
			}
		});

	}

	public void showMenu() {

		for (int i = 0; i < mMenu.size(); i++) {
			mMenu.get(i).setVisibility(View.VISIBLE);
		}

	}

}
