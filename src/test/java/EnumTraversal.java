import com.example.aitagstorage.tools.TagTables;

public class EnumTraversal {
    public static void main(String[] args) {
        for (TagTables tables : TagTables.values()) {
            System.out.println(tables);
        }
    }
}
