package common.util;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Random;


public class RandomUtils {

    private static final Logger log = Logger.getLogger(RandomUtils.class);

    public String getRand() {
        return String.valueOf(System.currentTimeMillis() % 1000);
    }
    public String getRandName(String part) {
        return getRandomVowel()+getRandomVowel()+getRand()+part;
    }
    public String getRandFromList(List<String> listWithStrings) {
        return listWithStrings.get((new Random()).nextInt(listWithStrings.size()));
    }
    public String getRandomAlphabetChar(){
        Random r = new Random();
        return String.valueOf((char)(r.nextInt(26)+'a'));
    }
    private String getRandomVowel(){
        char[] vowel = {'a','e','i','o','u'};
        Random r = new Random();
        return String.valueOf(vowel[r.nextInt(vowel.length)]);
    }
    private String getRandomConsonant(){
        char[] consonant = {'b','c','d','f','g','h','j','k','l', 'm','n','p','m','q','r','s','t','v','w','x','y','z'};
        Random r = new Random();
        return String.valueOf(consonant[r.nextInt(consonant.length)]);
    }
    public String getLoremIpsum(){
        return  new RandomUtils().getRandomConsonant()+
                new RandomUtils().getRandomVowel()+
                new RandomUtils().getRandomConsonant()+
                new RandomUtils().getRandomVowel()+
                new RandomUtils().getRandomConsonant()+
                new RandomUtils().getRandomVowel()+
                new RandomUtils().getRandomConsonant()+
                new RandomUtils().getRandomVowel()+
                new RandomUtils().getRandomConsonant();
    }

}
