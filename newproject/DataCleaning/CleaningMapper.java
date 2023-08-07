import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CleaningMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Text outputKey = new Text();
    private Text outputValue = new Text();
    private Set<String> stopWords = new HashSet<String>(Arrays.asList(
        "a", "about", "actually", "almost", "also", "although", "always", "am", "an", "and", "any", "are", "as", "at",
        "be", "became", "become", "but", "by", "can", "could", "did", "do", "does", "each", "either", "else", "for",
        "from", "had", "has", "have", "hence", "how", "i", "if", "in", "is", "it", "its", "just", "may", "maybe", "me",
        "might", "mine", "must", "my", "neither", "nor", "not", "of", "oh", "ok", "when", "where", "whereas", "wherever",
        "whenever", "whether", "which", "while", "who", "whom", "whoever", "whose", "why", "will", "with", "within",
        "without", "would", "yes", "yet", "you", "your"
    ));

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        line = line.toLowerCase(); // Convert to lowercase
        line = line.replaceAll("<br />", " "); // Remove HTML tags
        line = line.replaceAll("\\p{Punct}", " "); // Remove symbols
        line = line.replaceAll("\\s+", " "); // Remove extra spaces
        line = line.replaceAll("\\d", ""); // Remove all numbers

        // Remove non-printable characters
        line = line.replaceAll("[^\\x20-\\x7E]", "");

        // Remove stop words and filter out unwanted entries
        StringBuilder cleanedLine = new StringBuilder();
        for (String word : line.split(" ")) {
            if (!stopWords.contains(word) 
                && !word.trim().isEmpty() 
                && word.trim().length() > 1 
                && !word.equals("t")
                && !isRepeatedChars(word)) {
                cleanedLine.append(word).append(" ");
            }
        }

        String finalCleanedLine = cleanedLine.toString().trim();
        if (!finalCleanedLine.isEmpty()) {
            outputKey.set(finalCleanedLine);
            outputValue.set(""); // You can set this to any value or even the same cleaned line if needed
            context.write(outputKey, outputValue);
        }
    }

    private boolean isRepeatedChars(String word) {
        char firstChar = word.charAt(0);
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }
}
