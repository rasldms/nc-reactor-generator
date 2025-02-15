package net.ncplanner.plannerator.discord;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.ImageIO;
import simplelibrary.font.FontManager;
import simplelibrary.image.Color;
import simplelibrary.image.Image;
public abstract class KeywordCommand extends Command{
    private HashMap<String, Keyword> keywords = new HashMap<>();
    private ArrayList<String> keywordOrder = new ArrayList<>();
    public KeywordCommand(String command, String... alternates){
        super(command, alternates);
        addKeywords();
    }
    public abstract void addKeywords();
    public void addKeyword(Keyword keyword){
        String regex = keyword.getRegex();
        keywords.put(regex, keyword);
        keywordOrder.add(regex);
    }
    @Override
    public final void run(User user, MessageChannel channel, String args, boolean debug){
        ArrayList<Keyword> words = new ArrayList<>();
        String str = args.toLowerCase(Locale.ROOT);
        for(String regex : keywordOrder){
            Pattern p = Pattern.compile("(^|\\s|,)"+regex+"(\\s|$|,)");
            String theArgs = (keywords.get(regex).caseSensitive()?args:args.toLowerCase(Locale.ROOT));
            Matcher m = p.matcher(theArgs);
            int end = 0;
            while(m.find(end)){
                end = m.end()-1;
                Keyword key = keywords.get(regex).newInstance();
                String original = m.group().trim();
                if(!key.read(original))continue;
                words.add(key);
            }
            Keyword k = keywords.get(regex).newInstance();
            Color c = k.getColor();
            str = str.replaceAll("(?<!@)"+regex, "@@@@@"+c.getRGB()+"@@@$0@@@@@");
        }
        ArrayList<Object> debugText = new ArrayList<>();
        String[] strs = str.split("@@@@@");
        String text = "";
        for(String s : strs){
            if(s.contains("@@@")){
                debugText.add(new Color(Integer.parseInt(s.split("@@@")[0])));
                debugText.add(s.split("@@@")[1]);
                text+=s.split("@@@")[1];
            }else{
                debugText.add(new Color(.5f, .5f, .5f, 1));
                debugText.add(s);
                text+=s;
            }
        }
        int border = 5;
        int textHeight = 20;
        int wide = (int)FontManager.getLengthForStringWithHeight(args, textHeight)+1;
        for(Keyword w : words){
            wide = Math.max(wide, (int)FontManager.getLengthForStringWithHeight(w.name+" | "+w.input, textHeight)+1);
        }
        int width = wide;
        if(!text.isEmpty()){
            Image image = Bot.makeImage(width+border*2, textHeight*(1+words.size())+border*2, (buff) -> {
                Renderer renderer = new Renderer();
                Core.theme.drawKeywordBackground(0, 0, buff.width, buff.height, 1);
                double x = 5;
                for(Object o : debugText){
                    if(o instanceof Color){
                        renderer.setColor((Color)o);
                    }else if(o instanceof String){
                        String s = (String)o;
                        double len = FontManager.getLengthForStringWithHeight(s, textHeight);
                        renderer.drawText(x, border, width+border, border+textHeight, s);
                        x+=len;
                    }
                }
                for(int i = 0; i<words.size(); i++){
                    Keyword word = words.get(i);
                    renderer.setColor(word.getColor());
                    renderer.drawText(border, border+(i+1)*textHeight, width+border, border+(i+2)*textHeight, word.name+" | "+word.input);
                }
            });
            File debugFile = new File("debug.png");
            try{
                FileOutputStream fos = new FileOutputStream(debugFile);
                ImageIO.write(image, fos);
                fos.close();
            }catch(IOException ex){
                Logger.getLogger(KeywordCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
            channel.sendFile(debugFile, "debug.png").complete();
            debugFile.delete();
        }
        run(user, channel, words, debug);
    }
    public abstract void run(User user, MessageChannel channel, ArrayList<Keyword> keywords, boolean debug);
}