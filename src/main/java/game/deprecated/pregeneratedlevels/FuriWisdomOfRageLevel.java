package game.deprecated.pregeneratedlevels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.movement.Direction;
import game.objects.enemies.enums.EnemyEnums;
import game.spawner.EnemyFormation;
import game.objects.powerups.timers.DeprecatedEnemySpawnTimer;
import game.spawner.FormationCreator;
import game.spawner.enums.SpawnFormationEnums;
import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.enums.AudioEnums;

public class FuriWisdomOfRageLevel implements Level {

	private AudioEnums song = AudioEnums.Furi_Wisdowm_Of_Rage;
	private DataClass dataClass = DataClass.getInstance();
	private List<DeprecatedEnemySpawnTimer> enemySpawnTimers = new ArrayList<DeprecatedEnemySpawnTimer>();
	private Random random = new Random();

	public FuriWisdomOfRageLevel() {
		initLevel();
	}

	private void initLevel() {
		initRepeatableTimers();
		initSingleFireTimers();
	}

//	Wisdom of Rage:
//
//		Random spawns alleen hier:
//		0:00 tot 0:40 is hetzelfde. Start van het level, start met basic enemies
//		0:40 tot 0:58 is eerste verandering, kleine decrease in tempo, introduceer iets nieuws
//		0:58 tot 1:15 first good shot
//		1:15 tot 1:30 zelfde als hiervoor maar 1 deuntje meer
//		1:30 tot 2:13 einde eerste fase. Nu enemies spawnen in formatie
//
//		2:13 tot 2:30, langzaam
//		2:30 tot 2:44 langzaam +1
//		2:44 tot 3:00 laat ze van boven en onder komen, medium tempo
//		3:00 tot 3:30, laat ze van alle kanten komen, medium
//		3:30 tot 3:55 intermissie, langzaam van rechts alleen
//		3:55 tot 4:10, intermissie over, medium tempo (rechts, midden en boven)
//		4:10 tot 4:40 hoog tempo alle kanten
//		4:40 tot 5:06 afbouwen, het is over
//

