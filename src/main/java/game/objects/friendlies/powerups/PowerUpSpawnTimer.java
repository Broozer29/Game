package game.objects.friendlies.powerups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.managers.PowerUpManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.objects.enemies.EnemyEnums;

public class PowerUpSpawnTimer implements ActionListener {

	PowerUpManager powerUpManager = PowerUpManager.getInstance();
	
	private Timer timer;
	private int timeBeforeActivation;
	private PowerUps powerUpType;
	
	public PowerUpSpawnTimer(int timeBeforeActivation, PowerUps powerUpType) {
		this.timeBeforeActivation = timeBeforeActivation;
		this.powerUpType = powerUpType;
		initTimer();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		powerUpManager = PowerUpManager.getInstance();
		powerUpManager.spawnPowerUp(powerUpType);
		powerUpManager.createPowerUpTimer();
		timer.stop();
	}
	

	private void initTimer() {
		this.timer = new Timer(timeBeforeActivation, this);
		timer.start();
	}
}