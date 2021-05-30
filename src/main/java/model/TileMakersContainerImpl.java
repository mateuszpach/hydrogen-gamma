package model;

import vartiles.TileMaker;

import java.util.ArrayList;

public class TileMakersContainerImpl implements TileMakersContainer {

    private final ArrayList<TileMaker> tileMakers = new ArrayList<>();

    @Override
    public void addTileMaker(TileMaker maker) {
        tileMakers.add(maker);
    }

    @Override
    public ArrayList<TileMaker> getTileMakers() {
        return tileMakers;
    }
}