	@Override
	public void initSingleFireTimers() {
		FormationCreator formCreator = new FormationCreator();
		int additionalDelay = 0;
		boolean loopable = false;
		float scale = 1;
		DeprecatedEnemySpawnTimer timer = null;
		EnemyFormation formation = null;
		int xMovementSpeed = 2;
		int yMovementSpeed = 2;

		// Enemytype, spawnAmount, Time before activation, looping, direction, scale,
		// additional delay
//		0:00 tot 0:40 is hetzelfde. Start van het level, start met basic enemies
		addSpawnTimer(createSpawnTimer(EnemyEnums.Bomba, 1, 3, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed));



		for (int i = 2; i < 41; i += 2) {
			addSpawnTimer(
					createSpawnTimer(EnemyEnums.getRandomEnemy(), 4, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed));
		}

//		create22SecondsFormation(timer, formation, formCreator);
//		create51SecondsFormation(timer, formation, formCreator);
//		create71SecondsFormation(timer, formation, formCreator);
//		create130SecondsFormations(timer, formation, formCreator);
//		create160SecondsFormations(timer, formation, formCreator);
//		createFinalLoodjes(timer, formation, formCreator);
		
		
//
////		0:40 tot 0:58 is eerste verandering, kleine decrease in tempo, introduceer iets nieuws
//		addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 38, loopable, Direction.DOWN, scale,
//				additionalDelay));
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 38, loopable, Direction.UP, scale, additionalDelay));
//
//		for (int i = 41; i < 56; i += 2) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 4, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
////		0:58 tot 1:15 first good shot
//		addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 56, loopable, Direction.DOWN, scale,
//				additionalDelay));
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 56, loopable, Direction.UP, scale, additionalDelay));
//
//		for (int i = 57; i < 75; i += 2) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 4, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
////		1:15 tot 1:30 zelfde als hiervoor maar 1 deuntje meer
//		for (int i = 75; i < 90; i += 2) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 6, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
////		1:30 tot 2:13 einde eerste fase. Nu enemies spawnen in formatie
//		for (int i = 86; i < 133; i += 2) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 7, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
//		addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 90, loopable, Direction.DOWN, scale,
//				additionalDelay));
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 90, loopable, Direction.UP, scale, additionalDelay));
//
////		2:13 tot 2:30, langzaam
//		for (int i = 138; i < 150; i += 2) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 4, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 138, loopable, Direction.LEFT, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 40, 40);
//		timer.setFormation(formation, dataClass.getWindowWidth(), 250);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 142, loopable, Direction.LEFT, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 40, 40);
//		timer.setFormation(formation, dataClass.getWindowWidth(), 100);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 146, loopable, Direction.LEFT, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 40, 40);
//		timer.setFormation(formation, dataClass.getWindowWidth(), 450);
//		addSpawnTimer(timer);
//
////		2:30 tot 2:44 langzaam +1
//		for (int i = 150; i < 164; i += 2) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 3, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
////		2:44 tot 3:00 laat ze van boven en onder komen, medium tempo
//		for (int i = 164; i < 180; i += 2) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 3, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Seeker, 10, 164, loopable, Direction.DOWN, scale, additionalDelay));
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Seeker, 10, 164, loopable, Direction.UP, scale, additionalDelay));
//
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Seeker, 10, 172, loopable, Direction.UP, scale, additionalDelay));
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Seeker, 10, 172, loopable, Direction.DOWN, scale, additionalDelay));
//
////		3:00 tot 3:30, laat ze van alle kanten komen, medium
//		addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 190, loopable, Direction.DOWN, scale,
//				additionalDelay));
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 190, loopable, Direction.UP, scale, additionalDelay));
//
//		addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 200, loopable, Direction.DOWN, scale,
//				additionalDelay));
//		addSpawnTimer(
//				createRandomSpawnTimer(EnemyEnums.Alien_Bomb, 50, 200, loopable, Direction.UP, scale, additionalDelay));
//
//		for (int i = 180; i < 210; i += 4) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 3, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
//		for (int i = 184; i < 210; i += 4) {
//			addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Random, 3, i, loopable, Direction.LEFT_DOWN, scale,
//					additionalDelay));
//		}
//
//		for (int i = 184; i < 210; i += 4) {
//			addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Random, 3, i, loopable, Direction.LEFT_UP, scale,
//					additionalDelay));
//		}
//
//		for (int i = 180; i < 210; i += 4) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 3, i, loopable, Direction.RIGHT, scale, additionalDelay));
//		}
//
//		for (int i = 184; i < 210; i += 4) {
//			addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Random, 3, i, loopable, Direction.RIGHT_UP, scale,
//					additionalDelay));
//		}
//
//		for (int i = 184; i < 210; i += 4) {
//			addSpawnTimer(createRandomSpawnTimer(EnemyEnums.Random, 3, i, loopable, Direction.RIGHT_DOWN, scale,
//					additionalDelay));
//		}
//
////		3:30 tot 3:55 intermissie, langzaam van rechts alleen
//		for (int i = 212; i < 235; i += 3) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 4, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
////		3:55 tot 4:10, intermissie over, medium tempo (rechts, midden en boven)
//		for (int i = 235; i < 250; i += 4) {
//			addSpawnTimer(
//					createRandomSpawnTimer(EnemyEnums.Random, 4, i, loopable, Direction.LEFT, scale, additionalDelay));
//		}
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 240, loopable, Direction.LEFT, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 40, 40);
//		timer.setFormation(formation, dataClass.getWindowWidth(), 300);
//		addSpawnTimer(timer);
////		4:10 tot 4:40 hoog tempo alle kanten
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 252, loopable, Direction.RIGHT, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 70, 70);
//		timer.setFormation(formation, -500, (dataClass.getWindowHeight() / 2) - 300);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 256, loopable, Direction.RIGHT_UP, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, -400, dataClass.getWindowHeight() + 50);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 256, loopable, Direction.LEFT_DOWN, scale,
//				additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth(), -150);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 262, loopable, Direction.RIGHT_DOWN, scale,
//				additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, -300, -250);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 262, loopable, Direction.LEFT_UP, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, dataClass.getWindowHeight() + 100);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Flamer, 1, 270, loopable, Direction.RIGHT, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Square, 60, 60);
//		timer.setFormation(formation, -250, (dataClass.getWindowHeight() / 2) - 150);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Bomba, 1, 280, loopable, Direction.LEFT, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Cross, 100, 100);
//		timer.setFormation(formation, dataClass.getWindowWidth(), (dataClass.getWindowHeight() / 2) - 150);
//		addSpawnTimer(timer);
//
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 290, loopable, Direction.RIGHT_UP, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, -400, dataClass.getWindowHeight() + 100);
//
//		addSpawnTimer(timer);
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 290, loopable, Direction.LEFT_DOWN, scale,
//				additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, -250);
//
//		addSpawnTimer(timer);
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 290, loopable, Direction.RIGHT_DOWN, scale,
//				additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, -400, -250);
//
//		addSpawnTimer(timer);
//		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 290, loopable, Direction.LEFT_UP, scale, additionalDelay);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, dataClass.getWindowHeight() + 100);
////		

