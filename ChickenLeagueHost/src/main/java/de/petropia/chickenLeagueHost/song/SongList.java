package de.petropia.chickenLeagueHost.song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import de.petropia.chickenLeagueHost.Constants;

public class SongList {
	
	private final File directory;
	private final List<File> nbsFiles = new ArrayList<>();
	private final List<Song> songs = new ArrayList<>();
	
	public static final SongList INSTANCE = new SongList(Constants.plugin.getDataFolder().getAbsolutePath() + "/Songs");
	
	/**
	 * Representation of all .nbs files in specified directory
	 * @param path Full path of songs directory
	 */
	public SongList(String path) {
		directory = new File(path);
		if(!checkIfDirectoryExist()) {
			createDirectory();
			return;
		}
		loadNbsFiles();
		loadSongsFromNbs();
	}
	
	/**
	 * Parse all .nbs files to Song object and put in list
	 */
	private void loadSongsFromNbs() {
		for(File file : nbsFiles) {
			songs.add(NBSDecoder.parse(file));
		}
	}
	
	/**
	 * Add every .nbs file to  the list nbsFiles if it ends with .nbs
	 */
	private void loadNbsFiles() {
		for(File file : directory.listFiles()) {
			if(!file.getName().endsWith(".nbs")) {
				continue;
			}
			nbsFiles.add(file);
		}
	}
	
	/**
	 * Checks if the where the songs are exists
	 * @return true when it exists
	 */
	private boolean checkIfDirectoryExist() {
		if(!directory.exists()) {
			return false;
		}
		if(!directory.isDirectory()) {
			return false;
		}
		return true;
	}
	
	private void createDirectory() {
		directory.mkdirs();
	}

	public List<Song> getSongs() {
		return songs;
	}
	
	public List<File> getNbsFiles(){
		return nbsFiles;
	}
	
	public SongList getInstance() {
		return INSTANCE;
	}
}
