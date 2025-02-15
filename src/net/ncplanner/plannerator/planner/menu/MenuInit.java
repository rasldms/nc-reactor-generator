package net.ncplanner.plannerator.planner.menu;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Supplier;
import net.ncplanner.plannerator.multiblock.configuration.Configuration;
import net.ncplanner.plannerator.multiblock.configuration.TextureManager;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Main;
import net.ncplanner.plannerator.planner.Task;
import net.ncplanner.plannerator.planner.Updater;
import net.ncplanner.plannerator.planner.VersionManager;
import net.ncplanner.plannerator.planner.file.FileReader;
import net.ncplanner.plannerator.planner.file.FormatReader;
import net.ncplanner.plannerator.planner.file.reader.NCPF10Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF11Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF1Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF2Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF3Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF4Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF5Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF6Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF7Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF8Reader;
import net.ncplanner.plannerator.planner.file.reader.NCPF9Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR1Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR2Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR3Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR4Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR5Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageMSR6Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR1Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR2Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR3Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR4Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR5Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulHellrageSFR6Reader;
import net.ncplanner.plannerator.planner.file.reader.OverhaulNCConfigReader;
import net.ncplanner.plannerator.planner.file.reader.UnderhaulHellrage1Reader;
import net.ncplanner.plannerator.planner.file.reader.UnderhaulHellrage2Reader;
import net.ncplanner.plannerator.planner.file.reader.UnderhaulNCConfigReader;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentProgressBar;
import net.ncplanner.plannerator.planner.menu.dialog.MenuUpdate;
import net.ncplanner.plannerator.planner.module.FusionTestModule;
import net.ncplanner.plannerator.planner.module.Module;
import net.ncplanner.plannerator.planner.module.OverhaulModule;
import net.ncplanner.plannerator.planner.module.PrimeFuelModule;
import net.ncplanner.plannerator.planner.module.RainbowFactorModule;
import net.ncplanner.plannerator.planner.module.UnderhaulModule;
import net.ncplanner.plannerator.planner.theme.Theme;
import simplelibrary.Sys;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
import simplelibrary.error.ErrorCategory;
import simplelibrary.error.ErrorLevel;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
public class MenuInit extends Menu{
    private final Task init;
    HashMap<String, Supplier<FormatReader>> readers = new HashMap<>();
    ArrayList<String> readerNames = new ArrayList<>();
    HashMap<String, Task> readerTasks = new HashMap<>();
    private final MenuComponentProgressBar progressBar;
    {
        addReader("NCPF11Reader", ()->{return new NCPF11Reader();});// .ncpf version 11
        addReader("NCPF10Reader", ()->{return new NCPF10Reader();});// .ncpf version 10
        addReader("NCPF9Reader", ()->{return new NCPF9Reader();});// .ncpf version 9
        addReader("NCPF8Reader", ()->{return new NCPF8Reader();});// .ncpf version 8
        addReader("NCPF7Reader", ()->{return new NCPF7Reader();});// .ncpf version 7
        addReader("NCPF6Reader", ()->{return new NCPF6Reader();});// .ncpf version 6
        addReader("NCPF5Reader", ()->{return new NCPF5Reader();});// .ncpf version 5
        addReader("NCPF4Reader", ()->{return new NCPF4Reader();});// .ncpf version 4
        addReader("NCPF3Reader", ()->{return new NCPF3Reader();});// .ncpf version 3
        addReader("NCPF2Reader", ()->{return new NCPF2Reader();});// .ncpf version 2
        addReader("NCPF1Reader", ()->{return new NCPF1Reader();});// .ncpf version 1
        addReader("OverhaulHellrageSFR6Reader", ()->{return new OverhaulHellrageSFR6Reader();});// hellrage SFR .json 2.1.1-2.1.7 (present)
        addReader("OverhaulHellrageSFR5Reader", ()->{return new OverhaulHellrageSFR5Reader();});// hellrage SFR .json 2.0.32-2.0.37
        addReader("OverhaulHellrageSFR4Reader", ()->{return new OverhaulHellrageSFR4Reader();});// hellrage SFR .json 2.0.31
        addReader("OverhaulHellrageSFR3Reader", ()->{return new OverhaulHellrageSFR3Reader();});// hellrage SFR .json 2.0.30
        addReader("OverhaulHellrageSFR2Reader", ()->{return new OverhaulHellrageSFR2Reader();});// hellrage SFR .json 2.0.7-2.0.29
        addReader("OverhaulHellrageSFR1Reader", ()->{return new OverhaulHellrageSFR1Reader();});// hellrage SFR .json 2.0.1-2.0.6
        addReader("UnderhaulHellrage2Reader", ()->{return new UnderhaulHellrage2Reader();});// hellrage .json 1.2.23-1.2.25 (present)
        addReader("UnderhaulHellrage1Reader", ()->{return new UnderhaulHellrage1Reader();});// hellrage .json 1.2.5-1.2.22
        addReader("OverhaulHellrageMSR6Reader", ()->{return new OverhaulHellrageMSR6Reader();});// hellrage MSR .json 2.1.1-2.1.7 (present)
        addReader("OverhaulHellrageMSR5Reader", ()->{return new OverhaulHellrageMSR5Reader();});// hellrage MSR .json 2.0.32-2.0.37
        addReader("OverhaulHellrageMSR4Reader", ()->{return new OverhaulHellrageMSR4Reader();});// hellrage MSR .json 2.0.31
        addReader("OverhaulHellrageMSR3Reader", ()->{return new OverhaulHellrageMSR3Reader();});// hellrage MSR .json 2.0.30
        addReader("OverhaulHellrageMSR2Reader", ()->{return new OverhaulHellrageMSR2Reader();});// hellrage MSR .json 2.0.7-2.0.29
        addReader("OverhaulHellrageMSR1Reader", ()->{return new OverhaulHellrageMSR1Reader();});// hellrage MSR .json 2.0.1-2.0.6
        addReader("OverhaulNCConfigReader", ()->{return new OverhaulNCConfigReader();});// OVERHAUL nuclearcraft.cfg
        addReader("UnderhaulNCConfigReader", ()->{return new UnderhaulNCConfigReader();});// UNDERHAUL nuclearcraft.cfg
    }
    private  void addReader(String s, Supplier<FormatReader> reader){
        readerNames.add(s);
        readers.put(s, reader);
    }
    public MenuInit(GUI gui){
        super(gui, null);
        progressBar = add(new MenuComponentProgressBar(0, 0, gui.helper.displayWidth(), gui.helper.displayHeight(), 3){
            @Override
            public Task getTask(){
                return init;
            }
        });
        init = new Task("Initializing...");
        Task t2 = init.addSubtask("Resetting Metadata");
        Task tf = init.addSubtask("Adding File Readers...");
        for(String s : readerNames){
            readerTasks.put(s, tf.addSubtask("Adding "+s+"..."));
        }
        Task tc = init.addSubtask("Initializing Configurations...");
        Task tc1 = tc.addSubtask("Initializing Nuclearcraft Configuration");
        Task tm = init.addSubtask("Adding modules...");
        Task tm1 = tm.addSubtask("Adding Underhaul Module");
        Task tm2 = tm.addSubtask("Adding Overhaul Module");
        Task tm3 = tm.addSubtask("Adding Fusion Test Module");
        Task tm4 = tm.addSubtask("Adding Rainbow Factor Module");
        Task tm5 = tm.addSubtask("Adding Prime Fuel Module");
        Task ts = init.addSubtask("Loading settings...");
        Task tmr = init.addSubtask("Refreshing modules...");
        Task tct = init.addSubtask("Adjusting MSR block textures...");
        Task tci = init.addSubtask("Imposing Configuration...");
        new Thread(() -> {
            System.out.println("Started Initialization Thread");
            Core.resetMetadata();
            System.out.println("Reset Metadata");
            t2.finish();
            for(String s : readerNames){
                FileReader.formats.add(readers.get(s).get());
                readerTasks.get(s).finish();
            }
            System.out.println("Loaded File Formats");
            Configuration.initNuclearcraftConfiguration();
            System.out.println("Loaded NC Config");
            tc1.finish();
            Core.modules.add(new UnderhaulModule());
            tm1.finish();
            Core.modules.add(new OverhaulModule());
            tm2.finish();
            Core.modules.add(new FusionTestModule());
            tm3.finish();
            Core.modules.add(new RainbowFactorModule());
            tm4.finish();
            Core.modules.add(new PrimeFuelModule());
            tm5.finish();
            System.out.println("Added Modules");
            File f = new File("settings.dat").getAbsoluteFile();
            if(f.exists()){
                Config settings = Config.newConfig(f);
                settings.load();
                System.out.println("Loading theme");
                Object o = settings.get("theme");
                if(o instanceof String){
                    Core.setTheme(Theme.getByName((String)o));
                }else Core.setTheme(Theme.getByLegacyID((int)o));
                try{
                    Config modules = settings.get("modules", Config.newConfig());
                    HashMap<Module, Boolean> moduleStates = new HashMap<>();
                    for(String key : modules.properties()){
                        for(Module m : Core.modules){
                            if(m.name.equals(key))moduleStates.put(m, modules.getBoolean(key));
                        }
                    }
                    for(Module m : Core.modules){
                        if(!moduleStates.containsKey(m))continue;
                        if(m.isActive()){
                            if(!moduleStates.get(m))m.deactivate();
                        }else{
                            if(moduleStates.get(m))m.activate();
                        }
                    }
                }catch(Exception ex){}
                Core.tutorialShown = settings.get("tutorialShown", false);
                Core.invertUndoRedo = settings.get("invertUndoRedo", false);
                Core.autoBuildCasing = settings.get("autoBuildCasing", true);
                ConfigList lst = settings.getConfigList("pins", new ConfigList());
                for(int i = 0; i<lst.size(); i++){
                    Core.pinnedStrs.add(lst.getString(i));
                }
            }
            System.out.println("Loaded Settings");
            ts.finish();
            Core.refreshModules();
            System.out.println("Refreshed Modules");
            tmr.finish();
            for(Configuration configuration : Configuration.configurations){
                if(configuration.overhaul!=null&&configuration.overhaul.fissionMSR!=null){
                    for(net.ncplanner.plannerator.multiblock.configuration.overhaul.fissionmsr.Block b : configuration.overhaul.fissionMSR.allBlocks){
                        if(b.heater&&!b.getDisplayName().contains("Standard")){
                            try{
                                b.setInternalTexture(TextureManager.getImage("overhaul/"+b.getDisplayName().toLowerCase(Locale.ROOT).replace(" coolant heater", "").replace("liquid ", "")));
                            }catch(Exception ex){
                                Sys.error(ErrorLevel.warning, "Failed to load internal texture for MSR Block: "+b.name, ex, ErrorCategory.fileIO);
                            }
                        }
                    }
                }
            }
            System.out.println("Set MSR Textures");
            tct.finish();
            Configuration.configurations.get(0).impose(Core.configuration);
            System.out.println("Imposed Configuration");
            tci.finish();
            if(Main.isBot)gui.open(new MenuDiscord(gui));
            else{
                if(!Core.tutorialShown&&!Main.headless){
                    gui.open(new MenuTutorial(gui, gui.menu));
                    Core.tutorialShown = true;
                }
                gui.open(new MenuMain(gui));
                gui.open(new MenuStackEditor(gui, gui.menu));
            }
            System.out.println("Checking for updates...");
            Updater updater = Updater.read("https://raw.githubusercontent.com/ThizThizzyDizzy/nc-reactor-generator/overhaul/versions.txt", VersionManager.currentVersion, "NC-Reactor-Generator");
            if(updater!=null&&updater.getVersionsBehindLatestDownloadable()>0){
                gui.menu = new MenuUpdate(gui, gui.menu, updater);
            }
            System.out.println("Update Check Complete.");
        }, "Initialization Thread").start();
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        progressBar.width = gui.helper.displayWidth();
        progressBar.height = gui.helper.displayHeight();
    }
}