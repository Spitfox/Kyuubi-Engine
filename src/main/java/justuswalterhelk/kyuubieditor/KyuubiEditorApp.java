package justuswalterhelk.kyuubieditor;

import justuswalterhelk.kyuubiforge.Core.ApplicationSpecification;
import justuswalterhelk.kyuubiforge.Core.WindowSpecification;

public class KyuubiEditorApp
{
    public static void main(String[] args)
    {
        WindowSpecification windowSpecification = new WindowSpecification(1920/2,1080/2,
                "KyuubiEditor (Development Version)", true);
        ApplicationSpecification specs = new ApplicationSpecification("KyuubiEditor", windowSpecification);
        KyuubiEditor editor = new KyuubiEditor(specs);
        editor.run();
    }
}