//		4:40 tot 5:06 afbouwen, het is over
		for (int i = 280; i < 306; i += 2) {
			addSpawnTimer(
					createSpawnTimer(EnemyEnums.getRandomEnemy(), 4, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed));
		}
	}

	@Override
	public void initRepeatableTimers() {
		// TODO Auto-generated method stub

	}

	private void addSpawnTimer(DeprecatedEnemySpawnTimer timer) {
		enemySpawnTimers.add(timer);
	}

	private DeprecatedEnemySpawnTimer createSpawnTimer(EnemyEnums enemyType, int spawnAttempts, int timeBeforeActivation,
													   boolean loopable, Direction direction, float enemyScale, int additionalDelay, int xMovementSpeed, int yMovementSpeed) {

		if (enemyType == null) {
			enemyType = selectRandomEnemy();
		}

		DeprecatedEnemySpawnTimer timer = new DeprecatedEnemySpawnTimer(timeBeforeActivation, spawnAttempts, enemyType, loopable, direction,
				enemyScale, xMovementSpeed, yMovementSpeed);
		return timer;
	}

	public AudioEnums getSong() {
		return song;
	}

	public List<DeprecatedEnemySpawnTimer> getTimers() {
		return this.enemySpawnTimers;
	}

	private EnemyEnums selectRandomEnemy() {
		EnemyEnums[] enums = EnemyEnums.values();
		EnemyEnums randomValue = enums[random.nextInt(enums.length)];

		if (randomValue == EnemyEnums.Alien_Bomb) {
			return selectRandomEnemy();
		}
		return randomValue;
	}

