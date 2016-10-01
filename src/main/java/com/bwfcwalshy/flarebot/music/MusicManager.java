package com.bwfcwalshy.flarebot.music;

import com.bwfcwalshy.flarebot.FlareBot;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {

    // Guild ID | Queue
    private Map<String, AudioPlayer> players;

    private FlareBot bot;
    public MusicManager(FlareBot flareBot){
        players = new HashMap<>();
        this.bot = flareBot;
    }

    public void addSong(String guildId, String musicFile){
        AudioPlayer player;
        if(players.containsKey(guildId))
            player = players.get(guildId);
        else player = AudioPlayer.getAudioPlayerForGuild(bot.getClient().getGuildByID(guildId));
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File("cached" + File.separator + musicFile));
            //FileProvider file = new FileProvider("cached" + File.separator + musicFile);
            AudioPlayer.Track track = new AudioPlayer.Track(input);
            // I need to remove 11 for the YouTube ID and 4 for the ".mp3" and finally the joiner "-" = 16
            track.getMetadata().put("name", musicFile.substring(0, musicFile.length()-16));
            //track.getMetadata().put("duration", duration);
            player.queue(track);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        players.put(guildId, player);
    }

    public void pause(String guildId){
        if(players.containsKey(guildId))
            players.get(guildId).setPaused(true);
    }

    public void play(String guildId){
        if(players.containsKey(guildId))
            players.get(guildId).setPaused(false);
    }

    public void skip(String guildId){
        if(players.containsKey(guildId))
            players.get(guildId).skip();
    }

    public void stop(String guildId){
        if(players.containsKey(guildId)) {
            players.get(guildId).setPaused(true);
            players.get(guildId).getPlaylist().clear();
        }
    }

    public void shuffle(String guildId){
        if(players.containsKey(guildId)) {
            players.get(guildId).shuffle();
        }
    }

    public void setVolume(String guildId, int i){
        if(i >= 0 && i <= 100){
            FlareBot.LOGGER.debug(String.valueOf((float) i /100));
            FlareBot.LOGGER.debug(String.valueOf(players.get(guildId).getVolume()));
            players.get(guildId).setVolume((float) i / 100);
        }
    }

    public Map<String, AudioPlayer> getPlayers() {
        return players;
    }
}