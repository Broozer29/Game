package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class BossHealthBar extends Sprite {

    private Sprite redBar;
    private Sprite blackBackgroundBar;
    private List<Sprite> drawableComponents = new ArrayList<>();
    private float originalRedBarWidth;
    private GameObject boss;

    public BossHealthBar(SpriteConfiguration spriteConfiguration, GameObject boss) {
        super(spriteConfiguration);
        initBars();
        this.boss = boss;
        this.drawableComponents.add(blackBackgroundBar);
        this.drawableComponents.add(redBar);
        this.drawableComponents.add(this);
        this.originalRedBarWidth = redBar.getWidth();
    }

    private void initBars() {
        SpriteConfiguration backgroundSpriteConfig = new SpriteConfiguration();
        backgroundSpriteConfig.setImageType(ImageEnums.BossHealthBarBackground);
        backgroundSpriteConfig.setScale(this.scale);
        backgroundSpriteConfig.setxCoordinate(this.xCoordinate);
        backgroundSpriteConfig.setyCoordinate(this.yCoordinate);
        blackBackgroundBar = new Sprite(backgroundSpriteConfig);

        //xOffset not needed due to the centering method
        blackBackgroundBar.addYOffset(Math.round(81 * this.scale));

        SpriteConfiguration redSpriteConfig = new SpriteConfiguration();
        redSpriteConfig.setImageType(ImageEnums.BossHealthBarRed);
        redSpriteConfig.setScale(this.scale);
        redSpriteConfig.setxCoordinate(this.xCoordinate);
        redSpriteConfig.setyCoordinate(this.yCoordinate);
        redBar = new Sprite(redSpriteConfig);

        //xOffset not needed due to the centering method
        redBar.addYOffset(Math.round(81 * this.scale));
    }

    public void updateHealthbarLength(){
        if(boss.getCurrentHitpoints() <= 0 || !boss.isVisible()){
            for(Sprite drawableComponent : this.drawableComponents){
                drawableComponent.setVisible(false);
            }
            return;
        }

        float factor = boss.getCurrentHitpoints() / boss.getMaxHitPoints();
        redBar.setImageDimensions(Math.round(originalRedBarWidth * factor), redBar.getHeight());
    }

    public List<Sprite> getDrawableComponents(){
        return this.drawableComponents;
    }

    public void centerHealthBar(){
        for(Sprite drawableComponent : this.drawableComponents){
            drawableComponent.setCenterXCoordinate((int) Math.round(DataClass.getInstance().getWindowWidth() * 0.5));
        }
    }
}
