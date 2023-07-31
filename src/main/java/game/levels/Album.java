package game.levels;

import java.util.ArrayList;
import java.util.List;

public class Album {

	private List<Level> albumLevels = new ArrayList<Level>();

	public void album(AlbumEnums albumEnum) {
		this.saturateAlbum(albumEnum);
	}

	private void saturateAlbum(AlbumEnums albumEnum) {
		switch (albumEnum) {
		case Furi:
			saturateFuriAlbum();
			break;
		default:
			break;
		}
	}

	private void saturateFuriAlbum() {
		FuriWisdomOfRageLevel wisdomOfRage = new FuriWisdomOfRageLevel();
		this.albumLevels.add(wisdomOfRage);
	}

	public List<Level> getLevels() {
		return this.albumLevels;
	}
}
