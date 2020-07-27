
package com.rpbee.Save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.rpbee.Sprites.Anthon;

public class GameManager {
  private static GameManager ourInstance = new GameManager();

  public GameData gameData;
  private Json json = new Json();
  private FileHandle fileHandle = Gdx.files.local("bin/GameData.json");
  public GameManager() {}

    public void initializeGameData() {
        if (!fileHandle.exists()) {
            //gameData = new GameData();
            
            //gameData.setLife(Anthon.getHealth());
            //gameData.setMusicOn(false);
            //saveData();
        } else {
            loadData();
        }
    }

    public void saveData(GameData gameData) {
        if (gameData != null) {
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)),false);
            //fileHandle.writeString(json.prettyPrint(gameData),false);
        }
    }

    public GameData loadData() {
        return gameData = json.fromJson(GameData.class,Base64Coder.decodeString(fileHandle.readString()));
        //return gameData = json.fromJson(GameData.class,fileHandle.readString());
    }
    
    public static GameManager getInstance() {
        return ourInstance;
    }
}