//	private void create22SecondsFormation(DeprecatedEnemySpawnTimer timer, EnemyFormation formation,
//										  FormationCreator formCreator) {
//		boolean loopable = false;
//		float scale = 1;
//		int additionalDelay = 0;
//		int xMovementSpeed = 2;
//		int yMovementSpeed = 1;
//
//		timer = createSpawnTimer(EnemyEnums.Seeker, 22, 22, loopable, Direction.RIGHT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, -400, dataClass.getWindowHeight() + 100);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Seeker, 22, 22, loopable, Direction.LEFT_DOWN, scale,
//				additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, -250);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Seeker, 22, 22, loopable, Direction.RIGHT_DOWN, scale,
//				additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, -400, -250);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Seeker, 22, 22, loopable, Direction.LEFT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, dataClass.getWindowHeight() + 100);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Flamer, 22, 29, loopable, Direction.RIGHT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, -400, dataClass.getWindowHeight() + 100);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Flamer, 22, 29, loopable, Direction.LEFT_DOWN, scale,
//				additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, -250);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Flamer, 22, 29, loopable, Direction.RIGHT_DOWN, scale,
//				additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, -400, -250);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Flamer, 22, 29, loopable, Direction.LEFT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, dataClass.getWindowHeight() + 100);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Tazer, 22, 33, loopable, Direction.RIGHT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, -400, dataClass.getWindowHeight() + 100);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Tazer, 22, 33, loopable, Direction.LEFT_DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, -250);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Tazer, 22, 33, loopable, Direction.RIGHT_DOWN, scale,
//				additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, -400, -250);
//		addSpawnTimer(timer);
//
//		timer = createSpawnTimer(EnemyEnums.Tazer, 22, 33, loopable, Direction.LEFT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 200, dataClass.getWindowHeight() + 100);
//		addSpawnTimer(timer);
//	}
//
//	private void create51SecondsFormation(DeprecatedEnemySpawnTimer timer, EnemyFormation formation,
//										  FormationCreator formCreator) {
//		boolean loopable = false;
//		float scale = 1;
//		int additionalDelay = 0;
//		int xMovementSpeed = 2;
//		int yMovementSpeed = 1;
//
//
//		for(int i = 47; i < 75; i +=5) {
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 100);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT_DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 400);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 100);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT_DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 400);
//			addSpawnTimer(timer);
//		}
//
//	}
//
//	private void create71SecondsFormation(DeprecatedEnemySpawnTimer timer, EnemyFormation formation,
//										  FormationCreator formCreator) {
//		boolean loopable = false;
//		float scale = 1;
//		int additionalDelay = 0;
//		int xMovementSpeed = 2;
//		int yMovementSpeed = 1;
//
//		for(int i = 90; i < 130; i +=7) {
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 100);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT_DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 400);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 100);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT_DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 400);
//			addSpawnTimer(timer);
//		}
//
//		for(int i = 73; i < 130; i +=8) {
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.V, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, 0);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Bomba, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 520, 0);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() - 210);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Bomba, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 520, dataClass.getWindowHeight() - 120);
//			addSpawnTimer(timer);
//		}
//
//		for(int i = 105; i < 130; i +=5) {
//			timer = createSpawnTimer(EnemyEnums.Flamer, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 150);
//			addSpawnTimer(timer);
//		}
//	}
//
//	private void create130SecondsFormations(DeprecatedEnemySpawnTimer timer, EnemyFormation formation,
//											FormationCreator formCreator) {
//		boolean loopable = false;
//		float scale = 1;
//		int additionalDelay = 0;
//
//		int xMovementSpeed = 2;
//		int yMovementSpeed = 1;
//
//		for(int i = 130; i < 165; i +=8) {
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, 0);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() - 210);
//			addSpawnTimer(timer);
//		}
//	}
//
//
//	private void create160SecondsFormations(DeprecatedEnemySpawnTimer timer, EnemyFormation formation,
//											FormationCreator formCreator) {
//
//		boolean loopable = false;
//		float scale = 1;
//		int additionalDelay = 0;
//		int xMovementSpeed = 2;
//		int yMovementSpeed = 1;
//
//		for (int i = 168; i < 220; i += 8) {
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.V, 50, 50);
//			timer.setFormation(formation, 0, -200);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i , loopable, Direction.DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.V, 50, 50);
//			timer.setFormation(formation, 550, -200);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i , loopable, Direction.DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.V, 50, 50);
//			timer.setFormation(formation, 1100, -200);
//			addSpawnTimer(timer);
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, 50, 50);
//			timer.setFormation(formation, 0, dataClass.getWindowHeight());
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, 50, 50);
//			timer.setFormation(formation, 550, dataClass.getWindowHeight());
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, 50, 50);
//			timer.setFormation(formation, 1100, dataClass.getWindowHeight());
//			addSpawnTimer(timer);
//		}
//
//		for (int i = 180; i < 220; i += 8) {
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, 50, 50);
//			timer.setFormation(formation, -300, 0);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, 50, 50);
//			timer.setFormation(formation, -300, 550);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 100, 0);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 100, 550);
//			addSpawnTimer(timer);
//
//		}
//
//		for (int i = 193; i < 220; i += 8) {
//			timer = createSpawnTimer(EnemyEnums.Bomba, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 100, dataClass.getWindowHeight() / 2 - 50);
//			addSpawnTimer(timer);
//		}
//	}
//
//	private void createFinalLoodjes(DeprecatedEnemySpawnTimer timer, EnemyFormation formation,
//									FormationCreator formCreator) {
//		boolean loopable = false;
//		float scale = 1;
//		int additionalDelay = 0;
//		int xMovementSpeed = 2;
//		int yMovementSpeed = 1;
//
//
//		for (int i = 225; i < 276; i += 8) {
//			timer = createSpawnTimer(EnemyEnums.Tazer, 1, i, loopable, Direction.RIGHT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, 50, 50);
//			timer.setFormation(formation, -300, 0);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Tazer, 1, i, loopable, Direction.RIGHT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, 50, 50);
//			timer.setFormation(formation, -300, 620);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Tazer, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 100, 0);
//			addSpawnTimer(timer);
//			timer = createSpawnTimer(EnemyEnums.Tazer, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 100, 620);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Bomba, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 100, dataClass.getWindowHeight() / 2 - 50);
//			addSpawnTimer(timer);
//		}
//
//		for (int i = 244; i < 276; i += 8) {
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.V, 50, 50);
//			timer.setFormation(formation, 0, -300);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.V, 50, 50);
//			timer.setFormation(formation, 550, -300);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.V, 50, 50);
//			timer.setFormation(formation, 1100, -300);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, 50, 50);
//			timer.setFormation(formation, 0, dataClass.getWindowHeight());
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, 50, 50);
//			timer.setFormation(formation, 550, dataClass.getWindowHeight());
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, 50, 50);
//			timer.setFormation(formation, 1100, dataClass.getWindowHeight());
//			addSpawnTimer(timer);
//		}
//
//		for (int i = 280; i < 300; i += 5) {
//			timer = createSpawnTimer(EnemyEnums.Flamer, 1, i, loopable, Direction.LEFT_UP, scale,
//					additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth(), dataClass.getWindowHeight());
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Flamer, 1, i, loopable, Direction.LEFT_DOWN, scale,
//					additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Divide, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth(), -350);
//			addSpawnTimer(timer);
//
//			timer = createSpawnTimer(EnemyEnums.Bomba, 1, i, loopable, Direction.LEFT, scale,
//					additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Cross, 80, 80);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 100, dataClass.getWindowHeight() / 2 - 200);
//			addSpawnTimer(timer);
//		}
//	}
}
