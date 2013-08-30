package com.branchitup.app;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class AudioTest{
	public static void main(String[] args) throws IOException{
		File file = new File("/branchitup-env/diskresources/audio/0cd2e200-b7a9-4e92-b7da-deef81c38853.mp3");
		File img = new File("/home/meir/Music/children3.jpg");
		//CannotReadException wrong extension
		//InvalidAudioFrameException wrong binary audio
		
		AudioFile audioFile;
		try {
			audioFile = AudioFileIO.read(file);
			Tag tag = audioFile.getTag();
			AudioHeader header = audioFile.getAudioHeader();

			System.out.println("----> " + audioFile.getClass().getSimpleName() + ", " + audioFile);
			System.out.println("----> " + header.getTrackLength());
			System.out.println("----> " + header.getBitRate() + ", " + header.getBitRateAsNumber());
			System.out.println("----> " + header.getSampleRate());
			System.out.println("----> " + header.getChannels());
			System.out.println("----> " + header.getFormat()); //MPEG-1 Layer 3
			
//			Iterator<?> iterator = tag.getFields();
//			while(iterator.hasNext())
//			{
////			    String s = (String)iterator.next();
//			    System.out.println(iterator.next());
//			}
		} catch (CannotReadException | TagException | ReadOnlyFileException
				| InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}