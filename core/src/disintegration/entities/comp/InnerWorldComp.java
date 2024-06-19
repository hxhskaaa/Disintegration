package disintegration.entities.comp;

import arc.util.Tmp;
import disintegration.content.DTBlocks;
import disintegration.type.unit.WorldUnitType;
import ent.anno.Annotations.EntityComponent;
import ent.anno.Annotations.Import;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.gen.Building;
import mindustry.gen.Healthc;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import mindustry.world.Tiles;

@EntityComponent
abstract class InnerWorldComp implements Unitc, Healthc {
    @Import
    UnitType type;

    public World unitWorld, outerWorld;

    public int worldWidth, worldHeight;

    public void initWorld() {
        if (!(type instanceof WorldUnitType w)) return;
        unitWorld = new World();
        worldWidth = w.worldWidth;
        worldHeight = w.worldHeight;
        unitWorld.resize(worldWidth, worldHeight);
        unitWorld.tiles = new Tiles(worldWidth, worldHeight);
        unitWorld.tiles.fill();
        unitWorld.tiles.eachTile(t -> t.setFloor(DTBlocks.spaceStationFloor.asFloor()));
        unitWorld.tile(9, 9).setBlock(Blocks.duo);
        unitWorld.tile(0, 9).setBlock(Blocks.duo);
    }

    @Override
    public void update() {
        if (unitWorld == null) return;
        outerWorld = Vars.world;
        Vars.world = unitWorld;
        float cx = unitWorld.width() * Vars.tilesize / 2f, cy = unitWorld.height() * Vars.tilesize / 2f;
        unitWorld.tiles.eachTile(t -> {
            Building b = t.build;
            if (b != null) {
                b.set(Tmp.v1.set(t).sub(cx, cy).rotate(rotation()).add(this));
                b.updateTile();
            }
        });
        Vars.world = outerWorld;
    }
}
