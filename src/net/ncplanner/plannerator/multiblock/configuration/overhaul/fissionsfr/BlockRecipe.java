package net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionsfr;
import java.util.ArrayList;
import java.util.Objects;
import net.ncplanner.plannerator.multiblock.configuration.IBlockRecipe;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Pinnable;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
import simplelibrary.config2.ConfigNumberList;
import simplelibrary.image.Image;
public class BlockRecipe implements Pinnable, IBlockRecipe{
    public static BlockRecipe irradiatorRecipe(String inputName, String inputDisplayName, String inputTexture, String outputName, String outputDisplayName, String outputTexture, float efficiency, float heat){
        BlockRecipe recipe = new BlockRecipe(inputName, outputName);
        recipe.inputDisplayName = inputDisplayName;
        recipe.inputLegacyNames.add(inputDisplayName);
        recipe.setInputTexture(TextureManager.getImage(inputTexture));
        recipe.outputDisplayName = outputDisplayName;
        recipe.setOutputTexture(TextureManager.getImage(outputTexture));
        recipe.irradiatorEfficiency = efficiency;
        recipe.irradiatorHeat = heat;
        return recipe;
    }
    public static BlockRecipe fuel(String inputName, String inputDisplayName, String inputTexture, String outputName, String outputDisplayName, String outputTexture, float efficiency, int heat, int time, int criticality, boolean selfPriming){
        BlockRecipe recipe = new BlockRecipe(inputName, outputName);
        recipe.inputDisplayName = inputDisplayName;
        recipe.inputLegacyNames.add(inputDisplayName);
        recipe.setInputTexture(TextureManager.getImage(inputTexture));
        recipe.outputDisplayName = outputDisplayName;
        recipe.setOutputTexture(TextureManager.getImage(outputTexture));
        recipe.fuelCellEfficiency = efficiency;
        recipe.fuelCellHeat = heat;
        recipe.fuelCellTime = time;
        recipe.fuelCellCriticality = criticality;
        recipe.fuelCellSelfPriming = selfPriming;
        return recipe;
    }
    public String inputName;
    public String inputDisplayName;
    public ArrayList<String> inputLegacyNames = new ArrayList<>();
    public Image inputTexture;
    public Image inputDisplayTexture;
    public int inputRate;
    public String outputName;
    public String outputDisplayName;
    public Image outputTexture;
    public Image outputDisplayTexture;
    public int outputRate;
    public float fuelCellEfficiency;
    public int fuelCellHeat;
    public int fuelCellTime;
    public int fuelCellCriticality;
    public boolean fuelCellSelfPriming;
    public float irradiatorEfficiency;
    public float irradiatorHeat;
    public float reflectorEfficiency;
    public float reflectorReflectivity;
    public int moderatorFlux;
    public float moderatorEfficiency;
    public boolean moderatorActive;
    public int shieldHeat;
    public float shieldEfficiency;
    public int heatsinkCooling;
    public BlockRecipe(String inputName, String outputName){
        this.inputName = inputName;
        this.outputName = outputName;
    }
    public Config save(Block block, boolean partial){
        Config config = Config.newConfig();
        Config inputCfg = Config.newConfig();
        inputCfg.set("name", inputName);
        if(!partial){
            if(inputDisplayName!=null)inputCfg.set("displayName", inputDisplayName);
            if(!inputLegacyNames.isEmpty()){
                ConfigList lst = new ConfigList();
                for(String s : inputLegacyNames)lst.add(s);
                inputCfg.set("legacyNames", lst);
            }
            if(inputTexture!=null){
                ConfigNumberList tex = new ConfigNumberList();
                tex.add(inputTexture.getWidth());
                for(int x = 0; x<inputTexture.getWidth(); x++){
                    for(int y = 0; y<inputTexture.getHeight(); y++){
                        tex.add(inputTexture.getRGB(x, y));
                    }
                }
                inputCfg.set("texture", tex);
            }
        }
        if(inputRate!=0)inputCfg.set("rate", inputRate);
        config.set("input", inputCfg);
        Config outputCfg = Config.newConfig();
        outputCfg.set("name", outputName);
        if(!partial){
            if(outputDisplayName!=null)outputCfg.set("displayName", outputDisplayName);
            if(outputTexture!=null){
                ConfigNumberList tex = new ConfigNumberList();
                tex.add(outputTexture.getWidth());
                for(int x = 0; x<outputTexture.getWidth(); x++){
                    for(int y = 0; y<outputTexture.getHeight(); y++){
                        tex.add(outputTexture.getRGB(x, y));
                    }
                }
                outputCfg.set("texture", tex);
            }
        }
        if(outputRate!=0)outputCfg.set("rate", outputRate);
        config.set("output", outputCfg);
        if(block.fuelCell){
            Config fuelCellCfg = Config.newConfig();
            fuelCellCfg.set("efficiency", fuelCellEfficiency);
            fuelCellCfg.set("heat", fuelCellHeat);
            fuelCellCfg.set("time", fuelCellTime);
            fuelCellCfg.set("criticality", fuelCellCriticality);
            if(fuelCellSelfPriming)fuelCellCfg.set("selfPriming", true);
            config.set("fuelCell", fuelCellCfg);
        }
        if(block.irradiator){
            Config irradiatorCfg = Config.newConfig();
            irradiatorCfg.set("efficiency", irradiatorEfficiency);
            irradiatorCfg.set("heat", irradiatorHeat);
            config.set("irradiator", irradiatorCfg);
        }
        if(block.reflector){
            Config reflectorCfg = Config.newConfig();
            reflectorCfg.set("efficiency", reflectorEfficiency);
            reflectorCfg.set("reflectivity", reflectorReflectivity);
            config.set("reflector", reflectorCfg);
        }
        if(block.moderator){
            Config moderatorCfg = Config.newConfig();
            moderatorCfg.set("flux", moderatorFlux);
            moderatorCfg.set("efficiency", moderatorEfficiency);
            if(moderatorActive)moderatorCfg.set("active", true);
            config.set("moderator", moderatorCfg);
        }
        if(block.shield){
            Config shieldCfg = Config.newConfig();
            shieldCfg.set("heat", shieldHeat);
            shieldCfg.set("efficiency", shieldEfficiency);
            config.set("shield", shieldCfg);
        }
        if(block.heatsink){
            Config heatsinkCfg = Config.newConfig();
            heatsinkCfg.set("cooling", heatsinkCooling);
            config.set("heatsink", heatsinkCfg);
        }
        return config;
    }
    public Image getInputTexture(){
        return inputTexture;
    }
    public void setInputTexture(Image image){
        inputTexture = image;
        inputDisplayTexture = TextureManager.convert(image);
    }
    public Image getOutputTexture(){
        return outputTexture;
    }
    public void setOutputTexture(Image image){
        outputTexture = image;
        outputDisplayTexture = TextureManager.convert(image);
    }
    @Override
    public boolean equals(Object obj){
        if(obj==null)return false;
        BlockRecipe r = (BlockRecipe)obj;
        return Objects.equals(r.inputName, inputName)
                &&Objects.equals(r.inputDisplayName, inputDisplayName)
                &&r.inputLegacyNames.equals(inputLegacyNames)
                &&Core.areImagesEqual(r.inputTexture, inputTexture)
                &&r.inputRate==inputRate
                &&Objects.equals(r.outputName, outputName)
                &&Objects.equals(r.outputDisplayName, outputDisplayName)
                &&Core.areImagesEqual(r.outputTexture, outputTexture)
                &&r.outputRate==outputRate
                &&r.fuelCellEfficiency==fuelCellEfficiency
                &&r.fuelCellHeat==fuelCellHeat
                &&r.fuelCellTime==fuelCellTime
                &&r.fuelCellCriticality==fuelCellCriticality
                &&r.fuelCellSelfPriming==fuelCellSelfPriming
                &&r.irradiatorEfficiency==irradiatorEfficiency
                &&r.irradiatorHeat==irradiatorHeat
                &&r.reflectorEfficiency==reflectorEfficiency
                &&r.reflectorReflectivity==reflectorReflectivity
                &&r.moderatorFlux==moderatorFlux
                &&r.moderatorEfficiency==moderatorEfficiency
                &&r.moderatorActive==moderatorActive
                &&r.shieldHeat==shieldHeat
                &&r.shieldEfficiency==shieldEfficiency
                &&r.heatsinkCooling==heatsinkCooling;
    }
    @Override
    public String getInputDisplayName(){
        return inputDisplayName==null?inputName:inputDisplayName;
    }
    public ArrayList<String> getLegacyNames(){
        ArrayList<String> allNames = new ArrayList<>(inputLegacyNames);
        allNames.add(inputName);
        return allNames;
    }
    public String getOutputDisplayName(){
        return outputDisplayName==null?outputName:outputDisplayName;
    }
    @Override
    public ArrayList<String> getSearchableNames(){
        return getSimpleSearchableNames();
    }
    @Override
    public ArrayList<String> getSimpleSearchableNames(){
        ArrayList<String> lst = getLegacyNames();
        lst.add(getInputDisplayName());
        return lst;
    }
    @Override
    public String getPinnedName(){
        return inputName;
    }
    public void setInputName(String inputName){
        this.inputName = inputName;
    }
    public void setInputDisplayName(String inputDisplayName){
        this.inputDisplayName = inputDisplayName;
    }
    public void setOutputName(String outputName){
        this.outputName = outputName;
    }
    public void setOutputDisplayName(String outputDisplayName){
        this.outputDisplayName = outputDisplayName;
    }
    public void setInputLegacyNames(ArrayList<String> inputLegacyNames){
        this.inputLegacyNames = new ArrayList<>(inputLegacyNames);
    }
    public int getInputRate(){
        return inputRate;
    }
    public int getOutputRate(){
        return outputRate;
    }
    public float getFuelCellEfficiency(){
        return fuelCellEfficiency;
    }
    public int getFuelCellHeat(){
        return fuelCellHeat;
    }
    public int getFuelCellCriticality(){
        return fuelCellCriticality;
    }
    public boolean isFuelCellSelfPriming(){
        return fuelCellSelfPriming;
    }
    public float getIrradiatorEfficiency(){
        return irradiatorEfficiency;
    }
    public float getIrradiatorHeat(){
        return irradiatorHeat;
    }
    public float getReflectorEfficiency(){
        return reflectorEfficiency;
    }
    public float getReflectorReflectivity(){
        return reflectorReflectivity;
    }
    public int getModeratorFlux(){
        return moderatorFlux;
    }
    public float getModeratorEfficiency(){
        return moderatorEfficiency;
    }
    public boolean isModeratorActive(){
        return moderatorActive;
    }
    public int getShieldHeat(){
        return shieldHeat;
    }
    public float getShieldEfficiency(){
        return shieldEfficiency;
    }
    public int getHeatsinkCooling(){
        return heatsinkCooling;
    }
    public void setInputRate(int inputRate){
        this.inputRate = inputRate;
    }
    public void setOutputRate(int outputRate){
        this.outputRate = outputRate;
    }
    public void setFuelCellEfficiency(float fuelCellEfficiency){
        this.fuelCellEfficiency = fuelCellEfficiency;
    }
    public void setFuelCellHeat(int fuelCellHeat){
        this.fuelCellHeat = fuelCellHeat;
    }
    public void setFuelCellCriticality(int fuelCellCriticality){
        this.fuelCellCriticality = fuelCellCriticality;
    }
    public void setFuelCellSelfPriming(boolean fuelCellSelfPriming){
        this.fuelCellSelfPriming = fuelCellSelfPriming;
    }
    public void setIrradiatorEfficiency(float irradiatorEfficiency){
        this.irradiatorEfficiency = irradiatorEfficiency;
    }
    public void setIrradiatorHeat(float irradiatorHeat){
        this.irradiatorHeat = irradiatorHeat;
    }
    public void setReflectorEfficiency(float reflectorEfficiency){
        this.reflectorEfficiency = reflectorEfficiency;
    }
    public void setReflectorReflectivity(float reflectorReflectivity){
        this.reflectorReflectivity = reflectorReflectivity;
    }
    public void setModeratorFlux(int moderatorFlux){
        this.moderatorFlux = moderatorFlux;
    }
    public void setModeratorEfficiency(float moderatorEfficiency){
        this.moderatorEfficiency = moderatorEfficiency;
    }
    public void setModeratorActive(boolean moderatorActive){
        this.moderatorActive = moderatorActive;
    }
    public void setShieldHeat(int shieldHeat){
        this.shieldHeat = shieldHeat;
    }
    public void setShieldEfficiency(float shieldEfficiency){
        this.shieldEfficiency = shieldEfficiency;
    }
    public void setHeatsinkCooling(int heatsinkCooling){
        this.heatsinkCooling = heatsinkCooling;
    }
}