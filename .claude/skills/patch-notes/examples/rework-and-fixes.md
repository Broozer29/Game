Patch notes:
A small rework of Explosive Greed and Cash Infusion, and some bug fixes.

General:
- Contract:
    - The animation in the shop is slightly slower, making it easier to see which items the contract transformed into

Items:
- Explosive Greed (Generic legendary):
    - Old: Picking up a coin unleashes an explosion for 2500% (+2500%) damage
    - New: Picking up a coin unleashes an explosion dealing 100% (+100%) damage for every coin you have picked up since acquiring this item.
- Cash Infusion (Generic relic):
    - Old: Cash Carriers no longer spawn. Medium sized enemies have a chance of dropping coins worth 25 minerals
    - New: Cash Carriers no longer spawn. Critical Strikes have a 10% chance of spawning a coin upon dealing damage worth 15 minerals.
- One Shot One Kill (Captain relic):
    - Old: Your missiles that strike enemies with 100% hp will always critically strike and deal 250% damage.
    - New: Your missiles that strike enemies with 100% hp will always critically strike and have their damage increased by 250%.
        - It still does the same thing, but now the description accurately says the damage is increased by 250%, correctly indicating that Critical Strike bonuses still apply.

Bug fixes:
- Precision Amplifier:
    - No longer quadruples damage dealt for the Captain but doubles it, as intended. Oops.
- Infernal Pre-igniter:
    - The calculation behind the description has been fixed. It showed the wrong number in the item description, while the effect was applied correctly in practice:
        - Old description: While your Flamethrower is active, its damage exponentially increases with 0.3% per second.
        - New description: While your Flamethrower is active, its damage exponentially increases by 39.4% per second.
