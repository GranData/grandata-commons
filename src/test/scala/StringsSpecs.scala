import java.nio.file.Files

import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll

/**
 * Created by gustavo on 16/04/15.
 */
class StringsSpecs extends Specification with BeforeAll {

  def beforeAll: Unit = {

  }


  "Strings" should {

    import com.grandata.commons.strings._
    """split "\t\t\t"  """ in {

      "\t\t\t".splitFields('\t') mustEqual Array("", "", "", "")

    }
  }
}
