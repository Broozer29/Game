package game.deprecated.pregeneratedlevels;

import java.util.ArrayList;
import java.util.List;

public class Album {

	private List<Level> albumLevels = new ArrayList<Level>();

	public Album(AlbumEnums albumEnum) {
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
		FuriWisdomOfRageLevel wisdomOfRage2 = new FuriWisdomOfRageLevel();
		this.albumLevels.add(wisdomOfRage2);
	}

	public List<Level> getLevels() {
		return this.albumLevels;
	}
}
