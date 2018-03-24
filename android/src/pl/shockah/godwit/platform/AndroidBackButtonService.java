package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

public class AndroidBackButtonService extends BackButtonService {
	@Getter @Setter
	@Nullable private Delegate delegate;

	public AndroidBackButtonService() {
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}