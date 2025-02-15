package net.ncplanner.plannerator.planner.api;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.file.FileReader;
import net.ncplanner.plannerator.planner.file.FileWriter;
import net.ncplanner.plannerator.planner.file.FormatWriter;
import net.ncplanner.plannerator.planner.file.InputStreamProvider;
import net.ncplanner.plannerator.planner.file.NCPFFile;
import simplelibrary.image.Image;
public class HeadlessAPI{
    /**
     * Sets the global configuration. This is used when loading from hellrage formats. See multiblock.configuration.Configuration.configurations for a list of loaded configurations
     * @param configuration 
     */
    public static void setConfiguration(Configuration configuration){
        Core.configuration = configuration;
    }
    /**
     * Loads a file into an NCPFFile object
     * @param file the file to load
     * @throws IllegalArgumentException if the file is of an unknown format
     * @return an NCPFFile object containing all the information in the file
     */
    public static NCPFFile loadFile(File file)throws IllegalArgumentException{
        return FileReader.read(file);
    }
    /**
     * Loads a file from a stream into an NCPFFile object
     * @param input the InputStreamProvider to load from
     * @throws IllegalArgumentException if the file is of an unknown format
     * @return an NCPFFile object containing all the information in the file
     */
    public static NCPFFile loadFile(InputStreamProvider input)throws IllegalArgumentException{
        return FileReader.read(input);
    }
    /**
     * Saves a file to any supported format
     * @param ncpf the file to save
     * @param format the format to convert to- see static fields in planner.file.FileWriter
     * @param output the OutputStream to write the converted file to
     */
    public static void saveFile(NCPFFile ncpf, FormatWriter format, OutputStream output){
        format.write(ncpf, output);
    }
    /**
     * Converts a file to any supported format
     * @param file the file to convert
     * @param format the format to convert to- see static fields in planner.file.FileWriter
     * @param output the OutputStream to write the converted file to
     */
    public static void convertFile(File file, FormatWriter format, OutputStream output){
        saveFile(loadFile(file), format, output);
    }
    /**
     * Converts a file from a stream to any supported format
     * @param input the file to convert from an InputStreamProvider
     * @param format the format to convert to- see static fields in planner.file.FileWriter
     * @param output the OutputStream to write the converted file to
     */
    public static void convertFile(InputStreamProvider input, FormatWriter format, OutputStream output){
        format.write(loadFile(input), output);
    }
    /**
     * Creates an image for the first contained multiblock in an NCPFFile object
     * @param ncpf the NCPFFile to save
     * @return the first multiblock's image
     */
    public static Image getImage(NCPFFile ncpf){
        return FileWriter.PNG.write(ncpf);
    }
    /**
     * Loads a file and creates an image for the first contained multiblock
     * @param file the file to load
     * @return the first multiblock's image
     */
    public static Image getImage(File file){
        return getImage(loadFile(file));
    }
    /**
     * Loads a file and creates an image for the first contained multiblock
     * @param input the stream to load
     * @return the first multiblock's image
     */
    public static Image getImage(InputStreamProvider input){
        return getImage(loadFile(input));
    }
    /**
     * Creates images for all contained multiblocks in an NCPFFile
     * @param ncpf the file to load
     * @return an array containing all multiblocks' images
     */
    public static Image[] getImages(NCPFFile ncpf){
        Image[] images = new Image[ncpf.multiblocks.size()];
        for(int i = 0; i<ncpf.multiblocks.size(); i++){
            NCPFFile ncpf1 = new NCPFFile();
            ncpf1.configuration = ncpf.configuration;
            ncpf1.metadata = new HashMap<>(ncpf.metadata);
            ncpf1.multiblocks.add(ncpf.multiblocks.get(i));
            images[i] = FileWriter.PNG.write(ncpf1);
        }
        return images;
    }
    /**
     * Loads a file and creates images for all contained multiblocks
     * @param file the file to load
     * @return an array containing all multiblocks' images
     */
    public static Image[] getImages(File file){
        return getImages(loadFile(file));
    }
    /**
     * Loads a file and creates images for all contained multiblocks
     * @param input the stream to load
     * @return an array containing all multiblocks' images
     */
    public static Image[] getImages(InputStreamProvider input){
        return getImages(loadFile(input));
    }
    /**
     * Returns the multiblock information for the first contained multiblock in an NCPFFile object
     * @param ncpf the NCPFFile to save
     * @return the first multiblock's information
     */
    public static String getInfo(NCPFFile ncpf){
        return ncpf.multiblocks.get(0).getSaveTooltip().text;
    }
    /**
     * Loads a file and returns the multiblock information for the first contained multiblock
     * @param file the file to load
     * @return the first multiblock's information
     */
    public static String getInfo(File file){
        return getInfo(loadFile(file));
    }
    /**
     * Loads a file and returns the multiblock information for the first contained multiblock
     * @param input the stream to load
     * @return the first multiblock's information
     */
    public static String getInfo(InputStreamProvider input){
        return getInfo(loadFile(input));
    }
    /**
     * Returns the multiblock information for all contained multiblocks in an NCPFFile
     * @param ncpf the file to load
     * @return an array containing all multiblocks' information
     */
    public static String[] getInfos(NCPFFile ncpf){
        String[] infos = new String[ncpf.multiblocks.size()];
        for(int i = 0; i<ncpf.multiblocks.size(); i++){
            infos[i] = ncpf.multiblocks.get(i).getSaveTooltip().text;
        }
        return infos;
    }
    /**
     * Loads a file and returns the multiblock information for all contained multiblocks
     * @param file the file to load
     * @return an array containing all multiblocks' information
     */
    public static String[] getInfos(File file){
        return getInfos(loadFile(file));
    }
    /**
     * Loads a file and returns the multiblock information for all contained multiblocks
     * @param input the stream to load
     * @return an array containing all multiblocks' information
     */
    public static String[] getInfos(InputStreamProvider input){
        return getInfos(loadFile(input));
    }
}